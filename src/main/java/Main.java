import DES.*;

import static DES.Schnittstelle.*;

public class Main {
    public static void main(String[] args) {
        //beispiele();
        aufgabe5Krypto();
        //kryptoBlatt3();
    }

    public static void beispiele() {
        // Daten/Bit-String werden in BitArrays zwischengespeichert
        BitArray key;
        BitArray message;
        // Daten können entweder als Binär- oder als Hexadezimal-String eingegeben werden
        // dabei sind auch Leerzeichen zugelassen, bei Hex ist groß und klein erlaubt
        // nur Eingaben ganzer Bytes sind erlaubt, sonst kommt eine Fehlermeldung
        key = hexStringToBitArray("0123456789abcdef");
        message = binStringToBitArray("00000001 00100011 01000101 01100111 10001001 10101011 11001101 11101111");

        // der Typ BitArray wird automatisch in einen Hex-String umgewandelt
        // alternativ kann toString oder bitArrayToBinString/bitArrayToHexString aufgerufen werden
        // hierbei können auch für die bessere Lesbarkeit Byte-Trenner aktiviert werden
        System.out.println("key: " + key);
        System.out.println("key: " + key.toString(16, true));
        System.out.println("key: " + key.toString(2));
        System.out.println("key: " + bitArrayToHexString(key));
        System.out.println("key: " + bitArrayToBinString(key, true));

        // Inputs für die einzelnen Funktionen sind entweder BitArrays
        // oder Strings, optional mit Basis (default = 16)
        // sollten die Längen der Inputs nicht stimmen, wird eine entsprechende Exception geworfen (hoffentlich)
        BitArray cipher = encryptBlock(message, key);
        cipher = encryptBlock("0123456789abcdef", "0123456789abcdef");
        cipher = encryptBlock("00000001 00100011 01000101 01100111 10001001 10101011 11001101 11101111",
                "00000001 00100011 01000101 01100111 10001001 10101011 11001101 11101111", 2);
        System.out.println("cipher: " + cipher);

        // Außerdem können RoundKey- und DES-Iteratoren erzeugt werden
        // diese liefern dann bei jedem Aufruf den nächsten Rundenschlüssel/das Ergebnis der nächsten Runde
        // DES-Iterator gibt es mit und ohne direkt angewandte IP, diese kann nachträglich mit .doIP() durchgeführt werden
        Des desIt = getDesIteratorWithIP(message, key, false);
        Des desIt2 = getDesIteratorWithoutIP(message, key, false);
        RoundKeyGen keyGen = getRoundKeyGenerator(key, false);
        desIt2.doIP();
        for (int i = 1; i < 17; i++) {
            System.out.println("Runde " + i + " key: " + keyGen.nextKey()
                    + ", Rundenergebnis: " + desIt.nextRound());
        }
        // dann können noch die Hälften zurückgetauscht und IP^-1 durchgeführt werden und man erhält den cipher-text
        desIt.swapHalfs();
        desIt.doIPrev();
        System.out.println("cipher: " + desIt.getCurrentText());

        // außerdem können mit do... alle Schritte einzeln durchgeführt werden
        // dabei sind die Eingabelängen zu beachten, die aber ggf. eine passende Exception werfen sollten
        BitArray show = doE("12345678");
        show = doS("12345678abcd");
        System.out.println(doIPrev(message));

        // außerdem gehen noch ein paar andere tolle Dinge für die es keine Beispiele gibt
    }

    public static void aufgabe5Krypto() {
        BitArray key = hexStringToBitArray("133457799BBCDFF1");
        BitArray cipher = binStringToBitArray("11110000 10101010 11110000 10101010  11001111 01001011 01100101 0100 0100");
        BitArray cipherLeft = cipher.getLeftHalf();
        BitArray cipherRight = cipher.getRightHalf();

        key = doPC1(key);
        System.out.println("Key nach PC1  : " + key.toString(2, true));
        key = doShift(key, 1, false);
        System.out.println("Key nach shift: " + key.toString(2, true));
        key = doPC2(key);
        System.out.println("Key nach PC2  : " + key.toString(2, true));
        BitArray fFunc = doE(cipherLeft);
        System.out.println("nach expand   : " + fFunc.toString(2, true));
        fFunc = doXor(key, fFunc);
        System.out.println("nach xor      : " + fFunc.toString(2, true));
        fFunc = doS(fFunc);
        System.out.println("nach sBox     : " + fFunc.toString(2, true));
        fFunc = doP(fFunc);
        System.out.println("nach P        : " + fFunc.toString(2, true));
        fFunc = doXor(fFunc, cipherRight);
        System.out.println("nach xor      : " + fFunc.toString(2, true));
        BitArray ergebnis = doMergeBitArrays(fFunc, cipherLeft);
        System.out.println("nach merge    : " + ergebnis.toString(2, true));
        ergebnis = doIPrev(ergebnis);
        System.out.println("Klartext      : " + ergebnis.toString(2, true) + " = " + ergebnis);
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
}
