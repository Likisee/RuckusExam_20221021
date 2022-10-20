package com.ruckusexam.dao;

import com.ruckusexam.entity.TicketInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author csu
 */
@Mapper
public interface TicketInfoDao {

    boolean insertSimple(TicketInfo ticketInfo);

    boolean insert(TicketInfo ticketInfo);

    boolean updateById(TicketInfo ticketInfo);

    List<TicketInfo> queryByTicketId(Integer ticketId);

    List<TicketInfo> queryTicketInfo();

}