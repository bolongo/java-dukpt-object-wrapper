package dev.bolongo.DukptObjectWrapper;

import com.softwareverde.security.dukpt.BitSet;
import com.softwareverde.security.dukpt.Dukpt;

public class IPEK extends BaseCode {

    public IPEK(String value) {
        super(value);
    }

    public IPEK(byte[] value) {
        super(value);
    }

    public IPEK(BitSet value) {
        super(value);
    }

    public IPEK(BDK bdk, KSN ksn) throws Exception {
        super(Dukpt.getIpek(bdk.toBitSet(), ksn.getBaseKSN().toBitSet()));
    }
}