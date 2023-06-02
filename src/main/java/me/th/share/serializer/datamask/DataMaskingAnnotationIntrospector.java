package me.th.share.serializer.datamask;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import org.springframework.stereotype.Component;

@Component(value = "dataMaskingAnnotationIntrospector")
public class DataMaskingAnnotationIntrospector extends NopAnnotationIntrospector {

    private static final long serialVersionUID = -921971731072527298L;

    @Override
    public Object findSerializer(Annotated am) {
        DataMasking dataMasking = am.getAnnotation(DataMasking.class);
        if (dataMasking != null) {
            return null;
        }
        return null;
    }
}
