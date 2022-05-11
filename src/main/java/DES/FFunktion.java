package DES;

import static DES.Funktionen.xor;

class FFunktion {
    private static final char[] s1 = {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13};
    private static final char[] s2 = {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9};
    private static final char[] s3 = {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12};
    private static final char[] s4 = {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14};
    private static final char[] s5 = {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3};
    private static final char[] s6 = {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13};
    private static final char[] s7 = {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12};
    private static final char[] s8 = {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11};
    private static final char[][] sBoxen = {s1, s2, s3, s4, s5, s6, s7, s8};

    static char[] f(char[] cipherRight, char[] roundKey) {
        char[] expanded = e(cipherRight);
        char[] xored = xor(expanded, roundKey);
        char[] sboxed = s(xored);

        return p(sboxed);
    }

    static char[] s(char[] xored) {
        char[] sboxed = new char[32];
        for (int i = 0; i < 8; i++) {
            int index = i * 6;
            int input = 32 * xored[index] + 16 * xored[index + 5] + 8 * xored[index + 1] + 4 * xored[index + 2]
                    + 2 * xored[index + 3] + xored[index + 4];
            int output = sBoxen[i][input];
            index = (char) (i * 4);
            sboxed[index + 3] = 5;
            sboxed[index + 3] = (char) (output % 2);
            output /= 2;
            sboxed[index + 2] = (char) (output % 2);
            output /= 2;
            sboxed[index + 1] = (char) (output % 2);
            output /= 2;
            sboxed[index] = (char) (output % 2);
        }
        return sboxed;
    }


    static char[] e(char[] input) {
        return new char[]{input[31], input[0], input[1], input[2], input[3], input[4],
                input[3], input[4], input[5], input[6], input[7], input[8],
                input[7], input[8], input[9], input[10], input[11], input[12],
                input[11], input[12], input[13], input[14], input[15], input[16],
                input[15], input[16], input[17], input[18], input[19], input[20],
                input[19], input[20], input[21], input[22], input[23], input[24],
                input[23], input[24], input[25], input[26], input[27], input[28],
                input[27], input[28], input[29], input[30], input[31], input[0]};
    }

    static char[] p(char[] input) {
        return new char[]{input[15], input[6], input[19], input[20], input[28], input[11], input[27], input[16],
                input[0], input[14], input[22], input[25], input[4], input[17], input[30], input[9],
                input[1], input[7], input[23], input[13], input[31], input[26], input[2], input[8],
                input[18], input[12], input[29], input[5], input[21], input[10], input[3], input[24]};
    }
}
