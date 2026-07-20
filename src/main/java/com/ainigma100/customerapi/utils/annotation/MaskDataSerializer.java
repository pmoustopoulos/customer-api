package com.ainigma100.customerapi.utils.annotation;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class MaskDataSerializer extends ValueSerializer<Object> {


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
    public void serialize(Object value, JsonGenerator gen, SerializationContext serializationContext) {

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


    // In Jackson 3 the ContextualSerializer contract is folded into ValueSerializer#createContextual
    @Override
    public ValueSerializer<?> createContextual(SerializationContext serializationContext, BeanProperty property) {

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
