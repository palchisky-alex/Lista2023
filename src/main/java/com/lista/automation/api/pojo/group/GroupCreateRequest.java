package com.lista.automation.api.pojo.group;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupCreateRequest {

    private int client;
    private String name;
}
