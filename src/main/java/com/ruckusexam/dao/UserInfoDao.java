package com.ruckusexam.dao;

import com.ruckusexam.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author csu
 */
@Mapper
public interface UserInfoDao {

    boolean insert(UserInfo userInfo);

    boolean updateById(UserInfo userInfo);

    UserInfo queryById(Integer id);

    UserInfo queryByUserId(String userId);

    List<UserInfo> queryUserInfo();

}