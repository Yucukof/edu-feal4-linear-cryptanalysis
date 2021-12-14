package org.dcu.student.sem1.ca642;

import lombok.extern.slf4j.Slf4j;
import org.dcu.student.sem1.ca642.model.Hexadecimal;

import java.util.Scanner;

@Slf4j
public class Cryptoprompt {

    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);

        log.info("Please provide a 8-bytes plaintext (in hex)");
        final String input = scanner.nextLine();

        if (!Hexadecimal.isHex(input)) {
            throw new IllegalArgumentException(String.format("Input [%S] is not a valid hex.", input));
        }

        final Hexadecimal plaintext = Hexadecimal.from(input);

        if (plaintext.size() != 8) {
            throw new IllegalArgumentException(String.format("Input [%s] is not a valid FEAL plaintext.", input));
        }
        log.info("Plain text:\t{}", plaintext);
        log.info("\t\t\t{}", plaintext.toBin().toString());

        final byte[] data = plaintext.toBytes();
        final int[] key = new int[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0};

        FEAL.encrypt(data, key);
        final Hexadecimal ciphertext = Hexadecimal.from(data);
        log.info("Cipher text:\t{}", ciphertext);
        log.info("\t\t\t{}", ciphertext.toBin().toString());

        FEAL.decrypt(data, key);
        final Hexadecimal decipherText = Hexadecimal.from(data);
        log.info("Decipher text:\t{}", decipherText);
        log.info("\t\t\t{}", decipherText.toBin().toString());
    }
}
