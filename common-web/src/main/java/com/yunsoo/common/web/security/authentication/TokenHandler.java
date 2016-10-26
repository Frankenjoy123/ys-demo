package com.yunsoo.common.web.security.authentication;

import com.yunsoo.common.util.HashUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by:   Lijian
 * Created on:   2016-10-18
 * Descriptions:
 */
public final class TokenHandler {

    private static final String SPLITTER = ",";

    private static final String DELIMITER = ":";

    private Log log = LogFactory.getLog(this.getClass());

    private byte[] hashSalt;


    public TokenHandler(String hashSalt) {
        this.hashSalt = hashSalt.getBytes(StandardCharsets.UTF_8);
    }


    public String createToken(DateTime expires, String... values) {
        String expiresMillis = expires != null ? Long.toString(expires.getMillis(), 36) : "";
        StringBuilder sb = new StringBuilder(expiresMillis);
        for (String value : values) {
            sb.append(SPLITTER);
            if (value != null) {
                sb.append(value);
            }
        }
        return encodeToken(sb.toString());
    }

    public String createToken(DateTime expires, AuthAccount authAccount) {
        List<String> values = new ArrayList<>();
        values.add(authAccount.getId());
        values.add(authAccount.getOrgId());
        Map<String, String> details = authAccount.getDetails();
        if (details != null && !details.isEmpty()) {
            details.forEach((k, v) -> {
                values.add(String.format("%s%s%s", k, DELIMITER, v));
            });
        }
        return createToken(expires, values.toArray(new String[values.size()]));
    }

    public String[] parseToken(String token) {
        String src = decodeToken(token);
        if (src == null) {
            log.warn(String.format("token invalid [token: %s]", token));
            return null;
        }
        final String[] parts = src.split(SPLITTER);
        if (parts.length < 2) {
            log.warn(String.format("token invalid [token: %s]", token));
            return null;
        }
        if (parts[0].length() > 0) {
            long millis;
            try {
                millis = Long.parseLong(parts[0], 36);
            } catch (NumberFormatException e) {
                log.error(String.format("parse expire time %s failed", parts[0]), e);
                return null;
            }
            DateTime expires = new DateTime(millis);
            if (expires.isBeforeNow()) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("token expired [token: %s, expires: %s]", token, expires.toString()));
                }
                return null;
            }
        }
        return Arrays.copyOfRange(parts, 1, parts.length);
    }

    public AuthAccount parseTokenAsAuthAccount(String token) {
        if (token == null) {
            return null;
        }
        String[] values = parseToken(token);
        if (values == null || values.length == 0) {
            return null;
        } else {
            AuthAccount authAccount = new AuthAccount();
            if (values[0].length() > 0) {
                authAccount.setId(values[0]);
            }
            if (values.length > 1 && values[1].length() > 0) {
                authAccount.setOrgId(values[1]);
            }
            if (values.length > 2) {
                Map<String, String> details = new HashMap<>();
                authAccount.setDetails(details);
                for (int i = 2; i < values.length; i++) {
                    if (values[i].length() > 0) {
                        String[] detailArray = values[i].split(DELIMITER, 2);
                        details.put(
                                detailArray.length > 1 && detailArray[0].length() > 0 ? detailArray[0] : Integer.toString(i - 2),
                                detailArray[detailArray.length - 1]);
                    }
                }
            }
            return authAccount;
        }
    }


    //region private methods

    private String encodeToken(String src) {
        byte[] srcBytes = src.getBytes(StandardCharsets.UTF_8);

        byte[] hash = HashUtils.sha256(concat(srcBytes, hashSalt)); //hash

        return encode(concat(new byte[]{(byte) hash.length}, hash, srcBytes));
    }

    private String decodeToken(String token) {
        try {
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
            byte[] checkHash = HashUtils.sha256(concat(srcBytes, hashSalt));
            if (!compare(hash, checkHash)) {
                return null;
            }
            return new String(srcBytes, StandardCharsets.UTF_8);
        } catch (RuntimeException ex) {
            return null;
        }
    }


    private String encode(byte[] content) {
        return Base64.encodeBase64URLSafeString(content);
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

    //endregion

}
