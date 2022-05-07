package DES;

import static DES.Des.*;
import static DES.Funktionen.*;
import static DES.RoundKeyGen.*;
import static DES.FFunktion.*;

public class Schnittstelle {
    public static BitArray hexStringToBitArray(String string) {
        if (string == null) throw new IllegalArgumentException("String darf nicht null sein");
        return new BitArray(hexString2arr(string));
    }

    public static BitArray binStringToBitArray(String string) {
        if (string == null) throw new IllegalArgumentException("String darf nicht null sein");
        return new BitArray(binString2arr(string));
    }

    public static String bitArrayToBinString(BitArray array) {
        return bitArrayToBinString(array, false);
    }

    public static String bitArrayToBinString(BitArray array, boolean byteTrenner) {
        if (array == null) throw new IllegalArgumentException("Array darf nicht null sein");
        return arr2binString(array.getArray(), byteTrenner);
    }

    public static String bitArrayToHexString(BitArray array) {
        if (array == null) throw new IllegalArgumentException("Array darf nicht null sein");
        return arr2hexString(array.getArray());
    }

    public static boolean encryptFile(String input, String output, String key) {
        if (input == null || output == null || key == null)
            throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return encryptFile(input, output, hexStringToBitArray(key));
    }

    public static boolean encryptFile(String input, String output, BitArray key) {
        if (input == null || output == null || key == null)
            throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        try {
            desFile(input, output, key.getArray(), false);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    public static boolean decryptFile(String input, String output, String key) {
        if (input == null || output == null || key == null)
            throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return decryptFile(input, output, hexStringToBitArray(key));
    }

    public static boolean decryptFile(String input, String output, BitArray key) {
        if (input == null || output == null || key == null)
            throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        try {
            desFile(input, output, key.getArray(), true);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    public static BitArray encryptBlock(String input, String key) {
        if (input == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return encryptBlock(hexStringToBitArray(input), hexStringToBitArray(key));
    }

    public static BitArray encryptBlock(BitArray input, BitArray key) {
        if (input == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(desBlock(input.getArray(), key.getArray(), false));
    }

    public static BitArray decryptBlock(String input, String key) {
        if (input == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return decryptBlock(hexStringToBitArray(input), hexStringToBitArray(key));
    }

    public static BitArray decryptBlock(BitArray input, BitArray key) {
        if (input == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(desBlock(input.getArray(), key.getArray(), true));
    }

    public static Des getDesIteratorWithoutIP(String text, String key, boolean entschluesseln) {
        if (text == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return getDesIteratorWithoutIP(hexStringToBitArray(text), hexStringToBitArray(key), entschluesseln);
    }

    public static Des getDesIteratorWithoutIP(BitArray text, BitArray key, boolean entschluesseln) {
        if (text == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new Des(text.getArray(), key.getArray(), entschluesseln);
    }

    public static Des getDesIteratorWithIP(String text, String key, boolean entschluesseln) {
        if (text == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return getDesIteratorWithIP(hexStringToBitArray(text), hexStringToBitArray(key), entschluesseln);
    }

    public static Des getDesIteratorWithIP(BitArray text, BitArray key, boolean entschluesseln) {
        if (text == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new Des(doIP(text).getArray(), key.getArray(), entschluesseln);
    }

    public static BitArray doIP(String input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return doIP(hexStringToBitArray(input));
    }

    public static BitArray doIP(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(ip(input.getArray()));
    }

    public static BitArray doIPrev(String input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return doIPrev(hexStringToBitArray(input));
    }

    public static BitArray doIPrev(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(ip_rev(input.getArray()));
    }

    public static RoundKeyGen getRoundKeyGenerator(String key, boolean entschluesseln) {
        if (key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return getRoundKeyGenerator(hexStringToBitArray(key), entschluesseln);
    }

    public static RoundKeyGen getRoundKeyGenerator(BitArray key, boolean entschluesseln) {
        if (key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new RoundKeyGen(key.getArray(), entschluesseln);
    }

    public static BitArray doPC1(String input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return doPC1(hexStringToBitArray(input));
    }

    public static BitArray doPC1(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(pc1(input.getArray()));
    }

    public static BitArray doPC2(String input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return doPC2(hexStringToBitArray(input));
    }

    public static BitArray doPC2(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(pc2(input.getArray()));
    }

    public static BitArray doE(String input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return doE(hexStringToBitArray(input));
    }

    public static BitArray doE(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(e(input.getArray()));
    }

    public static BitArray doP(String input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return doP(hexStringToBitArray(input));
    }

    public static BitArray doP(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(p(input.getArray()));
    }

    public static BitArray fFunction(BitArray text, BitArray roundKey) {
        if (text == null || roundKey == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(f(text.getRightHalf().getArray(), roundKey.getArray()));
    }

    public static BitArray fFunctionHalf(BitArray rightHalf, BitArray roundKey) {
        if (rightHalf == null || roundKey == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(f(rightHalf.getArray(), roundKey.getArray()));
    }

    public static BitArray doXor(String text1, String text2) {
        return doXor(hexStringToBitArray(text1), hexStringToBitArray(text2));
    }

    public static BitArray doXor(BitArray text1, BitArray text2) {
        if (text1 == null || text2 == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return new BitArray(xor(text1.getArray(), text2.getArray()));
    }
}
