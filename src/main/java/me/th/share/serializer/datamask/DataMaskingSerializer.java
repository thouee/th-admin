package me.th.share.serializer.datamask;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class DataMaskingSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private final ThreadLocal<DataMasking> masker = new ThreadLocal<>();

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        DataMasking dataMasking = masker.get();
        MaskingMode mode = dataMasking.mode();
        // 此处根据不同 mode，采取不同的脱敏方式，包括自定义方式

        jsonGenerator.writeString(s);
        masker.remove();
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            DataMasking dataMasking = beanProperty.getAnnotation(DataMasking.class);
            masker.set(dataMasking);
            return this;
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
