package me.th.share.base;

import lombok.Getter;
import lombok.Setter;
import me.th.share.query.PageLink;
import me.th.share.query.QueryOrder;

import java.io.Serializable;

@Getter
@Setter
public class BaseQueryCriteria implements Serializable {

    private static final long serialVersionUID = -2183492679004157722L;

    private QueryOrder queryOrder;

    private PageLink pageLink;
}
