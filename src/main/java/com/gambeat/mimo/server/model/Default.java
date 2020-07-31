package com.gambeat.mimo.server.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;

public class Default implements Serializable {
    @Id
    private String id;

//    @CreatedDate
//    private DateTime created;
//
//    @LastModifiedDate
//    private DateTime modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
