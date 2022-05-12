package DES;

import static DES.BitArray.*;
import static DES.Des.*;
import static DES.Funktionen.*;
import static DES.RoundKeyGen.*;
import static DES.FFunktion.*;

public class Schnittstelle {
    public static BitArray hexStringToBitArray(String string) {
        if (string == null) throw new IllegalArgumentException("String darf nicht null sein");
        String tmp = string.replaceAll(" ", "");
        if (tmp.length() % 2 != 0)
            throw new IllegalArgumentException("Der String muss eine gerade Anzahl an Zeichen haben, da sich sonst keine ganzen Byte ergeben");
        return new BitArray(hexString2arr(string));
    }

    public static BitArray binStringToBitArray(String string) {
        if (string == null) throw new IllegalArgumentException("String darf nicht null sein");
        String tmp = string.replaceAll(" ", "");
        if (tmp.length() % 8 != 0)
            throw new IllegalArgumentException("Der String muss eine durch 8 Teilbare Anzahl an 0/1 beinhalten");
        return new BitArray(binString2arr(string));
    }

    public static BitArray stringToBitArray(String string, int basis) {
        if (basis == 2) return binStringToBitArray(string);
        else if (basis == 16) return hexStringToBitArray(string);
        else throw new IllegalArgumentException("Nur Basis 2 und 16 sind zulässig!");
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

    public static BitArray encryptBlock(String hexString, String hexKey) {
        return encryptBlock(hexStringToBitArray(hexString), hexStringToBitArray(hexKey));
    }

    public static BitArray encryptBlock(String input, String key, int stringBasis) {
        return encryptBlock(stringToBitArray(input, stringBasis), stringToBitArray(key, stringBasis));
    }

    public static BitArray encryptBlock(BitArray input, BitArray key) {
        if (input == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (input.length() != 64 || key.length() != 64)
            throw new IllegalArgumentException("Key und Text müssen 64 Bit lang sein");
        return new BitArray(desBlock(input.getArray(), key.getArray(), false));
    }

    public static BitArray decryptBlock(String hexString, String hexKey) {
        return decryptBlock(hexStringToBitArray(hexString), hexStringToBitArray(hexKey));
    }

    public static BitArray decryptBlock(String input, String key, int stringBasis) {
        return decryptBlock(stringToBitArray(input, stringBasis), stringToBitArray(key, stringBasis));
    }

    public static BitArray decryptBlock(BitArray input, BitArray key) {
        if (input == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (input.length() != 64 || key.length() != 64)
            throw new IllegalArgumentException("Key und Text müssen 64 Bit lang sein");
        return new BitArray(desBlock(input.getArray(), key.getArray(), true));
    }

    public static Des getDesIteratorWithoutIP(String hexString, String hexKey, boolean entschluesseln) {
        return getDesIteratorWithoutIP(hexStringToBitArray(hexString), hexStringToBitArray(hexKey), entschluesseln);
    }

    public static Des getDesIteratorWithoutIP(String text, String key, int stringBasis, boolean entschluesseln) {
        return getDesIteratorWithoutIP(stringToBitArray(text, stringBasis), stringToBitArray(key, stringBasis), entschluesseln);
    }

    public static Des getDesIteratorWithoutIP(BitArray text, BitArray key, boolean entschluesseln) {
        if (text == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (text.length() != 64 || key.length() != 64)
            throw new IllegalArgumentException("Key und Text müssen 64 Bit lang sein");
        return new Des(text.getArray(), key.getArray(), entschluesseln);
    }

    public static Des getDesIteratorWithIP(String hexString, String hexKey, boolean entschluesseln) {
        return getDesIteratorWithIP(hexStringToBitArray(hexString), hexStringToBitArray(hexKey), entschluesseln);
    }

    public static Des getDesIteratorWithIP(String text, String key, int stringBasis, boolean entschluesseln) {
        return getDesIteratorWithIP(stringToBitArray(text, stringBasis), stringToBitArray(key, stringBasis), entschluesseln);
    }

    public static Des getDesIteratorWithIP(BitArray text, BitArray key, boolean entschluesseln) {
        if (text == null || key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (text.length() != 64 || key.length() != 64)
            throw new IllegalArgumentException("Key und Text müssen 64 Bit lang sein");
        return new Des(doIP(text).getArray(), key.getArray(), entschluesseln);
    }

    public static RoundKeyGen getRoundKeyGenerator(String hexKey, boolean entschluesseln) {
        return getRoundKeyGenerator(hexStringToBitArray(hexKey), entschluesseln);
    }

    public static RoundKeyGen getRoundKeyGenerator(String key, int stringBasis, boolean entschluesseln) {
        return getRoundKeyGenerator(stringToBitArray(key, stringBasis), entschluesseln);
    }

    public static RoundKeyGen getRoundKeyGenerator(BitArray key, boolean entschluesseln) {
        if (key == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (key.length() != 64)
            throw new IllegalArgumentException("Key muss 64 Bit lang sein");
        return new RoundKeyGen(key.getArray(), entschluesseln);
    }

    public static BitArray doIP(String hexString) {
        return doIP(hexStringToBitArray(hexString));
    }

    public static BitArray doIP(String input, int stringBasis) {
        return doIP(stringToBitArray(input, stringBasis));
    }

    public static BitArray doIP(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (input.length() != 64)
            throw new IllegalArgumentException("Key und Text müssen 32 Bit lang sein");
        return new BitArray(ip(input.getArray()));
    }

    public static BitArray doIPrev(String hexString) {
        return doIPrev(hexStringToBitArray(hexString));
    }

    public static BitArray doIPrev(String input, int stringBasis) {
        return doIPrev(stringToBitArray(input, stringBasis));
    }

    public static BitArray doIPrev(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (input.length() != 64)
            throw new IllegalArgumentException("Input muss 64 Bit lang sein");
        return new BitArray(ip_rev(input.getArray()));
    }

    public static BitArray doPC1(String hexString) {
        return doPC1(hexStringToBitArray(hexString));
    }

    public static BitArray doPC1(String input, int stringBasis) {
        return doPC1(stringToBitArray(input, stringBasis));
    }

    public static BitArray doPC1(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (input.length() != 64)
            throw new IllegalArgumentException("Input muss 64 Bit lang sein");
        return new BitArray(pc1(input.getArray()));
    }

    public static BitArray doPC2(String hexString) {
        return doPC2(hexStringToBitArray(hexString));
    }

    public static BitArray doPC2(String input, int stringBasis) {
        return doPC2(stringToBitArray(input, stringBasis));
    }

    public static BitArray doPC2(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (input.length() != 56)
            throw new IllegalArgumentException("Input muss 56 Bit lang sein");
        return new BitArray(pc2(input.getArray()));
    }

    public static BitArray doE(String hexString) {
        return doE(hexStringToBitArray(hexString));
    }

    public static BitArray doE(String input, int stringBasis) {
        return doE(stringToBitArray(input, stringBasis));
    }

    public static BitArray doE(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (input.length() != 32)
            throw new IllegalArgumentException("Input muss 32 Bit lang sein");
        return new BitArray(e(input.getArray()));
    }

    public static BitArray doS(String hexString) {
        return doS(hexStringToBitArray(hexString));
    }

    public static BitArray doS(String input, int stringBasis) {
        return doS(stringToBitArray(input, stringBasis));
    }

    public static BitArray doS(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (input.length() != 48)
            throw new IllegalArgumentException("Input muss 48 Bit lang sein");
        return new BitArray(s(input.getArray()));
    }

    public static BitArray doP(String hexString) {
        return doP(hexStringToBitArray(hexString));
    }

    public static BitArray doP(String input, int stringBasis) {
        return doP(stringToBitArray(input, stringBasis));
    }

    public static BitArray doP(BitArray input) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (input.length() != 32)
            throw new IllegalArgumentException("Input muss 32 Bit lang sein");
        return new BitArray(p(input.getArray()));
    }

    public static BitArray fFunction(String hexString, String hexRoundKey) {
        return fFunction(hexStringToBitArray(hexString), hexStringToBitArray(hexRoundKey));
    }

    public static BitArray fFunction(String text, String roundKey, int stringBasis) {
        return fFunction(stringToBitArray(text, stringBasis), stringToBitArray(roundKey, stringBasis));
    }

    public static BitArray fFunction(BitArray text, BitArray roundKey) {
        if (text == null || roundKey == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (text.length() != 64 && roundKey.length() != 48)
            throw new IllegalArgumentException("Input muss 64 Bit und roundKey 48 Bit lang sein");
        return new BitArray(f(text.getRightHalf().getArray(), roundKey.getArray()));
    }

    public static BitArray fFunctionHalf(String hexRightHalf, String hexRoundKey) {
        return fFunctionHalf(hexStringToBitArray(hexRightHalf), hexStringToBitArray(hexRoundKey));
    }

    public static BitArray fFunctionHalf(String rightHalf, String roundKey, int stringBasis) {
        return fFunctionHalf(stringToBitArray(rightHalf, stringBasis), stringToBitArray(roundKey, stringBasis));
    }

    public static BitArray fFunctionHalf(BitArray rightHalf, BitArray roundKey) {
        if (rightHalf == null || roundKey == null)
            throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (rightHalf.length() != 32 && roundKey.length() != 48)
            throw new IllegalArgumentException("rightHalf muss 32 Bit und roundKey 48 Bit lang sein");
        return new BitArray(f(rightHalf.getArray(), roundKey.getArray()));
    }

    public static BitArray doXor(String hexString1, String hexString2) {
        return doXor(hexStringToBitArray(hexString1), hexStringToBitArray(hexString2));
    }

    public static BitArray doXor(String text1, String text2, int stringBasis) {
        return doXor(stringToBitArray(text1, stringBasis), stringToBitArray(text2, stringBasis));
    }

    public static BitArray doXor(BitArray text1, BitArray text2) {
        if (text1 == null || text2 == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (text1.length() != text2.length())
            throw new IllegalArgumentException("Beide Arrays müssen gleich lang sein");
        return new BitArray(xor(text1.getArray(), text2.getArray()));
    }

    public static BitArray doShift(String hexString, int round, boolean rechts) {
        return doShift(hexStringToBitArray(hexString), round, rechts);
    }

    public static BitArray doShift(String input, int round, int stringBasis, boolean rechts) {
        return doShift(stringToBitArray(input, stringBasis), round, rechts);
    }

    public static BitArray doShift(BitArray input, int round, boolean rechts) {
        if (input == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        if (round < 1 || round > 16) throw new IllegalArgumentException("Round muss zwischen 1 <= round <= 16 liegen");
        if (input.length() != 56)
            throw new IllegalArgumentException("Input muss 56 Bit lang sein");
        return new BitArray(shift(input.getArray(), round, rechts));
    }

    public static BitArray doMergeBitArrays(BitArray anfang, BitArray ende) {
        if (anfang == null || ende == null) throw new IllegalArgumentException("Parameter dürfen nicht null sein");
        return mergeArrays(anfang, ende);
    }
}
