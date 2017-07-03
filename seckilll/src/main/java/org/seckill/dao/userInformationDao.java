package org.seckill.dao;

import org.apache.ibatis.annotations.Param;

public interface userInformationDao {
	//存储用户信息
		int saveUserInformation(@Param("userphone")long userphone, @Param("password")long password, @Param("vocation")String vocation,@Param("favorate") String favorate);
}
