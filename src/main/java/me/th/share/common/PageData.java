package me.th.share.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class PageData<T> {

    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Collection<T> records;
}
