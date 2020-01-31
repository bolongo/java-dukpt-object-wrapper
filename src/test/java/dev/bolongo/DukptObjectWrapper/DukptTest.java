package dev.bolongo.DukptObjectWrapper;

import com.softwareverde.security.dukpt.Dukpt;
import com.softwareverde.security.dukpt.DukptVariant;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * This test was taken from https://github.com/bolongo/java-dukpt library and in here serves the purpose of showing the
 * how these operations should be done using this library
 */
public class DukptTest {
    @Test
    public void testDecryptValidData() throws Exception {
        // Setup
        BDK bdk = new BDK("0123456789ABCDEFFEDCBA9876543210");// ANSI Test Key
        KSN ksn = new KSN("FFFF9876543210E00008");

        String dataHexString = "C25C1D1197D31CAA87285D59A892047426D9182EC11353C051ADD6D0F072A6CB3436560B3071FC1FD11D9F7E74886742D9BEE0CFD1EA1064C213BB55278B2F12";
        String expectedValue = "%B5452300551227189^HOGAN/PAUL      ^08043210000000725000000?\0\0\0\0";

        byte[] data = Dukpt.toByteArray(dataHexString);

        // Action
        byte[] key = Dukpt.computeKey(bdk.toByteArray(), ksn.toByteArray());
        data = Dukpt.decryptTripleDes(key, data);
        String dataOutput = new String(data, StandardCharsets.UTF_8);

        // Assert
        assertEquals(expectedValue, dataOutput);
    }

    @Test
    public void testEncrypt() throws Exception {
        // Setup
        BDK bdk = new BDK("0123456789ABCDEFFEDCBA9876543210");// ANSI Test Key
        KSN ksn = new KSN("FFFF9876543210E00008");
        String payloadString = "Mary had a little lamb.";

        byte[] encryptedPayload;
        byte[] decryptedPayload;

        // Action
        byte[] key = Dukpt.computeKey(bdk.toByteArray(), ksn.toByteArray());
        encryptedPayload = Dukpt.encryptTripleDes(key, payloadString.getBytes(StandardCharsets.UTF_8), true);
        decryptedPayload = Dukpt.decryptTripleDes(key, encryptedPayload);

        String dataOutput = new String(decryptedPayload, StandardCharsets.UTF_8).trim();

        // Assert
        assertEquals(payloadString, dataOutput);
    }

    @Test
    public void testGetIpek() throws Exception {
        // Setup
        BDK bdk = new BDK("0123456789ABCDEFFEDCBA9876543210");// ANSI Test Key
        KSN ksn = new KSN("629949012C0000000003");

        // Action
        final DukptVariant dukptVariant = new DukptVariant();
        IPEK ipek = new IPEK(bdk, ksn);

        // Assert
        assertEquals("D2943CCF80F42E88E23C12D1162FD547", ipek.toHexadecimal());
    }

    @Test
    public void testToDataKey() throws Exception {
        // Setup
        BDK bdk = new BDK("0123456789ABCDEFFEDCBA9876543210");// ANSI Test Key
        KSN ksn = new KSN("FFFF9876543210E00008");

        // Action
        final DukptVariant dukptVariant = new DukptVariant(Dukpt.KEY_REGISTER_BITMASK, Dukpt.DATA_VARIANT_BITMASK);
        byte[] derivedKey = dukptVariant.computeKey(bdk.toByteArray(), ksn.toByteArray());
        byte[] dataKey = dukptVariant.toDataKey(derivedKey);

        // Assert
        assertEquals("C39B2778B058AC376FB18DC906F75CBA", Dukpt.toHex(dataKey));
    }

    @Test
    public void testDecryptValidDataWithIpek() throws Exception {
        // Setup
        IPEK ipek = new IPEK("6AC292FAA1315B4D858AB3A3D7D5933A");
        KSN ksn = new KSN("FFFF9876543210E00008");
        String dataHexString = "C25C1D1197D31CAA87285D59A892047426D9182EC11353C051ADD6D0F072A6CB3436560B3071FC1FD11D9F7E74886742D9BEE0CFD1EA1064C213BB55278B2F12";
        String expectedValue = "%B5452300551227189^HOGAN/PAUL      ^08043210000000725000000?\0\0\0\0";

        byte[] data = Dukpt.toByteArray(dataHexString);

        // Action
        byte[] key = Dukpt.computeKeyFromIpek(ipek.toByteArray(), ksn.toByteArray());
        data = Dukpt.decryptTripleDes(key, data);
        String dataOutput = new String(data, StandardCharsets.UTF_8);

        // Assert
        assertEquals(expectedValue, dataOutput);
    }

    @Test
    public void testEncryptWithIpek() throws Exception {
        // Setup
        IPEK ipek = new IPEK("6AC292FAA1315B4D858AB3A3D7D5933A");
        KSN ksn = new KSN("FFFF9876543210E00008");
        String payloadString = "Mary had a little lamb.";

        byte[] encryptedPayload;
        byte[] decryptedPayload;

        // Action
        byte[] key = Dukpt.computeKeyFromIpek(ipek.toByteArray(), ksn.toByteArray());
        encryptedPayload = Dukpt.encryptTripleDes(key, payloadString.getBytes(StandardCharsets.UTF_8), true);
        decryptedPayload = Dukpt.decryptTripleDes(key, encryptedPayload);

        String dataOutput = new String(decryptedPayload, StandardCharsets.UTF_8).trim();

        // Assert
        assertEquals(payloadString, dataOutput);
    }

    @Test
    public void testToDataKeyWithIpek() throws Exception {
        // Setup
        IPEK ipek = new IPEK("6AC292FAA1315B4D858AB3A3D7D5933A");
        KSN ksn = new KSN("FFFF9876543210E00008");

        // Action
        final DukptVariant dukptVariant = new DukptVariant(Dukpt.KEY_REGISTER_BITMASK, Dukpt.DATA_VARIANT_BITMASK);
        byte[] derivedKey = dukptVariant.computeKeyFromIpek(ipek.toByteArray(), ksn.toByteArray());
        byte[] dataKey = dukptVariant.toDataKey(derivedKey);

        // Assert
        assertEquals("C39B2778B058AC376FB18DC906F75CBA", Dukpt.toHex(dataKey));
    }
}
