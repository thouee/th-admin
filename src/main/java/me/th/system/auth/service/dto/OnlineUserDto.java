package me.th.system.auth.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class OnlineUserDto implements Serializable {

    private static final long serialVersionUID = -1L;

    private String username;

    private String nickname;

    private String browser;

    private String ip;

    private String address;

    private String key;

    private LocalDateTime loginTime;
}
