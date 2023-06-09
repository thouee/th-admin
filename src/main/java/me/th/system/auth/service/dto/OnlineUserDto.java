package me.th.system.auth.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OnlineUserDto {

    private String username;

    private String nickname;

    private String browser;

    private String ip;

    private String address;

    private String key;

    private LocalDateTime loginTime;
}
