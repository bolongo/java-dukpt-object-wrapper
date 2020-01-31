package dev.bolongo.DukptObjectWrapper;

import com.softwareverde.security.dukpt.BitSet;
import com.softwareverde.security.dukpt.Dukpt;

class BaseCode {
    protected String asString;
    protected BitSet asBitSet;
    protected byte[] asByteArray;

    public String toHexadecimal() {
        return this.asString;
    }

    public byte[] toByteArray() {
        return this.asByteArray;
    }

    public BitSet toBitSet() {
        return this.asBitSet;
    }

    BaseCode(String value ) {
        this.updateHexadecimal(value);
    }

    BaseCode(byte[] value) {
        this.updateByteArray(value);
    }

    BaseCode(BitSet value) {
        this.updateBitSet(value);
    }

    protected BaseCode updateBitSet(BitSet value) {
        this.asBitSet = value;
        this.asByteArray = Dukpt.toByteArray(asBitSet);
        this.asString = Dukpt.toHex(this.asByteArray);
        return this;
    }

    protected BaseCode updateByteArray(byte[] value) {
        this.asByteArray = value;
        this.asBitSet = Dukpt.toBitSet(value);
        this.asString = Dukpt.toHex(this.asByteArray);
        return this;
    }

    protected BaseCode updateHexadecimal(String value) {
        this.asString = value;
        this.asByteArray = Dukpt.toByteArray(value);
        this.asBitSet = Dukpt.toBitSet(asByteArray);
        return this;
    }

    protected Long bitSetToLong(BitSet bitSet) {
        long value = 0L;
        for (int i = 0; i < bitSet.length(); i++) {
            int index = (bitSet.length() - (8 * (i / 8)) - 8) + (7 - (i % 8));
            if(bitSet.get(index)) {
                value = (value | (1L << i));
            }
        }
        return value;
    }

    protected BitSet bitSetFromLong(Long value) {
        if(value == 0L) {
            return new BitSet(8);
        }
        int bitCount = (int) Math.ceil(Math.log(value.doubleValue()) / Math.log(2));
        BitSet bitSet = new BitSet( ((int) Math.ceil(bitCount / 8.0)) * 8);

        long _value = value;
        int i = 0;
        while (_value != 0L) {
            if(_value % 2L != 0L) {
                bitSet.set(bitSet.length() - i - 1);
            }
            _value = _value >> 1;
            i++;
        }

        return bitSet;
    }

    protected BitSet bitSetShiftRight(BitSet bitSet, int shifts) {
        return bitSet.get(shifts, Math.max(shifts, bitSet.length()));
    }

    protected BitSet bitSetShiftLeft(BitSet bitSet, int shifts) {
        BitSet response = new BitSet(bitSet.length() + shifts);
        int i = bitSet.nextSetBit(0);
        while (i >= 0) {
            response.set(i + shifts);
            i = bitSet.nextSetBit(i + 1);
        }
        return response;
    }
}