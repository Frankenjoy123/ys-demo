package com.yunsoo.api.rabbit.security;

/**
 * Created by Zhe on 2015/3/5.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.rabbit.object.TAccount;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public final class TokenHandler {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String SEPARATOR = ".";
    private static final String SEPARATOR_SPLITTER = "\\.";

    private final Mac hmac;
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenHandler.class);

    public TokenHandler(byte[] secretKey) {
        try {
            hmac = Mac.getInstance(HMAC_ALGO);
            hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
        } catch (InvalidKeyException e) {
            throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
        }
    }

    public TAccount parseUserFromToken(String token) {
        final String[] parts = token.split(SEPARATOR_SPLITTER);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
            try {
                final byte[] userBytes = fromBase64(parts[0]);
                final byte[] hash = fromBase64(parts[1]);

                //Since the createHmac method uses an undisclosed secret key internally to compute the hash,
                // no client will be able to tamper with the content and provide a hash that is the same as the one the server will produce
                boolean validHash = Arrays.equals(createHmac(userBytes), hash);
                if (validHash) {
                    final TAccount tAccount = new TAccount();
                    String[] userInfoArray = fromJSON(userBytes).split(",");
                    if (userInfoArray == null || userInfoArray.length <= 0) {
                        LOGGER.error("ParseUserFromToken error! UserInfoArray is empty, and UserBytes is: " + userBytes.toString());
                    }
                    tAccount.setId(Long.parseLong(userInfoArray[0]));
                    tAccount.setExpires(Long.parseLong(userInfoArray[1]));
                    return tAccount;
                }
            } catch (IllegalArgumentException e) {
                //log tempering attempt here
                LOGGER.error("parseUserFromToken Exception Message:  ", e.getMessage());
            }
        }
        return null;
    }

    public String createTokenForUser(TAccount user) {
        //generate user information to hash
        String userInfo = user.getId() + "," + user.getExpires(); // format:  [userid,expires]
        byte[] userBytes = toJSON(userInfo);
        byte[] hash = createHmac(userBytes);
        final StringBuilder sb = new StringBuilder(170); //170
        sb.append(toBase64(userBytes));
        sb.append(SEPARATOR);
        sb.append(toBase64(hash));
        return sb.toString();
    }

    private String fromJSON(final byte[] userBytes) {
        try {
            return new ObjectMapper().readValue(new ByteArrayInputStream(userBytes), String.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] toJSON(String userInfo) {
        try {
            return new ObjectMapper().writeValueAsBytes(userInfo);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String toBase64(byte[] content) {
        return DatatypeConverter.printBase64Binary(content);
    }

    private byte[] fromBase64(String content) {
        return DatatypeConverter.parseBase64Binary(content);
    }

    // synchronized to guard internal hmac object
    private synchronized byte[] createHmac(byte[] content) {
        return hmac.doFinal(content);
    }
}
