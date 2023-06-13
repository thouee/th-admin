package me.th.share.query;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class QueryOrder implements Serializable {

    private static final long serialVersionUID = -1347657387502385724L;

    /**
     * 排序方向，字典[order_direction][value]
     */
    private Integer direction;

    /**
     * 排序列名
     */
    private String column;
}
