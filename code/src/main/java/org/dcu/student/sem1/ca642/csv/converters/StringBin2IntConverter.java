package org.dcu.student.sem1.ca642.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import org.dcu.student.sem1.ca642.model.Binary;

public class StringBin2IntConverter extends AbstractBeanField<String, Long> {

    @Override
    protected Object convert(final String value) {
        return Binary.from(value).toInt();
    }

    @Override
    protected String convertToWrite(final Object value) {
        final int integer = (int) value;
        return Binary.from(integer).toString();
    }

}
