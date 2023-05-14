package com.lista.automation.api.authentication;


import lombok.Getter;

/**
 * Created by Palchitsky Alex
 */


public enum Scope {
    ADMIN(1),
    JUNIOR(2),
    READONLY(3);

    Scope(int value) {
        this.value = value;
    }

    @Getter
    private int value;
}
