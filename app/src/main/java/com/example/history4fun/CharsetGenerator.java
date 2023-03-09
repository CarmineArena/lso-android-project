package com.example.history4fun;

import java.security.SecureRandom;
import java.util.Random;

public class CharsetGenerator {
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#!?_";
    private final int len;
    private final Random random = new SecureRandom();

    public CharsetGenerator(int len) {
        this.len = len;
    }

    public String get_generated_random_string() {
        StringBuilder sb = new StringBuilder(this.len);
        for (int i = 0; i < this.len; i++) {
            sb.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }
}