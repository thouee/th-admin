package me.th.share.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AppProperties {

    @Value("${ip.local-parse:true}")
    public static Boolean localParse;
}
