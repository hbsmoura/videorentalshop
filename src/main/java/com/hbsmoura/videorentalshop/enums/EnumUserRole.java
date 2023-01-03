package com.hbsmoura.videorentalshop.enums;

import org.springframework.security.core.GrantedAuthority;

public enum EnumUserRole implements GrantedAuthority {
    ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
