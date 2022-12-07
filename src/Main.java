/* Консольное приложение “Калькулятор” для работы с арабскими и римскими числами.
Приложение должно читать из консоли введенные пользователем строки, числа,
арифметические операции проводимые между ними и выводить в консоль результат их выполнения.

Реализован класс Main с методом public static String calc(String input).
Метод должен принимать строку с арифметическим выражением между двумя числами и возвращать строку с результатом их выполнения.*/

import java.util.*;

public class Main {

    public static Main objectMain = new Main();
    public static boolean foundRim = false;

    public static void main(String[] args) throws Exception {

        Scanner in = new Scanner(System.in);

        while (true) {
            // Читаем введеную строку
            objectMain.print("Введите выражение: ");
            String text = in.nextLine();
            //in.close();

            // Проверка на наличие римских цифр
            String[] check = text.split("");
            for (String s : check) {
                if (s.equals("I") | s.equals("V") | s.equals("X") | s.equals("L") | s.equals("C") | s.equals("D") | s.equals("M")) {
                    foundRim = true;
                    break;
                }
            }

            if (check.length < 3) throw new Exception("Строка не является математической операцией");

            try {
                // Если в строке имеются Арабские числа -> calc() если Римские -> convertRim()
                if (!foundRim) {
                    objectMain.print(calc(text) + "\n");

                } else {
                    String convert = convertToArab(text);
                    objectMain.print(calc(convert) + "\n");
                }
            } catch (Exception e) {
                System.out.print("Что-то пошло не так, попробуйте ещё раз\n");
            }
        }
    }

