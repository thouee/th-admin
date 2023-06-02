package me.th.share.serializer.dict;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import me.th.share.util.JacksonUtils;

import java.io.IOException;

public class DictSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private final ThreadLocal<String> key = new ThreadLocal<>();

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 此部分需根据具体业务进行处理
        // 从缓存中获取，缓存可以使用 redis，数据结构使用 hash
        // key 为键，value 为项，获取对应数据 name
        // 获取不到就从数据库中查询
        // 查询到就放入缓存然后返回
        // 查询不到就说明没有这个字典项，直接返回空

        ObjectNode node = JacksonUtils.newObjectNode();
        node.put("code", "");
        node.put("label", "");
        jsonGenerator.writeObject(node);
        key.remove();
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            Dict dict = beanProperty.getAnnotation(Dict.class);
            this.key.set(dict.key());
            return this;
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
