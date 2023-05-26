package me.th.share.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1942375890021916923L;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    @ApiModelProperty(value = "创建者", hidden = true)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    @ApiModelProperty(value = "更新者", hidden = true)
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Timestamp createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Timestamp updatedTime;

    // TODO 此处可根据需要添加 is_deleted

    /**
     * 分组校验
     */
    public @interface Create {
    }

    /**
     * 分组校验
     */
    public @interface Update {
    }

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
