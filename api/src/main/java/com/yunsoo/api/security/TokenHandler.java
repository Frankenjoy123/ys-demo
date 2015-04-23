package com.yunsoo.api.security;

import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TAccountStatusEnum;
import com.yunsoo.common.util.HashUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */
public final class TokenHandler {

    private static final String SPLITTER = ",";
    private static final String SALT = "LEmrmtPfWc1txs9uC9A7PevSVSbBbQL0";

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenHandler.class);

    public TAccount parseAccessToken(String token) {
        String src = decodeToken(token);
        if (src == null) {
            //token invalid
            LOGGER.error("Token invalid [token: {}]", token);
            return new TAccount(TAccountStatusEnum.INVALID_TOKEN);
        }
        final String[] parts = src.split(SPLITTER);
        if (parts.length != 3) {
            LOGGER.error("Token invalid [token: {}]", token);
            return new TAccount(TAccountStatusEnum.INVALID_TOKEN);
        }
        String accountId = parts[0];
        String orgId = parts[1];
        DateTime expires = new DateTime(Long.parseLong(parts[2]));

        if (expires.isBeforeNow()) {
            LOGGER.error("Token expired [token: {}, expires: {}]", token, expires.toString());
            return new TAccount(TAccountStatusEnum.TOKEN_EXPIRED);
        }
        TAccount account = new TAccount(TAccountStatusEnum.ENABLED);
        account.setId(accountId);
        account.setOrgId(orgId);
        return account;
    }

    public String createAccessToken(String accountId, String orgId, DateTime expires) {
        return encodeToken(accountId + SPLITTER + orgId + SPLITTER + expires.getMillis());
    }

    private String encodeToken(String src) {
        byte[] srcBytes = src.getBytes(StandardCharsets.UTF_8);
        byte[] saltBytes = SALT.getBytes(StandardCharsets.UTF_8);

        byte[] hash = HashUtils.sha256(concat(srcBytes, saltBytes)); //hash

        return encode(concat(new byte[]{(byte) hash.length}, hash, srcBytes));
    }

    private String decodeToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return null;
            }
            byte[] bytes = decode(token);
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            byte hashLength = bytes[0];
            if (bytes.length <= hashLength + 1) {
                return null;
            }
            byte[] hash = Arrays.copyOfRange(bytes, 1, hashLength + 1);
            byte[] srcBytes = Arrays.copyOfRange(bytes, hashLength + 1, bytes.length);
            byte[] saltBytes = SALT.getBytes(StandardCharsets.UTF_8);
            byte[] checkHash = HashUtils.sha256(concat(srcBytes, saltBytes));
            if (!compare(hash, checkHash)) {
                return null;
            }
            return StringUtils.newStringUtf8(srcBytes);
        } catch (RuntimeException ex) {
            return null;
        }
    }


    private String encode(byte[] content) {
        return Base64.encodeBase64String(content);
    }

    private byte[] decode(String content) {
        return Base64.decodeBase64(content);
    }

    private byte[] concat(byte[] first, byte[]... others) {
        int length = first.length;

        for (byte[] other : others) {
            length += other.length;
        }
        byte[] result = new byte[length];
        System.arraycopy(first, 0, result, 0, first.length);
        int offset = first.length;
        for (byte[] other : others) {
            System.arraycopy(other, 0, result, offset, other.length);
            offset += other.length;
        }
        return result;
    }

    private boolean compare(byte[] bytes1, byte[] bytes2) {
        if (bytes1 == null || bytes2 == null || bytes1.length != bytes2.length) {
            return false;
        }
        for (int i = 0; i < bytes1.length; i++) {
            if (bytes1[i] != bytes2[i]) {
                return false;
            }
        }
        return true;
    }

}
