package me.th.share.serializer.datamask;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import me.th.share.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class DataMaskingSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private final ThreadLocal<DataMasking> masking = new ThreadLocal<>();

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        DataMasking dataMasking = masking.get();
        MaskingFunMode fun = dataMasking.maskFun();
        ObjectNode node = JacksonUtils.newObjectNode();

        if (!MaskingFunMode.CUSTOM.equals(fun)) {
            // 使用自定义之外的方式
            node.put("masker", fun.function().apply(s));
        } else {
            // 自定义方式
            int pre = dataMasking.prefixNoMaskLen();
            int suf = dataMasking.suffixNoMaskLen();
            String symbol = dataMasking.symbol();
            String masker = doMasking(s, pre, suf, symbol);
            node.put("masker", masker);
        }

        if (dataMasking.showOrigin()) {
            node.put("origin", s);
        }

        jsonGenerator.writeObject(node);
        masking.remove();
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            DataMasking dataMasking = beanProperty.getAnnotation(DataMasking.class);
            masking.set(dataMasking);
            return this;
        }
        return serializerProvider.findNullValueSerializer(null);
    }

    private String doMasking(String origin, int pre, int suf, String symbol) {
        if (StringUtils.isEmpty(origin)) {
            return origin;
        }

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0, n = origin.length(); i < n; i++) {
            if (i < pre) {
                stringBuffer.append(origin.charAt(i));
                continue;
            }
            if (i > (n - suf - 1)) {
                stringBuffer.append(origin.charAt(i));
                continue;
            }
            stringBuffer.append(symbol);
        }
        return stringBuffer.toString();
    }
}
