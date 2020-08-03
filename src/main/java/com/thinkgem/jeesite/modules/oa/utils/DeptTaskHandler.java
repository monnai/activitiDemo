package com.thinkgem.jeesite.modules.oa.utils;

import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 部门领导任务分配
 */
@SuppressWarnings("serial")
public class DeptTaskHandler implements TaskListener {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// 获取当前登录的用户，在获取用户对应的部门领导
		User user = UserUtils.getUser();

		User deptLeader = new User(); // 保存当前用户部门Leader
		// 获取当前用户部门下的所有用户，查找出当前部门的Leader
		List<User> userList = userDao.findUserByOfficeId(user);
		for(int i=0;i<userList.size();i++){
			Role role = new Role();
			role.getSqlMap().put("dsf", BaseService.dataScopeFilter(userList.get(i), "o", "u"));
			List<Role> roleList = roleDao.findList(role);
			userList.get(i).setRoleList(roleList);
			// 遍历用户的角色，判断是否是“dept_leader”（部门领导）
			for(Role r:roleList){
				if("dept_leader".equals(r.getEnname())){
					deptLeader = userList.get(i);
					// 设置个人任务的办理人
					delegateTask.setAssignee(deptLeader.getLoginName());
				}
			}
		}			
	}

}
