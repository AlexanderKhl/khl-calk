import java.util.TreeMap;

public class RomArConverter {
    static String[] romArray = new String[]{"0", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    TreeMap<Character,Integer> romKeysMap = new TreeMap<>();
    TreeMap<Integer,String> arKeysMap = new TreeMap<>();

    public static boolean isRoman(String val) {
        for(int i=0; i<romArray.length;i++) {
            if (val.equals(romArray[i])) {
                return true;
            }
        }
        return false;
    }

    // Инициализация карт в конструкторе
    public RomArConverter() {
        // Так как X*X=C (максимально возможное число),
        // то бОльших чисел не будет, поэтому хранить их в карте romKeysMap не будем
        romKeysMap.put('I', 1);
        romKeysMap.put('V', 5);
        romKeysMap.put('X', 10);
        romKeysMap.put('L', 50);
        romKeysMap.put('C', 100);

        // Так как X*X=C (максимально возможное число),
        // то бОльших чисел не будет, поэтому хранить их в карте arKeysMap не будем
        arKeysMap.put(100, "C");
        arKeysMap.put(90, "XC");
        arKeysMap.put(50, "L");
        arKeysMap.put(40, "XL");
        arKeysMap.put(10, "X");
        arKeysMap.put(9, "IX");
        arKeysMap.put(5, "V");
        arKeysMap.put(4, "IV");
        arKeysMap.put(1, "I");
    }

    public int romToAr(String str) {
        // Для вычисления арабского числа из римского обход римского числа
        // начинаем справа налево по массиву символов и произволим сравнение
        // арабского эквивалента римского символа-числа.
        // Если слева меньше, то вычитаем, иначе прибавляем к результату.
        int lastSymbolIndex = str.length()-1;
        char[] charArray = str.toCharArray();
        int currentArbianNumber;
        int arabianReturn = romKeysMap.get(charArray[lastSymbolIndex]);
        for(int i=lastSymbolIndex-1;i>=0;i--) {
            // Получаем арабское представление числа, соответствующего меньшему на 1 индексу в массиве
            // римских символов-чисел
            currentArbianNumber = romKeysMap.get(charArray[i]);
            if(currentArbianNumber < romKeysMap.get(charArray[i+1])) {
                arabianReturn -= currentArbianNumber;
            } else {
                arabianReturn += currentArbianNumber;
            }
        }
        return arabianReturn;
    }

    public String arToRom(int number) {
        String romanReturn = "";
        int arabianKey;
        do {
            // Получить арабский ключ римского числа меньший или равный текущему арабскому числу number
            arabianKey = arKeysMap.floorKey(number);
            romanReturn += arKeysMap.get(arabianKey);
            // Уменьшаем наше арабское число на полученный арабский ключ римского числа для следующей итерации
            number -= arabianKey;
        } while (number != 0);
        return romanReturn;
    }
}
