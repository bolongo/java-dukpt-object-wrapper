package dev.bolongo.DukptObjectWrapper;

import com.softwareverde.security.dukpt.BitSet;
import java.lang.RuntimeException;

public class KSN extends BaseCode {
    private long transactionCounter;

    private long transactionsLimit = 0b111111111111111111111;
    private long threshold = 100;
    private boolean isOverThreshold = false;


    public KSN(String value) {
        super(value);
    }

    public KSN(byte[] value) {
        super(value);
    }

    public KSN(BitSet value) {
        super(value);
    }

    public boolean isOverThreshold() {
        return this.isOverThreshold;
    }

    public KSN updateTransactionCounter(long value) {
        //Esta solamente tiene sentido como modificacion, mas no como inicializador
        this.transactionCounter = value;

        BitSet transactionCounterBitSet = this.bitSetFromLong(value);
        transactionCounterBitSet = this.bitSetShiftLeft(transactionCounterBitSet, 24 - transactionCounterBitSet.length());
        this.setTransactionCounterBitSet(transactionCounterBitSet);
        super.updateBitSet(this.asBitSet);

        return this;
    }

    public KSN updateBitSet(BitSet value) {
        super.updateBitSet(value);
        BitSet transactionCounterBitSet = getTransactionCounterBitSet();
        this.transactionCounter = this.bitSetToLong(transactionCounterBitSet);

        return this;
    }

    public KSN updateByteArray(byte[] value) {
        super.updateByteArray(value);
        BitSet transactionCounterBitSet = getTransactionCounterBitSet();
        this.transactionCounter = this.bitSetToLong(transactionCounterBitSet);

        return this;
    }

    public KSN updateHexadecimal(String value) {
        super.updateHexadecimal(value);
        BitSet transactionCounterBitSet = getTransactionCounterBitSet();
        this.transactionCounter = this.bitSetToLong(transactionCounterBitSet);

        return this;
    }

    public KSN decrementTransactionCounter() {
        return this.decrementTransactionCounter(1L);
    }

    public KSN decrementTransactionCounter(long value) {
        return this.incrementTransactionCounter(-value);
    }

    public KSN incrementTransactionCounter() {
        return this.incrementTransactionCounter(1L);
    }

    public KSN incrementTransactionCounter(long value) {
        if(this.transactionCounter + value< 0) {
            throw new TransactionCounterUnderflowException("Transaction counter is under the minimum value");
        }

        if(this.transactionCounter + value > this.transactionsLimit) {
            throw new TransactionCounterOverflowException("Transaction counter max value exceeded");
        }

        this.isOverThreshold = this.transactionCounter > this.transactionsLimit + value - this.threshold;

        this.transactionCounter += value;

        return this.updateTransactionCounter(this.transactionCounter);
    }

    private BitSet getTransactionCounterBitSet() {
        BitSet bitSetVal = asBitSet.get(59, this.asBitSet.length());
        return this.bitSetShiftLeft(bitSetVal, 3);
    }

    private void setTransactionCounterBitSet(BitSet value) {
        BitSet bitToInsert = this.bitSetShiftRight(value, 3);
        asBitSet.clear(59, asBitSet.length());
        int i = bitToInsert.nextSetBit(0);
        while (i >= 0) {
            asBitSet.set(i + 59);
            i = bitToInsert.nextSetBit(i + 1);
        }
    }

    public long getTransactionCounter() {
        return this.transactionCounter;
    }

    public KSN getBaseKSN() {
        KSN ksn = new KSN(this.toBitSet());
        return ksn.getTransactionCounter() != 0L ? ksn.updateTransactionCounter(0L) : ksn;
    }

    public class TransactionCounterOverflowException extends RuntimeException {

        public TransactionCounterOverflowException() {}

        public TransactionCounterOverflowException(String s) { super(s); }

        public TransactionCounterOverflowException(String s, Throwable throwable) { super(s, throwable); }

        public TransactionCounterOverflowException(Throwable throwable) { super(throwable); }

        public TransactionCounterOverflowException(String s, Throwable throwable, boolean b, boolean b1) { super(s, throwable, b, b1); }
    }

    public class TransactionCounterUnderflowException extends RuntimeException {
        public TransactionCounterUnderflowException() {}

        public TransactionCounterUnderflowException(String s) { super(s); }

        public TransactionCounterUnderflowException(String s, Throwable throwable) { super(s, throwable); }

        public TransactionCounterUnderflowException(Throwable throwable) { super(throwable); }

        public TransactionCounterUnderflowException(String s, Throwable throwable, boolean b, boolean b1) { super(s, throwable, b, b1); }
    }
}