package me.th.share.util;

import me.th.share.common.PageData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PageUtils {

    private PageUtils() {
    }

    /**
     * 由 {@link Page} 转为 {@link PageData}
     *
     * @param page -
     * @return PageData<T>
     */
    public static <T> PageData<T> toPageData(Page<T> page) {
        Pageable pageable = page.getPageable();
        PageData<T> pageData = new PageData<>();
        pageData.setPageSize(pageable.getPageSize());
        pageData.setPageNum(pageable.getPageNumber());
        pageData.setTotal(page.getTotalElements());
        pageData.setRecords(page.getContent());
        return pageData;
    }
}
