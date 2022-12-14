package com.ruckusexam.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author csu
 */
@Data
@Builder
public class CommentInfo implements Serializable {

    private int id;
    private Integer outerTicketId;
    private String commentContent;
    private String updateUser;
    private Timestamp updateTime;

    public CommentInfo() {
    }

    public CommentInfo(int id, Integer outerTicketId, String commentContent, String updateUser, Timestamp updateTime) {
        this.id = id;
        this.outerTicketId = outerTicketId;
        this.commentContent = commentContent;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
    }

}
