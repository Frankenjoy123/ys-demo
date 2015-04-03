package com.yunsoo.api.security;

/**
 * Created by Zhe on 2015/3/5.
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.object.TAccount;

public final class TokenHandler {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String SEPARATOR = ".";
    private static final String SEPARATOR_SPLITTER = "\\.";

    private final Mac hmac;

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
                    final TAccount tAccount = fromJSON(userBytes);
                }
            } catch (IllegalArgumentException e) {
                //log tempering attempt here
            }
        }
        return null;
    }

    public String createTokenForUser(TAccount user) {
        byte[] userBytes = toJSON(user);
        byte[] hash = createHmac(userBytes);
        final StringBuilder sb = new StringBuilder(170); //170
        sb.append(toBase64(userBytes));
        sb.append(SEPARATOR);
        sb.append(toBase64(hash));
        return sb.toString();
    }

    private TAccount fromJSON(final byte[] userBytes) {
        try {
            return new ObjectMapper().readValue(new ByteArrayInputStream(userBytes), TAccount.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] toJSON(TAccount user) {
        try {
            return new ObjectMapper().writeValueAsBytes(user);
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
/*
    public static void main(String[] args) {
		Date start = new Date();
		byte[] secret = new byte[70];
		new java.security.SecureRandom().nextBytes(secret);

		TokenHandler tokenHandler = new TokenHandler(secret);
		for (int i = 0; i < 1000; i++) {
			final User user = new User(java.util.UUID.randomUUID().toString().substring(0, 8), new Date(
					new Date().getTime() + 10000));
			user.grantRole(UserRole.ADMIN);
			final String token = tokenHandler.createTokenForUser(user);
			final User parsedUser = tokenHandler.parseUserFromToken(token);
			if (parsedUser == null || parsedUser.getUsername() == null) {
				System.out.println("error");
			}
		}
		System.out.println(System.currentTimeMillis() - start.getTime());
	}
*/
}
