package DES;

import static DES.Funktionen.*;

public class BitArray {
    private char[] array;

    BitArray(char[] array) {
        this.array = array;
        if (!validate(array.length)) throw new IllegalArgumentException("Array ist kein BitArray");
    }

    int getLength() {
        return array.length;
    }

    @Override
    public String toString() {
        return arr2hexString(array);
    }

    public String toString(int basis) {
        return toString(basis, false);
    }

    public String toString(int basis, boolean byteTrenner) {
        if (basis != 2 && basis != 16)
            throw new UnsupportedOperationException("BitArrays können nur in Strings mit der Basis 2 oder 16 umgewandelt werden");
        if (basis == 2) return arr2binString(array, byteTrenner);
        else return arr2hexString(array, byteTrenner);
    }

    public char[] getArray() {
        return array;
    }

    public BitArray getRightHalf() {
        char[] tmp = new char[32];
        System.arraycopy(array, 32, tmp, 0, 32);
        return new BitArray(tmp);
    }

    public BitArray getLeftHalf() {
        char[] tmp = new char[32];
        System.arraycopy(array, 0, tmp, 0, 32);
        return new BitArray(tmp);
    }

    public void swapHalfs() {
        char[] tmp = new char[64];
        System.arraycopy(array, 0, tmp, 32, 32);
        System.arraycopy(array, 32, tmp, 0, 32);
        array = tmp;
    }

    public void setArray(char[] array) {
        this.array = array;
    }

    public void setBit(char bit, int position) {
        if (bit != 0 && bit != 1) throw new IllegalArgumentException("Bit darf nur 0 oder 1 sein");
        if (position < 0 || position >= array.length)
            throw new IllegalArgumentException("Position außerhalb des arrays");
        array[position] = bit;
    }

    boolean validate(int length) {
        if (length != array.length) return false;
        for (char ch : array) {
            if (ch != 1 && ch != 0) return false;
        }
        return true;
    }
}