    public static String calc(String input) {

        // Проверка на исключение с разными системами исчесления
        if (Objects.equals(input, "Используются одновременно разные системы счисления")) {
            return input;
        }

        String[] line = input.split(" ");

        int a = Integer.parseInt(line[0]), b = Integer.parseInt(line[2]);
        String sim = line[1], result = null;

        try {
            // Проверка на то, что значений в выражении всего два
            if (!Objects.equals(line[3], null)) {
                return "Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)";
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        // Ищем символ и выполняет выражение
        switch (sim) {
            case ("+") -> result = Integer.toString(a + b);
            case ("-") -> result = Integer.toString(a - b);
            case ("*") -> result = Integer.toString(a * b);
            case ("/") -> result = Integer.toString(a / b);
        }

        // Если символы были Римские - конвектируем
        if (foundRim) {
            return convertToRim(Objects.requireNonNull(result));
        }
        return result;
    }

    static String convertToArab(String input) {

        // Замена символа на число
        String[] lineStr = input.split("");
        ArrayList<String> numbs = new ArrayList<>(Arrays.asList(lineStr));
        int sizeNumbs = numbs.size();

        // Проверка на наличие только римских цифр
        for (String s : numbs) {
            if (s.equals("1") | s.equals("2") | s.equals("3") | s.equals("4") | s.equals("5") | s.equals("6") | s.equals("7") | s.equals("8") | s.equals("9") | s.equals("0")) {
                return "Используются одновременно разные системы счисления";
            }
        }

        // Замена двух вычитаемых цифр (4, 9 ...)
        for (int i = 0; i < sizeNumbs - 1; i++) {
            switch (numbs.get(i) + numbs.get(i + 1)) {
                case ("IV") -> {
                    numbs.set(i, "4");
                    numbs.remove(i + 1);
                    sizeNumbs--;
                }
                case ("IX") -> {
                    numbs.set(i, "9");
                    numbs.remove(i + 1);
                    sizeNumbs--;
                }
                case ("XL") -> {
                    numbs.set(i, "40");
                    numbs.remove(i + 1);
                    sizeNumbs--;
                }
                case ("XC") -> {
                    numbs.set(i, "90");
                    numbs.remove(i + 1);
                    sizeNumbs--;
                }
                case ("CD") -> {
                    numbs.set(i, "400");
                    numbs.remove(i + 1);
                    sizeNumbs--;
                }
                case ("CM") -> {
                    numbs.set(i, "900");
                    numbs.remove(i + 1);
                    sizeNumbs--;
                }
            }
        }

        // Замена одиночных чисел
        for (int i = 0; i < sizeNumbs; i++) {
            switch (numbs.get(i)) {
                case ("I") -> numbs.set(i, "1");
                case ("V") -> numbs.set(i, "5");
                case ("X") -> numbs.set(i, "10");
                case ("L") -> numbs.set(i, "50");
                case ("C") -> numbs.set(i, "100");
                case ("D") -> numbs.set(i, "500");
                case ("M") -> numbs.set(i, "1000");
            }
        }
        // Очищаем пустые значения
        numbs.removeAll(Arrays.asList("", " ", null));
        int oneIndex = 0, twoIndex = 1;

        // Складываем значения в листе
        for (int i = 0; i <= numbs.size(); i++) {
            try {
                int firstNum = Integer.parseInt(numbs.get(oneIndex));
                int secondNum = Integer.parseInt(numbs.get(twoIndex));
                numbs.set(oneIndex, String.valueOf(firstNum + secondNum));
                numbs.remove(twoIndex);
            } catch (NumberFormatException e) {
                //System.out.print(String.valueOf(e));
                oneIndex = 2;
                twoIndex = 3;
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        // Очищаем строку
        String result = String.valueOf(numbs);
        StringBuilder strBuild = new StringBuilder(result);
        strBuild.delete(0, 1);
        strBuild.delete(strBuild.length() - 1, strBuild.length());

        result = strBuild.toString();
        result = result.replaceAll(",", "");
        result = result.replaceAll(" {3}", " ");
        result = result.replaceAll(" {2}", " ");

        //System.out.print(result);
        return result;
    }

    static String convertToRim(String input) {

        // Проверка на ноль
        if (Objects.equals(input, "0") & input.length() == 1) {
            return "В системе римских цифр отсутствует ноль";
        }

        // Проверка на отрицитальные числа
        if (Integer.parseInt(input) < 0) {
            return "В римской системе нет отрицательных чисел";
        }

        String[] kickNumb = input.split("");
        ArrayList<String> numbs = new ArrayList<>(Arrays.asList(kickNumb));
        String zero = "0";

        // Инвентируем массив чтобы разложить число
        Collections.reverse(numbs);
        int countZero = 0, numbsSize = numbs.size();
        boolean checkDel = false;
        for (int i = 0; i < numbsSize; i++) {
            if (!Objects.equals(numbs.get(i), "0")) {
                if (!checkDel) {
                    numbs.set(i, numbs.get(i) + zero.repeat(i));
                } else {
                    numbs.set(i, numbs.get(i) + zero.repeat(countZero));
                }
            } else {
                numbs.remove(i);
                checkDel = true;
                numbsSize--;
                countZero++;
                i--;
            }
        }

        // Инвентируем массив
        Collections.reverse(numbs);

        StringBuilder result = new StringBuilder();
        int sizeNumbs = numbs.size();

        // Замена двух вычитаемых цифр (4, 9 ...)
        for (int i = 0; i < sizeNumbs; i++) {
            switch (numbs.get(i)) {
                case ("4") -> numbs.set(i, "IV");
                case ("9") -> numbs.set(i, "IX");
                case ("40") -> numbs.set(i, "XL");
                case ("90") -> numbs.set(i, "XC");
                case ("400") -> numbs.set(i, "CD");
                case ("900") -> numbs.set(i, "CM");
            }
        }

        // Заменяем цифры на Римские символы
        for (int i = 0; i < numbs.size(); i++) {
            switch (numbs.get(i)) {
                case ("1") -> numbs.set(i, "I");
                case ("2") -> numbs.set(i, "II");
                case ("3") -> numbs.set(i, "III");
                case ("4") -> numbs.set(i, "IV");
                case ("5") -> numbs.set(i, "V");
                case ("6") -> numbs.set(i, "VI");
                case ("7") -> numbs.set(i, "VII");
                case ("8") -> numbs.set(i, "VIII");
                case ("9") -> numbs.set(i, "IX");
                case ("10") -> numbs.set(i, "X");
                case ("50") -> numbs.set(i, "L");
                case ("100") -> numbs.set(i, "C");
                case ("500") -> numbs.set(i, "D");
                case ("1000") -> numbs.set(i, "M");
            }
            result.append(numbs.get(i));
        }
        return result.toString();
    }

    // Привык к Python, поэтому метод print привычнее чем System.out.print
    void print(String s) {
        System.out.print(s);
    }
}