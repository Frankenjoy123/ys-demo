package com.yunsoo.common.util;

/*
*  Read MongoDB's ObjectId implementation, and generate this UuidUtil.
 */

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <p>A globally unique identifier for objects.</p>
 * <p>
 * <p>Consists of 12 bytes, divided as follows:</p>
 * <table border="1">
 * <caption>ObjectID layout</caption>
 * <tr>
 * <td>0</td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td><td>11</td>
 * </tr>
 * <tr>
 * <td colspan="4">time</td><td colspan="3">machine</td> <td colspan="2">pid</td><td colspan="3">inc</td>
 * </tr>
 * </table>
 * <p>
 * <p>Instances of this class are immutable.</p>
 *
 * @mongodb.driver.manual core/object-id ObjectId
 */
public class UuidUtil implements Comparable<UuidUtil>, java.io.Serializable {

    private static final long serialVersionUID = -4415279469780082174L;

    static final Logger LOGGER = Logger.getLogger("MongoUuidUtil");

    /**
     * Gets a new object id.
     *
     * @return the new id
     */
    public static UuidUtil get() {
        return new UuidUtil();
    }

    /**
     * Checks if a string could be an {@code ObjectId}.
     *
     * @param s a potential ObjectId as a String.
     * @return whether the string could be an object id
     * @throws IllegalArgumentException if hexString is null
     */
    public static boolean isValid(String s) {
        if (s == null)
            return false;

        final int len = s.length();
        if (len != 24)
            return false;

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9')
                continue;
            if (c >= 'a' && c <= 'f')
                continue;
            if (c >= 'A' && c <= 'F')
                continue;

            return false;
        }

