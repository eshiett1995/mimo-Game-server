package com.gambeat.mimo.server.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class Default implements Serializable {
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
