/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.test.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.test.entity.SysUser;

/**
 * 用户管理DAO接口
 * @author 郭景汇总
 * @version 2018-03-15
 */
@MyBatisDao
public interface SysUserDao extends CrudDao<SysUser> {
	
}