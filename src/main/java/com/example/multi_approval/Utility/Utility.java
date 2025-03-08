package com.example.multi_approval.Utility;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Utility {

    private static final Random random = new Random();

    public static String generateLoginId(String prefix, int numberLength) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < numberLength; i++) {
            sb.append(random.nextInt(10)); // 0-9
        }
        return sb.toString();
    }
}
