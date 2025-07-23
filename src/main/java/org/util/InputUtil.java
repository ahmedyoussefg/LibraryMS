package org.util;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    public static String prompt(String promptText) {
        System.out.print(promptText);
        return scanner.nextLine().trim();
    }

    public static String promptNonEmpty(String promptText) {
        while (true) {
            String input = prompt(promptText);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    public static int promptInt(String promptText) {
        while (true) {
            String input = prompt(promptText);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    public static boolean promptYesNo(String promptText) {
        String input = prompt(promptText + " (y/n): ");
        return input.equalsIgnoreCase("y");
    }
}
