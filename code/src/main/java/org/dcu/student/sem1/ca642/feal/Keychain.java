package org.dcu.student.sem1.ca642.feal;

import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.dcu.student.sem1.ca642.FEAL;
import org.dcu.student.sem1.ca642.csv.CsvEntry;
import org.dcu.student.sem1.ca642.csv.CsvHelper;
import org.dcu.student.sem1.ca642.csv.converters.StringBin2IntConverter;
import org.dcu.student.sem1.ca642.model.Binary;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.dcu.student.sem1.ca642.utils.ConversionUtils.bytes2long;
import static org.dcu.student.sem1.ca642.utils.ConversionUtils.long2bytes;

/**
 * An object representing the list of round keys used in FEAL-4 modified algorithms.
 *
 * @see Encryption
 * @see Decryption
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Keychain {

    /**
     * CSV Headers
     *
     * @see CsvHelper#exportKeychains(Collection, String)
     */
    protected static final String[] COLUMN_NAMES = Arrays.stream(Keychain.class.getDeclaredFields())
          .map(Field::getName)
          .filter(name -> !name.equals("COLUMN_NAMES"))
          .toArray(String[]::new);

    @CsvCustomBindByName(converter = StringBin2IntConverter.class)
    private int k0;
    @CsvCustomBindByName(converter = StringBin2IntConverter.class)
    private int k1;
    @CsvCustomBindByName(converter = StringBin2IntConverter.class)
    private int k2;
    @CsvCustomBindByName(converter = StringBin2IntConverter.class)
    private int k3;
    @CsvCustomBindByName(converter = StringBin2IntConverter.class)
    private int k4;
    @CsvCustomBindByName(converter = StringBin2IntConverter.class)
    private int k5;

    public Keychain(final int k0) {
        this.k0 = k0;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Keychain clone() {
        return Keychain.builder()
              .k0(this.k0)
              .k1(this.k1)
              .k2(this.k2)
              .k3(this.k3)
              .k4(this.k4)
              .k5(this.k5)
              .build();
    }

    @Override
    public String toString() {
        return "Keychain{" +
              "k0=" + Binary.from(k0) +
              ", k1=" + Binary.from(k1) +
              ", k2=" + Binary.from(k2) +
              ", k3=" + Binary.from(k3) +
              ", k4=" + Binary.from(k4) +
              ", k5=" + Binary.from(k5) +
              '}';
    }

    public int get(final int index) {
        switch (index) {
            case 0:
                return k0;
            case 1:
                return k1;
            case 2:
                return k2;
            case 3:
                return k3;
            case 4:
                return k4;
            case 5:
                return k5;
            default:
                throw new IndexOutOfBoundsException("Invalid key index: [" + index + "], must be between 0 and 5!");
        }
    }

    /**
     * Checks if the current keychain produce the same ciphertext for each known pair plaintext-ciphertext.
     *
     * @param dictionary the list of known pairs to try.
     * @return true if for all pairs the encryption of the plaintext matches the known ciphertext, false otherwise.
     */
    public boolean matches(final Collection<CsvEntry> dictionary) {
        return dictionary.stream()
              .allMatch(entry -> // @formatter:off
                    encrypt(entry.getPlaintext()) == entry.getCiphertext() &&
                    decrypt(entry.getCiphertext()) == entry.getPlaintext()
                                 // @formatter:on
              );
    }

    /**
     * Encrypt a given plaintext using the current keychain, using the provided FEAL encryption method.
     *
     * @param plaintext the long plaintext to encrypt.
     * @return a long ciphertext.
     * @see FEAL#encrypt(byte[], int[])
     */
    public long encrypt(final long plaintext) {

        final int[] keys = new int[]{k0, k1, k2, k3, k4, k5};
        final byte[] word = long2bytes(plaintext);

        FEAL.encrypt(word, keys);

        return bytes2long(word);
    }

    /**
     * Encrypt a given plaintext using the current keychain, using the provided FEAL encryption method.
     *
     * @param plaintext the long plaintext to encrypt.
     * @return a long ciphertext.
     * @see FEAL#encrypt(byte[], int[])
     */
    public long decrypt(final long plaintext) {

        final int[] keys = new int[]{k0, k1, k2, k3, k4, k5};
        final byte[] word = long2bytes(plaintext);

        FEAL.decrypt(word, keys);

        return bytes2long(word);
    }
}
