package com.yunsoo.auth.api.security.authentication;

import com.yunsoo.auth.api.security.AuthAccount;
import com.yunsoo.common.util.HashUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


/**
 * Created by  : Lijian
 * Created on  : 2015/6/10
 * Descriptions:
 */
public final class TokenHandler {

    private static final String SPLITTER = ",";

    private Log log = LogFactory.getLog(this.getClass());

    private byte[] hashSalt;


    public TokenHandler(String hashSalt) {
        this.hashSalt = hashSalt.getBytes(StandardCharsets.UTF_8);
    }


    public AuthAccount parseToken(String token) {
        String src = decodeToken(token);
        if (src == null) {
            log.error(String.format("Token invalid [token: %s]", token));
            return null;
        }
        final String[] parts = src.split(SPLITTER);
        if (parts.length < 2) {
            log.error(String.format("Token invalid [token: %s]", token));
            return null;
        }
        DateTime expires = new DateTime(Long.parseLong(parts[0]));
        String accountId = parts[1];
        String orgId = parts.length >= 3 ? parts[2] : null; //orgId is nullable

        if (expires.isBeforeNow()) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Token expired [token: %s, expires: %s]", token, expires.toString()));
            }
            return null;
        }

        AuthAccount account = new AuthAccount();
        account.setId(accountId);
        account.setOrgId(orgId);
        return account;
    }

    public String createToken(DateTime expires, String accountId, String orgId) {
        return encodeToken(expires.getMillis() + SPLITTER + accountId + SPLITTER + orgId);
    }

    public String createToken(DateTime expires, String accountId) {
        return encodeToken(expires.getMillis() + SPLITTER + accountId);
    }


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

}
