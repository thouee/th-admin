package me.th.share.query;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PageLink implements Serializable {

    private static final long serialVersionUID = 5261416618332109033L;

    private Integer pageSize = 10;

    private Integer pageNum = 0;
}
