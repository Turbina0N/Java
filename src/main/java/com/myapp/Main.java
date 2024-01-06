package com.myapp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nВыберите операцию:");
            System.out.println("1 - Добавить пару ключ-значение");
            System.out.println("2 - Удалить элемент по ключу");
            System.out.println("3 - Проверить наличие ключа");
            System.out.println("4 - Получить значение по ключу");
            System.out.println("5 - Вывести размер карты");
            System.out.println("6 - Очистить карту");
            System.out.println("7 - Выйти из программы");
            System.out.print("Ваш выбор: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Введите ключ: ");
                    String keyToAdd = scanner.nextLine();
                    System.out.print("Введите значение: ");
                    String valueToAdd = scanner.nextLine();
                    map.add(keyToAdd, valueToAdd);
                    System.out.println("Пара добавлена.");
                    break;
                case "2":
                    if (map.size() == 0) {
                        System.out.println("В карте нет элементов для удаления.");
                    } else {
                        System.out.print("Введите ключ для удаления: ");
                        String keyToRemove = scanner.nextLine();
                        if (map.contains(keyToRemove)) {
                            map.remove(keyToRemove);
                            System.out.println("Элемент удален.");
                        } else {
                            System.out.println("Элемент с таким ключом не найден.");
                        }
                    }
                    break;
                case "3":
                    if (map.size() == 0) {
                        System.out.println("Отсутствуют элементы для проверки.");
                    } else
                    {
                        System.out.print("Введите ключ для проверки: ");
                        String keyToCheck = scanner.nextLine();
                        System.out.println("Содержит '" + keyToCheck + "': " + map.contains(keyToCheck));
                        break;
                    }
                case "4":
                    System.out.print("Введите ключ для получения значения: ");
                    String keyToGet = scanner.nextLine();
                    if (map.contains(keyToGet)) {
                        System.out.println("Значение '" + keyToGet + "': " + map.get(keyToGet));
                    } else {
                        System.out.println("Значение с таким ключом не найдено.");
                    }
                    break;
                case "5":
                    System.out.println("Размер карты: " + map.size());
                    break;
                case "6":
                    map.clear();
                    System.out.println("Карта очищена.");
                    break;
                case "7":
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }
}
