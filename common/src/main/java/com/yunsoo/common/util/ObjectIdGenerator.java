package com.yunsoo.common.util;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by:   Lijian
 * Created on:   2015/4/15
 * Descriptions: max value: 7oiylpimjg5u2ca1ypr, max time: 2106-02-07T06:28:15Z, max count per-second: 16777216
 */
public final class ObjectIdGenerator {
    private static final int MACHINE_IDENTIFIER;
    private static final short PROCESS_IDENTIFIER;
    private static final AtomicInteger NEXT_COUNTER = new AtomicInteger(new SecureRandom().nextInt());

    /**
     * @return id, 19 length string, Example: 2kqkwehe64b29xb6gl9
     */
    public static String getNew() {
        return toBase36String(generateBytes());
    }

    public static boolean validate(String id) {
        return id != null && id.length() == 19;
    }

    public static Date getGeneratedDateFromId(String id) {
        byte[] bytes = parseBase36String(id);
        long timestamp = ((bytes[0] & 0xff) << 24 | (bytes[1] & 0xff) << 16 | (bytes[2] & 0xff) << 8 | bytes[3] & 0xff) & 0xFFFFFFFFL;
        return new Date(timestamp * 1000L);
    }


    static {
        try {
            MACHINE_IDENTIFIER = createMachineIdentifier();
            PROCESS_IDENTIFIER = createProcessIdentifier();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int createMachineIdentifier() {
        // build a 2-byte machine piece based on NICs info
        int machinePiece;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                sb.append(ni.toString());
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    ByteBuffer bb = ByteBuffer.wrap(mac);
                    try {
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                    } catch (BufferUnderflowException shortHardwareAddressException) { //NOPMD
                        // mac with less than 6 bytes. continue
                    }
                }
            }
            machinePiece = sb.toString().hashCode();
        } catch (Throwable t) {
            // exception sometimes happens with IBM JVM, use random
            machinePiece = (new SecureRandom().nextInt());
        }
        machinePiece = machinePiece & 0x00ffffff;
        return machinePiece;
    }

    // Creates the process identifier.  This does not have to be unique per class loader because
    // NEXT_COUNTER will provide the uniqueness.
    private static short createProcessIdentifier() {
        short processId;
        try {
            String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
            if (processName.contains("@")) {
                processId = (short) Integer.parseInt(processName.substring(0, processName.indexOf('@')));
            } else {
                processId = (short) java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
            }

        } catch (Throwable t) {
            processId = (short) new SecureRandom().nextInt();
        }

        return processId;
    }

    private static byte[] generateBytes() {
        int timestamp = (int) (new Date().getTime() / 1000);
        int machineIdentifier = MACHINE_IDENTIFIER;
        int processIdentifier = PROCESS_IDENTIFIER;
        int counter = NEXT_COUNTER.getAndIncrement() & 0x00ffffff;

        byte[] bytes = new byte[12];
        bytes[0] = (byte) (timestamp >> 24);
        bytes[1] = (byte) (timestamp >> 16);
        bytes[2] = (byte) (timestamp >> 8);
        bytes[3] = (byte) (timestamp);
        bytes[4] = (byte) (machineIdentifier >> 16);
        bytes[5] = (byte) (machineIdentifier >> 8);
        bytes[6] = (byte) (machineIdentifier);
        bytes[7] = (byte) (processIdentifier >> 8);
        bytes[8] = (byte) (processIdentifier);
        bytes[9] = (byte) (counter >> 16);
        bytes[10] = (byte) (counter >> 8);
        bytes[11] = (byte) (counter);

        return bytes;
    }

    private static String toBase36String(byte[] bytes) {
        return new BigInteger(1, bytes).toString(36);
    }

    private static byte[] parseBase36String(String id) {
        byte[] bytes = new BigInteger(id, 36).toByteArray();
        if (bytes.length == 13 && bytes[0] == 0) {
            byte[] stripped = new byte[12];
            System.arraycopy(bytes, 1, stripped, 0, 12);
            return stripped;
        } else {
            return bytes;
        }
    }

}
