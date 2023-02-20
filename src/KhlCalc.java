import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Простой калькулятор (тестовое задание).
 * @author Александр Хлопцев
 * @version 1.0
 */

public class KhlCalc {

    public static void main(String[] args) throws ExpressionException {
        Scanner scanner = new Scanner(System.in);
        /* Для того, чтобы не плодить if-ы по проверкам на:
        - расположение в интервале от 1 до 10,
        - данные в одной строке,
        - одновременность арабских или римских чисел;
        - для удобства понимания пользователем его ввода выведем информацию об ограничениях на ввод
        и проверим основные моменты регулярным выражением с выбросом
        ExpressionException("Выражение не соответствует условиям") */
        System.out.println("""
                Калькулятор
                1. Операции с простыми числами от 1 до 10 вида:\s
                a + b, a - b, a * b, a / b\s
                2. Данные передаются в одну строку.
                3. Может работать или с арабскими, или с римскими числами.
                Введите выражение:""");
        String expression = "";
        try {
            expression = scanner.nextLine();
            // Закрываем поток считывания, освобождая ресурс
            scanner.close();
        } catch (RuntimeException e) {
            // Закрываем поток считывания, освобождая ресурс
            scanner.close();
            System.out.println("Ошибка при считывании");
        }

        Pattern pattern = Pattern.compile("^" +
                "(" + "\s*" +
                "((X{0,1})?|(IX|IV|V?I{0,3})?)?" +  //"(?<Roman>(((X{0,1})?|(IX|IV|V?I{0,3})?)?))" +
                "\s*[\\+-\\/\\*]\s*" +
                "((X{0,1})?|(IX|IV|V?I{0,3})?)?" +  //"\\k<Roman>" + // не всегда работают именованные блоки
                "\s*" + ")" +
                "|" +
                "(" + "\s*" +
                "((10)?|(\\d{1})?)?" +   //"(?<Arab>(((10)?|(\\d{1})?)?))" +
                "\s*[\\+-\\/\\*]\s*" +
                "((10)?|(\\d{1})?)?" + //"\\k<Arab>" + // не всегда работают именованные блоки
                "\s*" + ")" +
                "$");
        // Обрезаем все пробельные символы [ \t\n\x0B\f\r] до и после выражения
        Matcher matcher = pattern.matcher(expression.strip());
        // Если введённое выражение соответствует шаблону, то вычесляем его, иначе выбрасываем исключение.
        if (matcher.matches()) {
            System.out.println(KhlCalc.calc(expression));
        } else {
            throw new ExpressionException("Выражение не соответствует условиям");
        }
    }

    // Метод принимает строку с арифметическим выражением между двумя числами
    // и возвращает строку с результатом их выполнения.
    public static String calc(String input) {
        // В результирующую строку добавляем сообщение об ошибке.
        // Это сообщение затрётся данными.
        String output = "Ошибка при вычислении выражения";
        String[] operations = {"+", "-", "/", "*"};
        String[] regexOperations = {"\\+", "-", "/", "\\*"};

        // Мы проверили на наличие знака операции. Он точно есть, поэтому индексу операции присваиваем 0
        int operationIndex = 0;
        for (int i = 0; i < operations.length; i++) {
            if (input.contains(operations[i])) {
                operationIndex = i;
                break;
            }
        }
        // Получаем массив из 2- элементов со строками-операндами
        String[] data = input.split(regexOperations[operationIndex]);
        // Убираем возможные пробельные символы
        data[0] = data[0].strip();
        data[1] = data[1].strip();
        // Так как проверку чисел на одиноковость формата мы выполнини в регулярном выражении,
        // то проверяем на "римскость" и "арабскость".
        int a, b, result = 0;
        boolean isRoman = RomArConverter.isRoman(data[0]);
        RomArConverter converter = new RomArConverter();
        if (isRoman) {
            a = converter.romToAr(data[0]);
            b = converter.romToAr(data[1]);
        } else {
            a = Integer.parseInt(data[0]);
            b = Integer.parseInt(data[1]);
        }
        switch (operations[operationIndex]) {
            case "+" -> result = a + b;
            case "-" -> result = a - b;
            case "/" -> result = a / b;
            case "*" -> result = a * b;
        }
        if (isRoman) {
             output = converter.arToRom(result);
        } else {
            output = Integer.toString(result);
        }
        return output;
    }
}