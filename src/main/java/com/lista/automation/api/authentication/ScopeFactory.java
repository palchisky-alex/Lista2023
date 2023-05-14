package com.lista.automation.api.authentication;

import net.bytebuddy.implementation.bytecode.Throw;

import java.util.Arrays;

import static com.lista.automation.api.Properties.getProp;

/**
 * Created by Palchitsky Alex
 */
public class ScopeFactory {

    public static AuthPojo getSpecFor(Scope scope) {
        switch (scope) {
            case ADMIN:
                return new AuthPojo(getProp().timeZone(), getProp().userNameAdmin(), getProp().userPassAdmin());
            case JUNIOR:
                return new AuthPojo(getProp().timeZone(), getProp().userNameJunior(), getProp().userPassJunior());
            case READONLY:
                return new AuthPojo(getProp().timeZone(), getProp().userNameReadonly(), getProp().userPassReadonly());
            default:
                throw new IllegalArgumentException("Not valid scope. Pick a scope from " + Arrays.toString(Scope.values()));
        }
    }
}
