package com.videorental.ui;

import java.util.Scanner;

public abstract class Menu {
    protected final Scanner scanner;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    public abstract void display();

    protected int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            }
            scanner.nextLine();
            System.out.println("Введіть ціле число.");
        }
    }

    protected double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            }
            scanner.nextLine();
            System.out.println("Введіть число.");
        }
    }

    protected String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
