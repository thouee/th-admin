package me.th.system.dict.service;

import me.th.system.dict.service.dto.DictCacheDto;

public interface DictService {

    /**
     * 查询用作缓存
     *
     * @param key   -
     * @param value -
     * @return DictCacheDto
     */
    DictCacheDto getDict4Cache(String key, String value);
}
