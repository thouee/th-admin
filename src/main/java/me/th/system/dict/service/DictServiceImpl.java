package me.th.system.dict.service;

import lombok.RequiredArgsConstructor;
import me.th.system.dict.entity.Dict;
import me.th.system.dict.repository.DictRepository;
import me.th.system.dict.service.dto.DictCacheDto;
import me.th.system.dict.service.mapstruct.DictCacheMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final DictRepository dictRepository;

    private final DictCacheMapper dictCacheMapper;

    @Override
    public DictCacheDto getDict4Cache(String key, String value) {
        Dict dict = dictRepository.findByKeyAndValue(key, value);
        return dictCacheMapper.toDto(dict);
    }
}
