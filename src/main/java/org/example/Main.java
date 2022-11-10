package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int maxCharCount(BlockingQueue<String> q, char letter) {
        String s;
        int max = 0;
        int count;
        try {
            for (int i = 0; i < 10000; i++) {
                count = 0;
                s = q.take();
                for (char c : s.toCharArray()) {
                    if (c == letter) {
                        count++;
                    }
                }
                if (count > max) {
                    max = count;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return max;
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> q1 = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> q2 = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> q3 = new ArrayBlockingQueue<>(100);

        Thread threadTextGen = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String s = generateText("abc", 10_000);
                try {
                    q1.put(s);
                    q2.put(s);
                    q3.put(s);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        threadTextGen.start();

        Thread threadA = new Thread(() -> {
            char c = 'a';
            int count = maxCharCount(q1, c);
            System.out.println("Max 'a' count: " + count);
        });
        threadA.start();

        Thread threadB = new Thread(() -> {
            char c = 'b';
            int count = maxCharCount(q2, c);
            System.out.println("Max 'b' count: " + count);
        });
        threadB.start();

        Thread threadC = new Thread(() -> {
            char c = 'c';
            int count = maxCharCount(q3, c);
            System.out.println("Max 'c' count: " + count);
        });
        threadC.start();
    }
}