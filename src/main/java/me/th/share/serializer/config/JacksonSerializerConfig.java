package me.th.share.serializer.config;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.th.share.serializer.dict.DictAnnotationIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.annotation.Resource;

@Configuration
public class JacksonSerializerConfig {

    @Resource(name = "dictAnnotationIntrospector")
    private DictAnnotationIntrospector dictAnnotationIntrospector;

    @Bean
    public ObjectMapper jackson2ObjectMapper(Jackson2ObjectMapperBuilder builder) {
        // 根据已有的配置创建自定义 om
        ObjectMapper om = builder.createXmlMapper(false).build();
        AnnotationIntrospector old = om.getSerializationConfig().getAnnotationIntrospector();
        AnnotationIntrospector pair = AnnotationIntrospector.pair(old,
                dictAnnotationIntrospector);
        om.setAnnotationIntrospector(pair);
        return om;
    }
}
