package com.ainigma100.customerapi.utils.annotation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;


public class MaskDataSerializer extends JsonSerializer<Object> implements ContextualSerializer {


    private final int visibleCharactersAtEnd;
    private final String maskSymbol;


    public MaskDataSerializer() {
        this.visibleCharactersAtEnd = 4;
        this.maskSymbol = "*";
    }

    public MaskDataSerializer(int visibleCharactersAtEnd, String maskSymbol) {
        this.visibleCharactersAtEnd = visibleCharactersAtEnd;
        this.maskSymbol = maskSymbol;
    }


    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {

        if (value != null) {

            String stringValue = value.toString();

            // Mask all characters except the last visibleCharactersAtEnd
            String regex = String.format(".(?=.{%d})", visibleCharactersAtEnd);
            String maskedValue = stringValue.replaceAll(regex, this.maskSymbol);

            gen.writeString(maskedValue);

        } else {
            gen.writeNull();
        }

    }


    // Find more info here: https://fasterxml.github.io/jackson-core/javadoc/1.9/org/codehaus/jackson/map/ContextualSerializer.html
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {


        if (property != null) {

            // Access the MaskData annotation
            MaskData maskData = property.getAnnotation(MaskData.class);

            if (maskData != null) {
                // Use the values from the MaskData annotation to create a new instance using the constructor (based on the suggestion of the documentation)
                return new MaskDataSerializer(maskData.visibleCharactersAtEnd(), maskData.maskSymbol());
            }
        }

        return this;
    }
}
