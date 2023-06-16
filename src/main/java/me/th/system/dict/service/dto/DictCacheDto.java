package me.th.system.dict.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DictCacheDto implements Serializable {

    private static final long serialVersionUID = -1760066838510724525L;

    private String name;

    private String key;

    private String value;
}
