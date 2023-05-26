package me.th.share.common;

import lombok.Getter;
import lombok.Setter;

/**
 * PageResponse
 */
@Getter
@Setter
public class PR<T> extends R<PageData<T>> {

    public PR(PageData<T> aPage) {
        super(aPage);
    }
}
