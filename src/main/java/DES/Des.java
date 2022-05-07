package DES;

import java.io.*;

import static DES.FFunktion.f;
import static DES.Funktionen.*;

public class Des {
    private char[] text;
    private final RoundKeyGen keyGen;
    private int count = 1;

    Des(char[] text, char[] key, boolean entschluesseln) {
        if (text.length != 64) throw new IllegalArgumentException("Der Input-Text muss 64 Bit lang sein");
        this.text = text;
        keyGen = new RoundKeyGen(key, entschluesseln);
    }

    char[] desRunde(char[] text, char[] roundKey) {
        char[] leftHalf = new char[32];
        char[] rightHalf = new char[32];
        System.arraycopy(text, 0, leftHalf, 0, 32);
        System.arraycopy(text, 32, rightHalf, 0, 32);
        char[] ffunc = f(rightHalf, roundKey);
        char[] ergebnis = new char[64];
        System.arraycopy(rightHalf, 0, ergebnis, 0, 32);
        System.arraycopy(xor(leftHalf, ffunc), 0, ergebnis, 32, 32);
        return ergebnis;
    }

    char[] doNextRound() {
        if (count > 16) throw new TooManyRoundsException("DES wird nur mit 16 Runden verwendet");
        text = desRunde(text, keyGen.getNextKey());
        count++;
        return text;
    }

    public BitArray nextRound() {
        return new BitArray(doNextRound());
    }

    static char[] desBlock(char[] text, char[] key, boolean entschluesseln) {
        Des des = new Des(text, key, entschluesseln);
        des.doIP();
        for (int i = 0; i < 16; i++) des.doNextRound();

        des.switchSides();
        des.doIPrev();
        return des.text;
    }

    static void desFile(String input, String output, char[] key, boolean entschluesseln) {
        try (FileInputStream read = new FileInputStream(input); FileOutputStream write = new FileOutputStream(output)) {
            byte[] b = new byte[8];
            int anz;
            while ((anz = read.read(b)) == 8) {
                char[] coded = desBlock(byteArr2arr(b), key, entschluesseln);
                write.write(arr2byteArr(coded));
            }
            if (anz != -1) {
                for (int i = anz; i < 8; i++) b[i] = 0;
                char[] coded = desBlock(byteArr2arr(b), key, entschluesseln);
                write.write(arr2byteArr(coded));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doIP() {
        text = ip(text);
    }

    public void doIPrev() {
        text = ip_rev(text);
    }

    public void switchSides() {
        char[] tmp = text;
        text = new char[64];
        System.arraycopy(tmp, 0, text, 32, 32);
        System.arraycopy(tmp, 32, text, 0, 32);
    }

    static char[] ip(char[] input) {
        return new char[]{input[57], input[49], input[41], input[33], input[25], input[17], input[9], input[1],
                input[59], input[51], input[43], input[35], input[27], input[19], input[11], input[3],
                input[61], input[53], input[45], input[37], input[29], input[21], input[13], input[5],
                input[63], input[55], input[47], input[39], input[31], input[23], input[15], input[7],
                input[56], input[48], input[40], input[32], input[24], input[16], input[8], input[0],
                input[58], input[50], input[42], input[34], input[26], input[18], input[10], input[2],
                input[60], input[52], input[44], input[36], input[28], input[20], input[12], input[4],
                input[62], input[54], input[46], input[38], input[30], input[22], input[14], input[6]};
    }

    static char[] ip_rev(char[] input) {
        return new char[]{input[39], input[7], input[47], input[15], input[55], input[23], input[63], input[31],
                input[38], input[6], input[46], input[14], input[54], input[22], input[62], input[30],
                input[37], input[5], input[45], input[13], input[53], input[21], input[61], input[29],
                input[36], input[4], input[44], input[12], input[52], input[20], input[60], input[28],
                input[35], input[3], input[43], input[11], input[51], input[19], input[59], input[27],
                input[34], input[2], input[42], input[10], input[50], input[18], input[58], input[26],
                input[33], input[1], input[41], input[9], input[49], input[17], input[57], input[25],
                input[32], input[0], input[40], input[8], input[48], input[16], input[56], input[24]};
    }
}
