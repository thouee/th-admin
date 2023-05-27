package me.th.share.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Value("${ip.local-parse:true}")
    public static Boolean localParse;
}
