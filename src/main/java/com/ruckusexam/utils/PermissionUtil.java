package com.ruckusexam.utils;

import com.ruckusexam.entity.enums.Operation;
import com.ruckusexam.entity.enums.Role;
import com.ruckusexam.entity.enums.TicketStatus;
import com.ruckusexam.entity.enums.TicketType;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    public static boolean getPermissionCheck(Role role, Operation operation, TicketType tt) {
        if (role == Role.Administrator) {
            return true;
        }
        if (role == Role.PM) {
            // TicketType.Feature_Request
            if (operation == Operation.Create && tt == TicketType.Feature_Request) {
                return true;
            }
            if (operation == Operation.Edit && tt == TicketType.Feature_Request) {
                return true;
            }
            if (operation == Operation.Close && tt == TicketType.Feature_Request) {
                return true;
            }
            if (operation == Operation.ReOpen && tt == TicketType.Feature_Request) {
                return true;
            }
        }
        if (role == Role.RD) {
            // TicketType.Feature_Request
            if (operation == Operation.Resolve && tt == TicketType.Feature_Request) {
                return true;
            }
            // TicketType.Bug
            if (operation == Operation.Resolve && tt == TicketType.Bug) {
                return true;
            }
        }
        if (role == Role.QA) {
            // TicketType.Bug
            if (operation == Operation.Create && tt == TicketType.Bug) {
                return true;
            }
            if (operation == Operation.Edit && tt == TicketType.Bug) {
                return true;
            }
            if (operation == Operation.Close && tt == TicketType.Bug) {
                return true;
            }
            if (operation == Operation.ReOpen && tt == TicketType.Bug) {
                return true;
            }
            // TicketType.Test_Case
            if (operation == Operation.Create && tt == TicketType.Test_Case) {
                return true;
            }
            if (operation == Operation.Edit && tt == TicketType.Test_Case) {
                return true;
            }
            if (operation == Operation.Resolve && tt == TicketType.Test_Case) {
                return true;
            }
            if (operation == Operation.Close && tt == TicketType.Test_Case) {
                return true;
            }
            if (operation == Operation.ReOpen && tt == TicketType.Test_Case) {
                return true;
            }
        }
        return false;
    }

    public static boolean getTicketStatusChangeCheck(TicketStatus now, TicketStatus next) {
        if ((now.equals(TicketStatus.New) && next.equals(TicketStatus.Resolved))
                || (now.equals(TicketStatus.Resolved) && next.equals(TicketStatus.Closed))
                || (now.equals(TicketStatus.Resolved) && next.equals(TicketStatus.ReOpened))
                || (now.equals(TicketStatus.Closed) && next.equals(TicketStatus.ReOpened))
                || (now.equals(TicketStatus.ReOpened) && next.equals(TicketStatus.Resolved))) {
            return true;
        }
        return false;
    }

    public static List<String> getPossibleTicketOperation(TicketStatus now) {
        List<String> resultList = new ArrayList<String>();
        if (now.equals(TicketStatus.New)) {
            resultList.add(Operation.Edit.name());
            resultList.add(Operation.Resolve.name());
        } else if (now.equals(TicketStatus.Resolved)) {
            resultList.add(Operation.Close.name());
            resultList.add(Operation.ReOpen.name());
        } else if (now.equals(TicketStatus.Closed)) {
            resultList.add(Operation.ReOpen.name());
        } else if (now.equals(TicketStatus.ReOpened)) {
            resultList.add(Operation.Resolve.name());
        }
        return resultList;
    }

    public static TicketStatus getOperation2Status(TicketStatus now, Operation operation) {
        if (operation.equals(Operation.Edit)) {
            return now;
        } else if (operation.equals(Operation.Resolve)) {
            return TicketStatus.Resolved;
        } else if (operation.equals(Operation.Close)) {
            return TicketStatus.Closed;
        } else if (operation.equals(Operation.ReOpen)) {
            return TicketStatus.ReOpened;
        }
        return null;
    }

    public static boolean getAddUserPermissionCheck(String role) {
        if ("Administrator".equals(role)) {
            return true;
        }
        return false;
    }

    public static int getSeverity(int severity) {
        if (severity >= 1 && severity <= 5) {
            return severity;
        }
        return 0;
    }

    public static int getPriority(int priority) {
        if (priority >= 1 && priority <= 5) {
            return priority;
        }
        return 0;
    }

}
