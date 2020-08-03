package com.thinkgem.jeesite.modules.oa.service;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.oa.dao.LeaveDao;
import com.thinkgem.jeesite.modules.oa.entity.Leave;

/**
 * 调整请假内容处理器
 * @author liuj
 */
@Service
@Transactional
public class LeaveModifyProcessor implements TaskListener {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private LeaveDao leaveDao;
	@Autowired
	private RuntimeService runtimeService;

	public void notify(DelegateTask delegateTask) {
		// 这里可以指定个人任务的办理人，也可以指定组任务的办理人，它通过类去查询数据库，将下一个任务的办理人获取
		// 然后通过setAssignee()的方法来指定任务的办理人
		String processInstanceId = delegateTask.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery() 			// 创建一个流程实例查询
				.processInstanceId(processInstanceId) 	// 通过流程实例id查询
				.singleResult();						// 返回一个唯一的结果
		Leave leave = new Leave(processInstance.getBusinessKey());
		leave.setLeaveType((String) delegateTask.getVariable("leaveType"));	// 请假类型
		leave.setStartTime((Date) delegateTask.getVariable("startTime"));	// 开始时间
		leave.setEndTime((Date) delegateTask.getVariable("endTime"));		// 结束时间
		leave.setReason((String) delegateTask.getVariable("reason"));		// 请假原因
		leave.preUpdate();
		leaveDao.update(leave); // 更新请假信息
	}

}
