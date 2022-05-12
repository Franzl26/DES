package DES;

public class RoundKeyGen {
    private char[] key;
    private int round = 1;
    private final boolean entschluesseln;

    RoundKeyGen(char[] key, boolean entschluesseln) {
        if (key.length != 64) throw new IllegalArgumentException("Der Schlüssel mus 64 Bit lang sein");
        this.key = pc1(key);
        this.entschluesseln = entschluesseln;
    }

    char[] getNextKey() {
        key = shift(key, round, entschluesseln);
        round++;
        return pc2(key);
    }

    public BitArray nextKey() {
        if (round > 16) throw new TooManyRoundsException("Bei DES werden nur 16 Schlüsselrunden durchgeführt");
        return new BitArray(getNextKey());
    }

    static char[] shift(char[] input, int round, boolean rechts) {
        char[] newKey = new char[56];
        int shift = 2;
        if (rechts) {
            if (round == 1) shift = 0;
            else if (round == 2 || round == 9 || round == 16) shift = 1;
            // rechts Schieben linke Hälfte
            System.arraycopy(input, 0, newKey, shift, 28 - shift);
            System.arraycopy(input, 28 - shift, newKey, 0, shift);
            // rechts Schieben rechte Hälfte
            System.arraycopy(input, 28, newKey, 28 + shift, 28 - shift);
            System.arraycopy(input, 56 - shift, newKey, 28, shift);
        } else {
            if (round == 1 || round == 2 || round == 9 || round == 16) shift = 1;
            // links Schieben linke Hälfte
            System.arraycopy(input, shift, newKey, 0, 28 - shift);
            System.arraycopy(input, 0, newKey, 28 - shift, shift);
            // links Schieben rechte Hälfte
            System.arraycopy(input, shift + 28, newKey, 28, 28 - shift);
            System.arraycopy(input, 28, newKey, 56 - shift, shift);
        }
        return newKey;
    }

    static char[] pc1(char[] input) {
        return new char[]{input[56], input[48], input[40], input[32], input[24], input[16], input[8], input[0],
                input[57], input[49], input[41], input[33], input[25], input[17], input[9], input[1],
                input[58], input[50], input[42], input[34], input[26], input[18], input[10], input[2],
                input[59], input[51], input[43], input[35], input[62], input[54], input[46], input[38],
                input[30], input[22], input[14], input[6], input[61], input[53], input[45], input[37],
                input[29], input[21], input[13], input[5], input[60], input[52], input[44], input[36],
                input[28], input[20], input[12], input[4], input[27], input[19], input[11], input[3]};
    }

    static char[] pc2(char[] input) {
        return new char[]{input[13], input[16], input[10], input[23], input[0], input[4], input[2], input[27],
                input[14], input[5], input[20], input[9], input[22], input[18], input[11], input[3],
                input[25], input[7], input[15], input[6], input[26], input[19], input[12], input[1],
                input[40], input[51], input[30], input[36], input[46], input[54], input[29], input[39],
                input[50], input[44], input[32], input[47], input[43], input[48], input[38], input[55],
                input[33], input[52], input[45], input[41], input[49], input[35], input[28], input[31]};
    }
}
