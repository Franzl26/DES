package DES;

import java.util.Arrays;

class Funktionen {
    static char[] hexString2arr(String string) {
        string = string.replaceAll(" ", "");
        char[] array = new char[4 * string.length()];
        char[] ch = string.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            int zahl = Integer.parseInt(String.valueOf(ch[i]), 16);
            array[i * 4 + 3] = (char) (zahl % 2);
            zahl /= 2;
            array[i * 4 + 2] = (char) (zahl % 2);
            zahl /= 2;
            array[i * 4 + 1] = (char) (zahl % 2);
            zahl /= 2;
            array[i * 4] = (char) (zahl % 2);
        }
        return array;
    }

    static char[] binString2arr(String string) {
        string = string.replaceAll(" ", "");
        char[] array = new char[string.length()];
        char[] ch = string.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == '0') array[i] = 0;
            else if (ch[i] == '1') array[i] = 1;
            else throw new IllegalArgumentException("Der String enthält nicht binäre Zeichen");
        }

        return array;
    }

    static char[] byteArr2arr(byte[] input) {
        char[] array = new char[8 * input.length];
        for (int i = 0; i < input.length; i++) {
            int zahl = Byte.toUnsignedInt(input[i]);
            int index = i * 8;
            for (int j = index + 7; j >= index; j--) {
                array[j] = (char) (zahl % 2);
                zahl /= 2;
            }
        }
        return array;
    }

    static String arr2hexString(char[] array) {
        return arr2hexString(array, false);
    }

    static String arr2hexString(char[] array, boolean byteTrenner) {
        if (array.length % 4 != 0) {
            char[] tmp = array;
            array = new char[array.length + (4 - (array.length) % 4)];
            System.arraycopy(tmp, 0, array, array.length - tmp.length, tmp.length);
        }
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < array.length; i += 4) {
            int zahl = array[i] * 8 + array[i + 1] * 4 + array[i + 2] * 2 + array[i + 3];
            string.append(Integer.toHexString(zahl));
            if ((i + 4) % 8 == 0 && byteTrenner) string.append(" ");
        }
        return string.toString().toUpperCase();
    }

    static String arr2binString(char[] array) {
        return arr2binString(array, false);
    }

    static String arr2binString(char[] array, boolean byteTrenner) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            string.append(Integer.toHexString(array[i]));
            if ((i + 1) % 8 == 0 && byteTrenner) string.append(" ");
        }
        return string.toString();
    }

    static byte[] arr2byteArr(char[] array) {
        byte[] ergebnis = new byte[8];
        for (int i = 0; i < 8; i++) {
            int index = i * 8;
            int zahl = 128 * array[index] + 64 * array[index + 1] + 32 * array[index + 2] + 16 * array[index + 3]
                    + 8 * array[index + 4] + 4 * array[index + 5] + 2 * array[index + 6] + array[index + 7];
            ergebnis[i] = (byte) zahl;
        }

        return ergebnis;
    }

    static char[] xor(char[] eins, char[] zwei) {
        if (eins.length != zwei.length)
            throw new IllegalArgumentException("Die Inputs für XOR müssen gleich lang sein");
        char[] ergebnis = new char[eins.length];
        for (int i = 0; i < ergebnis.length; i++) {
            ergebnis[i] = (char) ((eins[i] + zwei[i]) % 2);
        }
        return ergebnis;
    }
}
