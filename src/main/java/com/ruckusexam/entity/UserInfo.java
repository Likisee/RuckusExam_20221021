package com.ruckusexam.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

/**
 * @author csu
 */
@Data
@Builder
public class UserInfo implements Serializable {

    private int id;
    private String userId;
    private String userName;
    private String userRole;
    private String userPassword;
    private int isDel;
    private Timestamp updateTime;

    public UserInfo() {
    }

    public UserInfo(int id, String userId, String userName, String userRole, String userPassword, int isDel, Timestamp updateTime) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.userPassword = userPassword;
        this.isDel = isDel;
        this.updateTime = updateTime;
    }

}
