package com.ruckusexam.service;

import com.ruckusexam.entity.CommentInfo;
import com.ruckusexam.entity.TicketInfo;
import com.ruckusexam.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author csu
 */
public interface RuckusExamService {

    // 登入(login)
    UserInfo login(String userId, String userPassword);

    // 建立使用者 (Administrator Only)
    boolean createUser(UserInfo userInfo);

    boolean updateUser(UserInfo userInfo);

    UserInfo queryById(Integer id);

    UserInfo queryByUserId(String userId);

    List<UserInfo> queryUserInfo();

    // 看Ticket報表
    List<TicketInfo> queryTicketInfo();

    // 新增一個Ticket
    boolean insertNewTicket(TicketInfo ticketInfo);

    // 選定一個Ticket
    List<TicketInfo> queryTicketHistoryByTicketId(Integer ticketId);

    List<CommentInfo> queryCommentInfo(@Param(value = "ticketId") Integer ticketId);

    // 更新一個Ticket
    boolean insertTicket(TicketInfo ticketInfo);
    boolean updateTicketById(TicketInfo ticketInfo);

    // 評論一個Ticket
    boolean insertComment(CommentInfo commentInfo);

}
