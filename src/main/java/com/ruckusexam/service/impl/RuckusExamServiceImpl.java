package com.ruckusexam.service.impl;

import com.ruckusexam.dao.CommentInfoDao;
import com.ruckusexam.dao.TicketInfoDao;
import com.ruckusexam.dao.UserInfoDao;
import com.ruckusexam.entity.CommentInfo;
import com.ruckusexam.entity.TicketInfo;
import com.ruckusexam.entity.UserInfo;
import com.ruckusexam.service.RuckusExamService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author csu
 */
@Service
@Slf4j
public class RuckusExamServiceImpl implements RuckusExamService {

    private static Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);
    String className = this.getClass().getSimpleName();

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    TicketInfoDao ticketInfoDao;

    @Autowired
    CommentInfoDao commentInfoDao;

    @Autowired
    RedisTemplate redisTemplate;

    /**************************************************************************
     登入(login)
     *************************************************************************/

    @Override
    public UserInfo login(String userId, String userPassword) {
        UserInfo result = null;
        try {
            UserInfo ui = userInfoDao.queryByUserId(userId);
            if (ui != null
                    && ui.getUserId().equals(userId) // userId已經確定是小寫
                    && ui.getUserPassword().equals(userPassword)
                    && ui.getIsDel() == 0) {
                result = ui;
            }
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "login", e.getMessage());
            LOGGER.error(errMsg);
            result = null;
        } finally {
            return result;
        }
    }

    /**************************************************************************
     建立使用者 (Administrator Only)
     *************************************************************************/

    @Override
    public boolean createUser(UserInfo userInfo) {
        boolean success = false;
        try {
            success = userInfoDao.insert(userInfo);
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "createUser", e.getMessage());
            LOGGER.error(errMsg);
            return false;
        } finally {
            return success;
        }
    }

    @Override
    public boolean updateUser(UserInfo userInfo) {
        boolean success = false;
        try {
            success = userInfoDao.updateById(userInfo);
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "updateUser", e.getMessage());
            LOGGER.error(errMsg);
            return false;
        } finally {
            return success;
        }
    }

    @Override
    public UserInfo queryById(Integer id) {
        return userInfoDao.queryById(id);
    }

    @Override
    public UserInfo queryByUserId(String userId) {
        return userInfoDao.queryByUserId(userId);
    }

    @Override
    public List<UserInfo> queryUserInfo() {
        List<UserInfo> resultList = new ArrayList<UserInfo>();
        try {
            resultList = userInfoDao.queryUserInfo();
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "queryUserInfo", e.getMessage());
            LOGGER.error(errMsg);
            return null;
        } finally {
            return resultList;
        }
    }

    /**************************************************************************
     看Ticket報表
     *************************************************************************/

    @Override
    public List<TicketInfo> queryTicketInfo() {
        List<TicketInfo> resultList = new ArrayList<TicketInfo>();
        try {
            resultList = ticketInfoDao.queryTicketInfo();
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "queryTicketInfo", e.getMessage());
            LOGGER.error(errMsg);
            return null;
        } finally {
            return resultList;
        }
    }

    /**************************************************************************
     新增一個Ticket
     *************************************************************************/

    @Override
    public boolean insertNewTicket(TicketInfo ticketInfo) {
        boolean success = false;
        try {
            success = ticketInfoDao.insertSimple(ticketInfo);
            if (success) {
                List<TicketInfo> tempVoList = ticketInfoDao.queryByTicketId(ticketInfo.getTicketId());
                success = false;
                for (TicketInfo ti : tempVoList) {
                    if (ti.getUpdateUser().equals(ticketInfo.getUpdateUser())) {
                        ticketInfo.setId(ti.getId());
                        ticketInfo.setTicketId(ti.getId());
                        success = ticketInfoDao.updateById(ticketInfo);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "insertTicket", e.getMessage());
            LOGGER.error(errMsg);
            return false;
        } finally {
            return success;
        }
    }

    /**************************************************************************
     選定一個Ticket
     *************************************************************************/

    @Override
    public List<TicketInfo> queryTicketHistoryByTicketId(Integer ticketId) {
        List<TicketInfo> resultList = new ArrayList<TicketInfo>();
        try {
            resultList = ticketInfoDao.queryByTicketId(ticketId);
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "queryTicketHistoryByTicketId", e.getMessage());
            LOGGER.error(errMsg);
            return null;
        } finally {
            return resultList;
        }
    }

    @Override
    public List<CommentInfo> queryCommentInfo(Integer ticketId) {
        List<CommentInfo> resultList = new ArrayList<CommentInfo>();
        try {
            resultList = commentInfoDao.queryCommentInfo(ticketId);
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "queryCommentInfo", e.getMessage());
            LOGGER.error(errMsg);
            return null;
        } finally {
            return resultList;
        }
    }

    /**************************************************************************
     更新一個Ticket
     *************************************************************************/

    @Override
    public boolean insertTicket(TicketInfo ticketInfo) {
        boolean success = false;
        try {
            success = ticketInfoDao.insert(ticketInfo);
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "insertTicket", e.getMessage());
            LOGGER.error(errMsg);
            return false;
        } finally {
            return success;
        }
    }

    @Override
    public boolean updateTicketById(TicketInfo ticketInfo) {
        boolean success = false;
        try {
            success = ticketInfoDao.updateById(ticketInfo);
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "updateTicketById", e.getMessage());
            LOGGER.error(errMsg);
            return false;
        } finally {
            return success;
        }
    }

    /**************************************************************************
     評論一個Ticket
     *************************************************************************/

    @Override
    public boolean insertComment(CommentInfo commentInfo) {
        boolean success = false;
        try {
            success = commentInfoDao.insert(commentInfo);
        } catch (Exception e) {
            String errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "insertComments", e.getMessage());
            LOGGER.error(errMsg);
            return false;
        } finally {
            return success;
        }
    }

}
