package me.th.share.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseQueryOrder implements Serializable {

    private static final long serialVersionUID = -1347657387502385724L;

    /**
     * 是否使用排序
     */
    private Boolean useOrder;

    /**
     * 排序方向，字典[order_direction][value]
     */
    private Integer direction;

    /**
     * 排序列名
     */
    private String column;
}
