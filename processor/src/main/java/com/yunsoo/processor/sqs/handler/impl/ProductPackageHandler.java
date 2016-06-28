package com.yunsoo.processor.sqs.handler.impl;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.message.ProductPackageMessage;
import com.yunsoo.common.data.object.ProductPackageObject;
import com.yunsoo.common.data.object.TaskFileEntryObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.processor.domain.LogDomain;
import com.yunsoo.processor.domain.ProductPackageDomain;
import com.yunsoo.processor.domain.TaskFileDomain;
import com.yunsoo.processor.sqs.MessageSender;
import com.yunsoo.processor.sqs.handler.MessageHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-06-23
 * Descriptions:
 */
@Component
public class ProductPackageHandler implements MessageHandler<ProductPackageMessage> {

    private static final int BATCH_LIMIT = 1000;
    private static final int TIMEOUT_SECONDS = 600;
    private static final long DELAY_SECONDS = 60;

    private static final Pattern REGEXP_DATETIME_PREFIX = Pattern.compile("^\\d\\d\\d\\d\\-[01]\\d\\-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d\\.\\d\\d\\dZ.*");

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private TaskFileDomain taskFileDomain;

    @Autowired
    private ProductPackageDomain productPackageDomain;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private LogDomain logDomain;


    @Override
    public void process(ProductPackageMessage message) {
        String fileId = message.getTaskFileId();
        TaskFileEntryObject tObj = taskFileDomain.getTaskFileEntryById(fileId);
        if (tObj == null) {
            log.warn("taskFileEntry not found by id: " + fileId);
            return;
        }

        //check status code of task file entry
        if (LookupCodes.TaskFileStatus.FAILED.equals(tObj.getStatusCode())) {
            return;
        }
        if (!LookupCodes.TaskFileStatus.UPLOADED.equals(tObj.getStatusCode())
                && !LookupCodes.TaskFileStatus.PROCESSING.equals(tObj.getStatusCode())) {
            throw new RuntimeException("taskFileEntry status invalid. "
                    + StringFormatter.formatMap("fileId", fileId, "statusCode", tObj.getStatusCode()));
        }

        //get task file
        YSFile taskFile = taskFileDomain.getTaskFile(tObj.getOrgId(), fileId);
        if (taskFile == null) {
            throw new RuntimeException("taskFile not found " + StringFormatter.formatMap("orgId", tObj.getOrgId(), "fileId", fileId));
        }
        if (!LookupCodes.TaskFileType.PACKAGE.equals(taskFile.getHeader("file_type"))) {
            throw new RuntimeException("taskFile.file_type invalid " + StringFormatter.formatMap("file_type", taskFile.getHeader("file_type")));
        }

        //start
        DateTime startDateTime = DateTime.now();
        DateTime timeOutDateTime = startDateTime.plusSeconds(TIMEOUT_SECONDS);
        DateTime batchStartDateTime;

        int continueOffset = (message.getContinueOffset() == null || message.getContinueOffset() < 0) ? 0 : message.getContinueOffset();
        long totalTime = (message.getTotalTime() == null || message.getTotalTime() < 0) ? 0 : message.getTotalTime();
        List<String> lines = Arrays.asList(new String(taskFile.getContent(), StandardCharsets.UTF_8).split("\r\n"));
        int linesSize = lines.size();

        if (!LookupCodes.TaskFileStatus.PROCESSING.equals(tObj.getStatusCode())) {
            //update status of task file entry
            taskFileDomain.updateTaskFileEntryStatus(fileId, LookupCodes.TaskFileStatus.PROCESSING);
        }

        for (int lineSteps, i = continueOffset; i < linesSize; i += lineSteps) {
            List<ProductPackageObject> packages = new ArrayList<>();
            lineSteps = parseLines(lines, i, BATCH_LIMIT, packages);
            continueOffset += lineSteps;

            int requestCount = packages.size();
            if (requestCount <= 0) {
                continue;
            }
            batchStartDateTime = DateTime.now();
            int responseCount = productPackageDomain.batchPackage(packages);
            DateTime batchEndDateTime = DateTime.now();

            log.info("batch package " + StringFormatter.formatMap(
                    "fileId", fileId,
                    "linesSize", linesSize,
                    "lineSteps", lineSteps,
                    "requestCount", requestCount,
                    "responseCount", responseCount,
                    "seconds", (batchEndDateTime.getMillis() - batchStartDateTime.getMillis()) / 1000.0));

            if (continueOffset < linesSize && timeOutDateTime.getMillis() < batchEndDateTime.getMillis()) {
                //timeout
                log.info("timeout batch package " + StringFormatter.formatMap(
                        "fileId", fileId,
                        "linesSize", linesSize,
                        "seconds", (batchEndDateTime.getMillis() - startDateTime.getMillis()) / 1000.0));
                totalTime += batchEndDateTime.getMillis() - startDateTime.getMillis();

                message.setContinueOffset(continueOffset);
                message.setTotalTime(totalTime);
                messageSender.sendMessage(message, DELAY_SECONDS); //send another new message with delay seconds
                log.info("continuous message sent " + StringFormatter.formatMap("message", message));
                return;
            }
        }

        //finished successfully
        taskFileDomain.updateTaskFileEntryStatus(fileId, LookupCodes.TaskFileStatus.FINISHED);

        //end
        DateTime endDateTime = DateTime.now();
        totalTime += endDateTime.getMillis() - startDateTime.getMillis();
        logDomain.logInfo(ProductPackageMessage.PAYLOAD_TYPE,
                "finished " + StringFormatter.formatMap("totalSeconds", totalTime / 1000.0),
                fileId,
                "task_file_id");
        log.info("finished batch package " + StringFormatter.formatMap(
                "fileId", fileId,
                "seconds", (endDateTime.getMillis() - startDateTime.getMillis()) / 1000.0,
                "totalSeconds", totalTime / 1000.0));
    }

    private int parseLines(List<String> lines, int index, int keyLimit, List<ProductPackageObject> resultPackages) {
        int i = index, size = lines.size(), keyCount = 0;
        for (; i < size; i++) {
            ProductPackageObject obj = parseLine(lines.get(i), i);
            if (obj != null) {
                if (obj.getParentProductKey() != null) keyCount += 1;
                if (obj.getChildProductKeySet() != null) keyCount += obj.getChildProductKeySet().size();
                if (keyCount > keyLimit) {
                    break;
                }
                resultPackages.add(obj);
            }
        }
        return i - index;
    }

    //2016-06-20T12:13:14.123Z key:ckey,ckey,ckey
    private ProductPackageObject parseLine(String line, int lineNum) {
        line = line != null ? line.trim() : null;
        if (line == null || line.length() == 0) {
            return null;
        }
        ProductPackageObject obj = new ProductPackageObject();
        String exp;
        if (line.length() > 24 && REGEXP_DATETIME_PREFIX.matcher(line).matches()) {
            obj.setPackageDateTime(DateTime.parse(line.substring(0, 24)));
            exp = line.substring(24, line.length()).trim();
        } else {
            exp = line;
        }

        //key:ckey,ckey,ckey
        String[] tempArray = exp.split(":");
        if (tempArray.length == 2) {
            obj.setProductKey(tempArray[0]);
            obj.setChildProductKeySet(Arrays.asList(tempArray[1].split(",")).stream().filter(k -> k != null && k.length() > 0).collect(Collectors.toSet()));
            if (obj.getProductKey() != null && obj.getProductKey().length() > 0 && obj.getChildProductKeySet().size() > 0) {
                return obj;
            }
        }
        throw new RuntimeException("parse line failed at line " + lineNum + ": " + line);
    }
}
