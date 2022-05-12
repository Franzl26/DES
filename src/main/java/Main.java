import DES.*;

import static DES.Schnittstelle.*;

public class Main {
    public static void main(String[] args) {
        beispiele();
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

        // außerdem gehen noch ein paar andere tolle Dinge für die es keine Beispiele gibt
    }
}
