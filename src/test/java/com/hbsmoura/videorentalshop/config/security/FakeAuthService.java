package com.hbsmoura.videorentalshop.config.security;

import java.util.UUID;

public class FakeAuthService {

    public boolean isItself(UUID id) {
        return true;
    }
}
