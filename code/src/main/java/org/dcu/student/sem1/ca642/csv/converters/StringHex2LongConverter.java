package org.dcu.student.sem1.ca642.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import org.dcu.student.sem1.ca642.model.Hexadecimal;

public class StringHex2LongConverter extends AbstractBeanField<String, Hexadecimal> {

    @Override
    protected Object convert(final String value) {
        return Hexadecimal.from(value).toLong();
    }
}
