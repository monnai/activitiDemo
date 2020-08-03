/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.Travel;

/**
 * 出差申请流程DAO接口
 * @author Guo
 * @version 2018-09-27
 */
@MyBatisDao
public interface TravelDao extends CrudDao<Travel> {
	
}