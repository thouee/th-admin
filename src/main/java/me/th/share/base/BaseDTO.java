package me.th.share.base;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

@Getter
@Setter
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = -3807676077863985750L;

    private String createdBy;

    private String updatedBy;

    private Timestamp createdTime;

    private Timestamp updatedTime;

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                builder.append(field.getName(), field.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toSpring builder 发生了错误");
        }
        return builder.toString();
    }
}
