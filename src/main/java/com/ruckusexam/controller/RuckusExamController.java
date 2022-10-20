package com.ruckusexam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruckusexam.entity.CommentInfo;
import com.ruckusexam.entity.TicketInfo;
import com.ruckusexam.entity.UserInfo;
import com.ruckusexam.entity.enums.Operation;
import com.ruckusexam.entity.enums.Role;
import com.ruckusexam.entity.enums.TicketStatus;
import com.ruckusexam.entity.enums.TicketType;
import com.ruckusexam.service.RuckusExamService;
import com.ruckusexam.utils.PermissionUtil;
import com.ruckusexam.utils.UsernameUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author csu
 */
@RestController
@RequestMapping("/RuckusExam")
public class RuckusExamController {

    private static Logger LOGGER = LoggerFactory.getLogger(Slf4j.class);
    String className = this.getClass().getSimpleName();

    @Autowired
    RuckusExamService ruckusExamImpl;

    /**************************************************************************
     登入(login), 登出(logout)
     *************************************************************************/

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ObjectNode login(@RequestParam("userId") String userId,
                            @RequestParam("userPassword") String userPassword,
                            HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        boolean success = false;
        String errMsg = "";
        try {
            if (!UsernameUtil.isValidUsername(userId)) {
                throw new RuntimeException("The userId is invalid.");
            }
            userId = UsernameUtil.getUsername(userId); // 轉小寫

            boolean isLogin = false;
            if (session.getAttribute("isLogin") != null) {
                isLogin = (boolean) session.getAttribute("isLogin");
            }
            if (isLogin == false) { // 只有isLogin=false才進入登入程序!! 已登入者必須先登出再登入!!
                UserInfo ui = ruckusExamImpl.login(userId, userPassword);
                if (ui != null) {
                    session.setAttribute("isLogin", true);
                    session.setAttribute("userId", ui.getUserId());
                    session.setAttribute("userName", ui.getUserName());
                    session.setAttribute("role", ui.getUserRole());
                }
            }
            success = true;
        } catch (Exception e) {
            success = false;
            errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "login", e.getMessage());
        } finally {
            jObj.put("success", success);
            jObj.put("errMsg", errMsg);
            jObj.put("isLogin", (Boolean) session.getAttribute("isLogin"));
            jObj.put("userId", (String) session.getAttribute("userId"));
            jObj.put("userName", (String) session.getAttribute("userName"));
            jObj.put("role", (String) session.getAttribute("role"));
            return jObj;
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ObjectNode login(HttpSession session) {

        session.setAttribute("isLogin", false);
        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("role");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        jObj.put("isLogin", (boolean) session.getAttribute("isLogin"));
        return jObj;
    }

    /**************************************************************************
     建立使用者 (Administrator Only)
     *************************************************************************/

    @RequestMapping(value = "createUser", method = RequestMethod.POST)
    public ObjectNode createUser(@RequestParam("userId") String userId,
                                 @RequestParam("userName") String userName,
                                 @RequestParam("userRole") String userRole,
                                 @RequestParam("userPassword") String userPassword,
                                 HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        boolean success = false;
        String errMsg = "";
        try {
            String role = (String) session.getAttribute("role");
            if (!PermissionUtil.getAddUserPermissionCheck(role)) {
                throw new RuntimeException("You don't have permission.");
            }
            if (!UsernameUtil.isValidUsername(userId)) {
                throw new RuntimeException("The userId is invalid.");
            }
            userId = UsernameUtil.getUsername(userId); // 轉小寫
            if (ruckusExamImpl.queryByUserId(userId) != null) {
                throw new RuntimeException("The userId is existing.");
            }
            int id = -1;
            int isDel = 0;
            Timestamp updateTime = new Timestamp(System.currentTimeMillis());
            UserInfo ui = new UserInfo(id, userId, userName, userRole, userPassword, isDel, updateTime);
            success = ruckusExamImpl.createUser(ui);
        } catch (Exception e) {
            success = false;
            errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "createUser", e.getMessage());
        } finally {
            jObj.put("success", success);
            jObj.put("errMsg", errMsg);
            return jObj;
        }
    }

    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public ObjectNode updateUser(@RequestParam("id") Integer id,
                                 @RequestParam("userId") String userId,
                                 @RequestParam("userName") String userName,
                                 @RequestParam("userRole") String userRole,
                                 @RequestParam("userPassword") String userPassword,
                                 @RequestParam("isDel") Integer isDel,
                                 HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        boolean success = false;
        String errMsg = "";
        try {
            String role = (String) session.getAttribute("role");
            if (!PermissionUtil.getAddUserPermissionCheck(role)) {
                throw new RuntimeException("You don't have permission.");
            }
            userId = UsernameUtil.getUsername(userId); // 轉小寫
            UserInfo ui = ruckusExamImpl.queryById(id);
            if (ui == null) {
                throw new RuntimeException("The id is not existing.");
            }
            if (!ui.getUserId().equals(userId)) {
                throw new RuntimeException("The userId cannot be changed.");
            }
            Timestamp updateTime = new Timestamp(System.currentTimeMillis());
            ui = new UserInfo(id, userId, userName, userRole, userPassword, isDel, updateTime);
            success = ruckusExamImpl.updateUser(ui);
        } catch (Exception e) {
            success = false;
            errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "updateUser", e.getMessage());
        } finally {
            jObj.put("success", success);
            jObj.put("errMsg", errMsg);
            return jObj;
        }
    }

    @RequestMapping(value = "queryUserInfo", method = RequestMethod.POST)
    public ObjectNode queryUserInfo(HttpSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        boolean success = false;
        String errMsg = "";
        List<UserInfo> resultList = new ArrayList<UserInfo>();
        String userInfolist = "[]";
        try {
            String role = (String) session.getAttribute("role");
            if (!PermissionUtil.getAddUserPermissionCheck(role)) {
                throw new RuntimeException("You don't have permission.");
            }
            resultList = ruckusExamImpl.queryUserInfo();
            success = true;
            if (resultList != null) {
                userInfolist = objectMapper.writeValueAsString(resultList);
            }
        } catch (Exception e) {
            success = false;
            errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "queryUserInfo", e.getMessage());
        } finally {
            jObj.put("success", success);
            jObj.put("errMsg", errMsg);
            jObj.put("userInfolist", userInfolist);
            return jObj;
        }
    }

    /**************************************************************************
     看Ticket報表
     *************************************************************************/

    @RequestMapping(value = "queryTicketInfo", method = RequestMethod.POST)
    public ObjectNode queryTicketInfo(HttpSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        boolean success = false;
        String errMsg = "";
        List<TicketInfo> resultList = new ArrayList<TicketInfo>();
        String ticketInfoList = "[]";
        try {
            boolean isLogin = (Boolean) session.getAttribute("isLogin");
            if (!isLogin) {
                throw new RuntimeException("Please login first.");
            }
            resultList = ruckusExamImpl.queryTicketInfo();
            success = true;
            if (resultList != null) {
                ticketInfoList = objectMapper.writeValueAsString(resultList);
            }
        } catch (Exception e) {
            success = false;
            errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "queryTicketInfo", e.getMessage());
        } finally {
            jObj.put("success", success);
            jObj.put("errMsg", errMsg);
            jObj.put("ticketInfoList", ticketInfoList);
            return jObj;
        }
    }

    /**************************************************************************
     新增一個Ticket
     *************************************************************************/

    @RequestMapping(value = "createTicket", method = RequestMethod.POST)
    public ObjectNode createTicket(@RequestParam("ticketType") String ticketType,
                                   @RequestParam("ticketContent") String ticketContent,
                                   HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        boolean success = false;
        String errMsg = "";
        try {
            String userId = (String) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            Role r = Role.valueOf(role);
            Operation op = Operation.Create;
            TicketType tt = TicketType.valueOf(ticketType);
            TicketStatus ts = TicketStatus.New;
            boolean permission = PermissionUtil.getPermissionCheck(r, op, tt);
            if (!permission) {
                throw new RuntimeException("You don't have permission.");
            }
            int id = -1;
            int ticketId = -1;
            int severityLevel = 3;
            String severityUser = "Default";
            int priorityLevel = 3;
            String priorityUser = "Default";
            Timestamp updateTime = new Timestamp(System.currentTimeMillis());
            TicketInfo ti = new TicketInfo(id, ticketId, ticketType, ticketContent, ts.toString(),
                    severityLevel, severityUser, priorityLevel, priorityUser,
                    userId, updateTime);
            success = ruckusExamImpl.insertNewTicket(ti);
        } catch (Exception e) {
            success = false;
            errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "createTicket", e.getMessage());
        } finally {
            jObj.put("success", success);
            jObj.put("errMsg", errMsg);
            return jObj;
        }
    }

    /**************************************************************************
     選定一個Ticket
     *************************************************************************/

    @RequestMapping(value = "queryTicketHistoryByTicketId", method = RequestMethod.POST)
    public ObjectNode queryTicketInfo(@RequestParam("ticketId") Integer ticketId,
                                      HttpSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        boolean success = false;
        String errMsg = "";
        List<TicketInfo> resultList = new ArrayList<TicketInfo>();
        String ticketInfoList = "[]";
        String operationList = "[]";
        try {
            boolean isLogin = (Boolean) session.getAttribute("isLogin");
            if (!isLogin) {
                throw new RuntimeException("Please login first.");
            }
            resultList = ruckusExamImpl.queryTicketHistoryByTicketId(ticketId);
            success = true;
            if (resultList != null) {
                ticketInfoList = objectMapper.writeValueAsString(resultList);
                operationList = objectMapper.writeValueAsString(PermissionUtil.getPossibleTicketOperation(TicketStatus.valueOf(resultList.get(0).getTicketStatus())));
            }
        } catch (Exception e) {
            success = false;
            errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "queryTicketHistoryByTicketId", e.getMessage());
        } finally {
            jObj.put("success", success);
            jObj.put("errMsg", errMsg);
            jObj.put("ticketInfoList", ticketInfoList);
            jObj.put("operationList", operationList);
            return jObj;
        }
    }

    @RequestMapping(value = "queryCommentInfo", method = RequestMethod.POST)
    public ObjectNode queryCommentInfo(@RequestParam("ticketId") Integer ticketId,
                                       HttpSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        boolean success = false;
        String errMsg = "";
        List<CommentInfo> resultList = new ArrayList<CommentInfo>();
        String commentInfoList = "[]";
        try {
            boolean isLogin = (Boolean) session.getAttribute("isLogin");
            if (!isLogin) {
                throw new RuntimeException("Please login first.");
            }
            resultList = ruckusExamImpl.queryCommentInfo(ticketId);
            success = true;
            if (resultList != null) {
                commentInfoList = objectMapper.writeValueAsString(resultList);
            }
        } catch (Exception e) {
            success = false;
            errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "queryCommentInfo", e.getMessage());
        } finally {
            jObj.put("success", success);
            jObj.put("errMsg", errMsg);
            jObj.put("commentInfoList", commentInfoList);
            return jObj;
        }
    }

    /**************************************************************************
     更新一個Ticket
     *************************************************************************/

    @RequestMapping(value = "updateTicket", method = RequestMethod.POST)
    public ObjectNode updateTicket(@RequestParam("ticketId") Integer ticketId,
                                   @RequestParam("ticketContent") String ticketContent,
                                   @RequestParam("severityLevel") Integer severityLevel,
                                   @RequestParam("priorityLevel") Integer priorityLevel,
                                   @RequestParam("operation") String operation,
                                   HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jObj = objectMapper.createObjectNode();
        boolean success = false;
        String errMsg = "";
        try {
            boolean isLogin = (Boolean) session.getAttribute("isLogin");
            if (!isLogin) {
                throw new RuntimeException("Please login first.");
            }

            // confirm TicketInfo
            TicketInfo ti = null;
            TicketType tt = null;
            TicketStatus tsCurrent = null;
            List<TicketInfo> resultList = ruckusExamImpl.queryTicketHistoryByTicketId(ticketId);
            if (resultList.size() > 0) {
                ti = resultList.get(0); // the latest
            } else {
                throw new RuntimeException("The ticketId is invalid.");
            }
            tt = TicketType.valueOf(ti.getTicketType());
            tsCurrent = TicketStatus.valueOf(ti.getTicketStatus());

            // check privilege
            String userId = (String) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");
            Role r = Role.valueOf(role);
            try {
                // owner checking
                Operation op = Operation.valueOf(operation);
                boolean permission = PermissionUtil.getPermissionCheck(r, op, tt);
                if (!permission) {
                    throw new RuntimeException("You don't have permission.");
                }

                // status checking
                TicketStatus tsNext = PermissionUtil.getOperation2Status(tsCurrent, op);
                permission = PermissionUtil.getTicketStatusChangeCheck(tsCurrent, tsNext);
                if (!permission) {
                    throw new RuntimeException("The ticket status change is illegal");
                }

                // PASS the examination
                ti.setTicketContent(ticketContent);
                ti.setUpdateUser(userId);
                ti.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                if((ti.getSeverityLevel() != PermissionUtil.getSeverity(severityLevel))) {
                    ti.setSeverityLevel(severityLevel);
                    ti.setSeverityUser(userId);
                }
                if((ti.getPriorityLevel() != PermissionUtil.getPriority(priorityLevel))) {
                    ti.setPriorityLevel(priorityLevel);
                    ti.setPriorityUser(userId);
                }
                if(op.equals(Operation.Edit)) {
                    success = ruckusExamImpl.updateTicketById(ti);
                } else {
                    ti.setTicketStatus(tsNext.toString());
                    success = ruckusExamImpl.insertTicket(ti);
                }

            } catch (Exception e) {
                // just update severityLevel or priorityLevel (everyone)
                boolean needChange = false;
                if((ti.getSeverityLevel() != PermissionUtil.getSeverity(severityLevel))) {
                    needChange = true;
                    ti.setSeverityLevel(severityLevel);
                    ti.setSeverityUser(userId);
                }
                if((ti.getPriorityLevel() != PermissionUtil.getPriority(priorityLevel))) {
                    needChange = true;
                    ti.setPriorityLevel(priorityLevel);
                    ti.setPriorityUser(userId);
                }
                if(needChange) {
                    success = ruckusExamImpl.updateTicketById(ti);
                }
            }
        } catch (Exception e) {
            success = false;
            errMsg = String.format("Class: %s, Func: %s, Msg: %s", className, "updateTicket", e.getMessage());
        } finally {
            jObj.put("success", success);
            jObj.put("errMsg", errMsg);
            return jObj;
        }
    }

    /**************************************************************************
     評論一個Ticket
     *************************************************************************/
}
