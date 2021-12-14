package org.dcu.student.sem1.ca642.csv;

import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;
import org.dcu.student.sem1.ca642.csv.converters.StringHex2LongConverter;
import org.dcu.student.sem1.ca642.model.Hexadecimal;

@Data
public class CsvEntry {

    @CsvCustomBindByName(converter = StringHex2LongConverter.class)
    private long plaintext;
    @CsvCustomBindByName(converter = StringHex2LongConverter.class)
    private long ciphertext;

    @Override
    public String toString() {
        return "Entry{" +
              "plaintext=" + Hexadecimal.from(plaintext) +
              ", ciphertext=" + Hexadecimal.from(ciphertext) +
              '}';
    }

}