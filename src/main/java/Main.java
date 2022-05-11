import DES.*;

import static DES.Schnittstelle.*;

public class Main {
    public static void main(String[] args) {
        test();
        // kryptoBlatt3();
        des();

        String input = "C:/Users/f-luc/Downloads/start.txt";
        String output = "C:/Users/f-luc/Downloads/mitte.txt";
        String output2 = "C:/Users/f-luc/Downloads/ende.txt";

        long start = System.currentTimeMillis();
        encryptFile(input, output, "0123456789abcdef");
        long mitte = System.currentTimeMillis();
        decryptFile(output, output2, "0123456789abcdef");
        long ende = System.currentTimeMillis();
        System.out.println("Verschlüsseln: " + (mitte - start));
        System.out.println("Entschlüsseln: " + (ende - mitte));
    }

    public static void des() {
        String key = "0123456789abcdef";
        String message = "E3E10C711C200024";

        // Block
        System.out.println("\nBlock: " + encryptBlock(message, key));

        // Des Iterator
        Des desIt = getDesIteratorWithoutIP(message, key, false);
        desIt.doIP();
        for (int i = 0; i < 16; i++) desIt.nextRound();
        desIt.swapHalfs();
        desIt.doIPrev();
        System.out.println("With desIt: " + desIt.getCurrentText());

        // Alles selbstständig
        BitArray k = hexStringToBitArray(key);
        BitArray m = hexStringToBitArray(message);
        m = doIP(m);
        k = doPC1(k);
        for (int i = 1; i <= 16; i++) {
            k = doShift(k, i, false);
            BitArray newL = doXor(m.getLeftHalf(), doP(doS(doXor(doE(m.getRightHalf()), doPC2(k)))));
            m = doMergeBitArrays(m.getRightHalf(), newL);
        }
        m.swapHalfs();
        m = doIPrev(m);
        System.out.println("Selbstständig: " + m);
    }

    public static void kryptoBlatt3() {
        // Aufgabe 3
        System.out.println("Aufgabe 3");
        RoundKeyGen keygen = getRoundKeyGenerator("264a57799bcbdf1f", false);
        for (int i = 0; i < 16; i++) {
            System.out.println("roundKey " + i + ": " + keygen.nextKey());
        }

        // Aufgabe 4
        System.out.println("\nAufgabe 4");
        RoundKeyGen k1 = getRoundKeyGenerator("0101010101010101", false);
        RoundKeyGen k2 = getRoundKeyGenerator("FEFEFEFEFEFEFEFE", false);
        RoundKeyGen k3 = getRoundKeyGenerator("E0E0E0E0f1f1f1f1", false);
        RoundKeyGen k4 = getRoundKeyGenerator("1f1f1f1f0e0e0e0e", false);
        for (int i = 0; i < 16; i++) {
            System.out.println(i + ": " + k1.nextKey() + " : " + k2.nextKey() + " : " + k3.nextKey() + " : " + k4.nextKey());
        }

        // Aufgabe 5
        System.out.println("\nAufgabe 5");
        RoundKeyGen k = getRoundKeyGenerator("133457799BBcdff1", false);
        BitArray half = fFunctionHalf(hexStringToBitArray("f0aaf0aa"), k.nextKey());
        System.out.println("Linke Hälfte: " + doXor(half, hexStringToBitArray("cf4b6544")));
        Des d = getDesIteratorWithIP("1123456789EBCDEF", "133457799bbcdff1", false);
        BitArray b = d.nextRound();
        System.out.println("Test: " + b + " = " + bitArrayToBinString(b, true));
        System.out.println("IP-1(m): " + doIPrev(hexStringToBitArray("ec01ccfff0aaf0aa")));


        // Aufgabe 6
        System.out.println("\nAufgabe 6");
        Des d2 = getDesIteratorWithIP("58554959484d424c", "123556799abddef1", false);
        BitArray b2 = d2.nextRound();
        System.out.println(b2 + " = " + bitArrayToBinString(b2, true));
    }

    public static void test() {
        BitArray key = hexStringToBitArray("0123456789abcdef");
        System.out.println("key    : " + key.toString(2, true) + " = " + key);
        // BitArray m = hexString2arr("1555c98c01ddff0c");
        BitArray m = hexStringToBitArray("E3E10C711C200024");
        System.out.println("Message: " + bitArrayToBinString(m, true) + " = " + m);
        BitArray dc = encryptBlock(m, key);
        System.out.println("Cipher : " + bitArrayToBinString(dc, true) + " = " + dc);
        BitArray ec = decryptBlock(dc, key);
        System.out.println("Message: " + bitArrayToBinString(ec, true) + " = " + ec);

        /*String input = "C:/Users/f-luc/Desktop/JavaProjects/DES/test.txt";
        String output = "C:/Users/f-luc/Desktop/JavaProjects/DES/versch.txt";
        String output2 = "C:/Users/f-luc/Desktop/JavaProjects/DES/entsch.txt";*/
        String input = "C:/Users/f-luc/Downloads/test.txt";
        String output = "C:/Users/f-luc/Downloads/testo.txt";
        String output2 = "C:/Users/f-luc/Downloads/testoo.txt";

        encryptFile(input, output, key);
        decryptFile(output, output2, key);

        Des des = getDesIteratorWithoutIP(doIP(m), key, false);
        RoundKeyGen keyGen = getRoundKeyGenerator(key, false);
        BitArray d = null;
        for (int i = 0; i < 16; i++) {
            d = des.nextRound();
            BitArray k = keyGen.nextKey();
            System.out.println("key: " + k + ", round: " + d);
        }
        d.swapHalfs();
        System.out.println(doIPrev(d));
    }
}
