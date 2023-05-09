package com.lista.automation.api;

import com.lista.automation.api.utils.Prop;
import org.aeonbits.owner.ConfigFactory;

public class Properties {


    public static Prop getProp() {
        return ConfigFactory.create(Prop.class);
    }
}
