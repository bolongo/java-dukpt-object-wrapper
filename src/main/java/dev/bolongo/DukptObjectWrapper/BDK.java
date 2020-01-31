package dev.bolongo.DukptObjectWrapper;

import com.softwareverde.security.dukpt.BitSet;

class BDK extends BaseCode {
    public BDK(String value) {
        super(value);
    }

    public BDK(byte[] value) {
        super(value);
    }

    public BDK(BitSet value) {
        super(value);
    }
}