package me.th.system.auth.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class AuthorityDto implements GrantedAuthority {

    private static final long serialVersionUID = -8193211321287628063L;
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
