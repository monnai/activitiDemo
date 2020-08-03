package com.thinkgem.jeesite.modules.oa.utils;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@SuppressWarnings("serial")
public class GroupTaskListenerImpl implements TaskListener {

	/**
	 * 通过类指定组任务的办理人
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		// 这里可以指定个人任务的办理人，也可以指定组任务的办理人，它通过类去查询数据库，将下一个任务的办理人获取
		// 然后通过setAssignee()的方法来指定任务的办理人
		// delegateTask.setAssignee("张飞");
		
		// 组任务
		delegateTask.addCandidateUser("黄忠");
		delegateTask.addCandidateUser("马超");
	}

}