        return true;
    }

    /**
     * Turn an object into an {@code ObjectId}, if possible. Strings will be converted into {@code ObjectId}s, if possible, and
     * {@code ObjectId}s will be cast and returned.  Passing in {@code null} returns {@code null}.
     *
     * @param o the object to convert
     * @return an {@code ObjectId} if it can be massaged, null otherwise
     * @deprecated This method is NOT a part of public API and will be dropped in 3.x versions.
     */
    @Deprecated
    public static UuidUtil massageToObjectId(Object o) {
        if (o == null)
            return null;

        if (o instanceof UuidUtil)
            return (UuidUtil) o;

        if (o instanceof String) {
            String s = o.toString();
            if (isValid(s))
                return new UuidUtil(s);
        }

        return null;
    }


    /**
     * Creates a new instance from a string.
     *
     * @param s the string to convert
     * @throws IllegalArgumentException if the string is not a valid id
     */
    public UuidUtil(String s) {
        this(s, false);
    }

    /**
     * Constructs a new instance of {@code ObjectId} from a string.
     *
     * @param s      the string representation of ObjectId. Can contains only [0-9]|[a-f]|[A-F] characters.
     * @param babble if {@code true} - convert to 'babble' objectId format
     * @deprecated 'babble' format is deprecated. Please use {@link #UuidUtil(String)} instead.
     */
    @Deprecated
    public UuidUtil(String s, boolean babble) {

        if (!isValid(s))
            throw new IllegalArgumentException("invalid ObjectId [" + s + "]");

        if (babble)
            s = babbleToMongod(s);

        byte b[] = new byte[12];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        ByteBuffer bb = ByteBuffer.wrap(b);
        _time = bb.getInt();
        _machine = bb.getInt();
        _inc = bb.getInt();
        _new = false;
    }

    /**
     * Create a new object id.
     */
    public UuidUtil() {
        _time = (int) (System.currentTimeMillis() / 1000);
        _machine = _genmachine;
        _inc = _nextInc.getAndIncrement();
        _new = true;
    }

    @Override
    public int hashCode() {
        int x = _time;
        x += (_machine * 111);
        x += (_inc * 17);
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        UuidUtil other = massageToObjectId(o);
        if (other == null)
            return false;

        return
                _time == other._time &&
                        _machine == other._machine &&
                        _inc == other._inc;
    }


    /**
     * @return a string representation of the ObjectId in hexadecimal format
     * @see UuidUtil#toHexString()
     * @deprecated use {@link #toHexString()}
     */
    @Deprecated
    public String toStringMongod() {
        byte b[] = toByteArray();

        StringBuilder buf = new StringBuilder(24);

        for (int i = 0; i < b.length; i++) {
            int x = b[i] & 0xFF;
            String s = Integer.toHexString(x);
            if (s.length() == 1)
                buf.append("0");
            buf.append(s);
        }

        return buf.toString();
    }

    /**
     * Convert to a byte array.  Note that the numbers are stored in big-endian order.
     *
     * @return the byte array
     */
    public byte[] toByteArray() {
        byte b[] = new byte[12];
        ByteBuffer bb = ByteBuffer.wrap(b);
        // by default BB is big endian like we need
        bb.putInt(_time);
        bb.putInt(_machine);
        bb.putInt(_inc);
        return b;
    }

    static String _pos(String s, int p) {
        return s.substring(p * 2, (p * 2) + 2);
    }

    /**
     * @deprecated This method is NOT a part of public API and will be dropped in 3.x versions.
     */
    @Deprecated
    public static String babbleToMongod(String b) {
        if (!isValid(b))
            throw new IllegalArgumentException("invalid object id: " + b);

        StringBuilder buf = new StringBuilder(24);
        for (int i = 7; i >= 0; i--)
            buf.append(_pos(b, i));
        for (int i = 11; i >= 8; i--)
            buf.append(_pos(b, i));

        return buf.toString();
    }

    public String toString() {
        return toStringMongod();
    }

    int _compareUnsigned(int i, int j) {
        long li = 0xFFFFFFFFL;
        li = i & li;
        long lj = 0xFFFFFFFFL;
        lj = j & lj;
        long diff = li - lj;
        if (diff < Integer.MIN_VALUE)
            return Integer.MIN_VALUE;
        if (diff > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return (int) diff;
    }

    public int compareTo(UuidUtil id) {
        if (id == null)
            return -1;

        int x = _compareUnsigned(_time, id._time);
        if (x != 0)
            return x;

        x = _compareUnsigned(_machine, id._machine);
        if (x != 0)
            return x;

        return _compareUnsigned(_inc, id._inc);
    }


    final int _time;
    final int _machine;
    final int _inc;
    boolean _new;

    private static AtomicInteger _nextInc = new AtomicInteger((new java.util.Random()).nextInt());

    private static final int _genmachine;

    static {

        try {
            // build a 2-byte machine piece based on NICs info
            int machinePiece;
            {
                try {
                    StringBuilder sb = new StringBuilder();
                    Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                    while (e.hasMoreElements()) {
                        NetworkInterface ni = e.nextElement();
                        sb.append(ni.toString());
                    }
                    machinePiece = sb.toString().hashCode() << 16;
                } catch (Throwable e) {
                    // exception sometimes happens with IBM JVM, use random
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                    machinePiece = (new Random().nextInt()) << 16;
                }
                LOGGER.fine("machine piece post: " + Integer.toHexString(machinePiece));
            }

            // add a 2 byte process piece. It must represent not only the JVM but the class loader.
            // Since static var belong to class loader there could be collisions otherwise
            final int processPiece;
            {
                int processId = new java.util.Random().nextInt();
                try {
                    processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
                } catch (Throwable t) {
                }

                ClassLoader loader = UuidUtil.class.getClassLoader();
                int loaderId = loader != null ? System.identityHashCode(loader) : 0;

                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toHexString(processId));
                sb.append(Integer.toHexString(loaderId));
                processPiece = sb.toString().hashCode() & 0xFFFF;
                LOGGER.fine("process piece: " + Integer.toHexString(processPiece));
            }

            _genmachine = machinePiece | processPiece;
            LOGGER.fine("machine : " + Integer.toHexString(_genmachine));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}