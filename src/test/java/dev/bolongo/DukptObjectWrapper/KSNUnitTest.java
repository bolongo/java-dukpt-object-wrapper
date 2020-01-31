package dev.bolongo.DukptObjectWrapper;

import org.junit.Test;
import static org.junit.Assert.*;

public class KSNUnitTest {
    @Test
    public void testKSNIncrement() {
        KSN ksn = new KSN("00000000000000000001");

        ksn.incrementTransactionCounter();

        assertEquals("00000000000000000002", ksn.toHexadecimal());

        ksn.decrementTransactionCounter(2);

        assertEquals("00000000000000000000", ksn.toHexadecimal());

        ksn.incrementTransactionCounter(0b111111111111111111110);

        assertEquals("000000000000001FFFFE", ksn.toHexadecimal());

        ksn.incrementTransactionCounter();

        assertEquals("000000000000001FFFFF", ksn.toHexadecimal());

        try {
            ksn.incrementTransactionCounter();
            fail();
        } catch (KSN.TransactionCounterOverflowException correctException) {
            assertTrue(true);
        } catch (Exception exception) {
            fail();
        }

        ksn.decrementTransactionCounter(0b111111111111111111111);

        assertEquals("00000000000000000000", ksn.toHexadecimal());

        try {
            ksn.decrementTransactionCounter();
            fail();
        } catch (KSN.TransactionCounterUnderflowException correctException) {
            assertTrue(true);
        } catch (Exception exception) {
            fail();
        }
    }
}
