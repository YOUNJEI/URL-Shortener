package com.url.urlshortener.utility;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class ShortenerAlgorithm {
    private String BASE62_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String createShortUrl(final String origin) {
        try {
            return getStringConvertToBase62(getBytesSHA256(origin));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getBytesSHA256(final String origin) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(origin.getBytes());
        return md.digest();
    }

    private String getStringConvertToBase62(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();

        // convert to binary fields
        for (int i = 0; i < 5; i++)
            stringBuilder.append(Integer.toBinaryString(bytes[i] & 255 | 256).substring(1));
        stringBuilder.append("00"); // padding bits

        String bits = stringBuilder.toString();
        stringBuilder.setLength(0); // reset

        for (int i = 0; i < bits.length(); i += 6) {
            char c = 0;

            for (int j = 0; j < 6; j++)
                c += (bits.charAt(i + j) - '0') * Math.pow(2, 5 - j);
            stringBuilder.append(BASE62_CHARSET.charAt(c));
        }
        return stringBuilder.toString();
    }
}
