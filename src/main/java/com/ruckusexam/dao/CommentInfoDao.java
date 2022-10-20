package com.ruckusexam.dao;

import com.ruckusexam.entity.CommentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author csu
 */
@Mapper
public interface CommentInfoDao {

    boolean insert(CommentInfo commentInfo);

    List<CommentInfo> queryCommentInfo(@Param(value = "ticketId") Integer ticketId);

}