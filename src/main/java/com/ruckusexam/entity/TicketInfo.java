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
public class TicketInfo implements Serializable {

    private int id;
    private int ticketId;
    private String ticketType;
    private String ticketContent;
    private String ticketStatus;
    private int severityLevel;
    private String severityUser;
    private int priorityLevel;
    private String priorityUser;
    private String updateUser;
    private Timestamp updateTime;

    public TicketInfo() {
    }

    public TicketInfo(int id, int ticketId, String ticketType, String ticketContent, String ticketStatus,
                      int severityLevel, String severityUser, int priorityLevel, String priorityUser,
                      String updateUser, Timestamp updateTime) {
        this.id = id;
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.ticketContent = ticketContent;
        this.ticketStatus = ticketStatus;

        this.severityLevel = severityLevel;
        this.severityUser = severityUser;
        this.priorityLevel = priorityLevel;
        this.priorityUser = priorityUser;

        this.updateUser = updateUser;
        this.updateTime = updateTime;
    }

}
