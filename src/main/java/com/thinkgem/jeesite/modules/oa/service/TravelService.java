/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.oa.dao.TravelDao;
import com.thinkgem.jeesite.modules.oa.entity.Travel;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 出差申请流程Service
 * @author Guo
 * @version 2018-09-27
 */
@Service
@Transactional(readOnly = true)
public class TravelService extends CrudService<TravelDao, Travel> {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private RepositoryService repositoryService;
	
	public Travel get(String id) {
		return super.get(id);
	}
	
	public List<Travel> findList(Travel travel) {
		return super.findList(travel);
	}
	
	public Page<Travel> findPage(Page<Travel> page, Travel travel) {
		return super.findPage(page, travel);
	}
	
	@Transactional(readOnly = false)
	public void save(Travel travel) {
		if (travel.getIsNewRecord()){
			travel.preInsert();
			dao.insert(travel);
			// 启动流程实例
			// 1.获取流程定义的key
			String key = travel.getClass().getSimpleName();
			// 2.关联业务，使用字符串（格式：Travel:id的形式），通过设置，让启动的流程（流程实例）关联业务
			// 	   使用正在执行对象表中的一个字段BUSINESS_KEY（Activiti提供的一个字段），让启动的流程（流程实例）关联业务
			String businessKey = key+":"+travel.getId();
			
			// 将任务标题保存到流程变量
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("title", "出差申请—"+UserUtils.getUser().getName());
			
			// 3.启动流程实例
			runtimeService.startProcessInstanceByKey(key, businessKey, vars);
		}else{
			// 重新编辑申请
			travel.preUpdate();
			dao.update(travel);
		}
	}
	

	/**
	 * 根据登录的用户名获取任务
	 */
	public List<Task> findTaskByName(String name){
		List<Task> list = taskService.createTaskQuery().taskAssignee(name).orderByTaskCreateTime().asc().list();		
		return list;
	}
	
	/**
	 * 使用任务ID，获取当前任务节点中对应的Form key中的连接的值
	 */
	public String getTaskFormKeyByTaskId(String taskId){
		TaskFormData taskFormData = formService.getTaskFormData(taskId);	 
		return taskFormData.getFormKey();
	}
	
	/**
	 * 通过任务id获取Travel（出差申请数据）
	 * @param taskId
	 * @return
	 */
	public Travel getTravelByTaskId(String taskId){
		// 1.通过任务id获取任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult(); 
		
		// 2.通过任务对象获取流程实例
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		
		// 3.通过流程实例id查询act_ru_execution（执行对象表）获取BUSINESS_KEY
		String businessKey = processInstance.getBusinessKey();
		
		// 4.通过businessKey中的Travel.id获取Travel（出差申请数据）
		String travelId = businessKey.split(":")[1];
		Travel travel = get(travelId);	
		return travel;
	}
	
	/**
	 * 获取任务节点下一个连线名称
	 * ProcessDefinition和ProcessDefinitionEntity
	 * ProcessDefinition：对应的是act_re_procdef（流程定义表）的数据
	 * ProcessDefinitionEntity：对应的是流程定义相关的 bpmn文件的数据
	 */
	public List<String> findFlowNameByTaskId(String taskId){
		// 1.通过任务id获取任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		// 2.通过任务对象获取流程定义id
		String processDefinitionId = task.getProcessDefinitionId();
		
		// 3.查询ProcessDefinitionEntity对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId);
		
		// 4.获取流程实例
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
		
		// 5.获取当前活动完成之后连线的名称
		// 获取当前活动的id（bpmn文件中UserTask的id）
		String activityId = processInstance.getActivityId();
		// 通过活动id获取活动信息
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		
		List<String> list = Lists.newArrayList();
		for(PvmTransition pvm:pvmTransitionList){
			String name = (String)pvm.getProperty("name"); // 获取name属性的值
			list.add(name);
		}		
		return list;
	}
	
	/**
	 * 通过任务id查询当前任务的历史批注信息
	 * @param taskId
	 * @return
	 */
	public List<Comment> findCommentByTaskId(String taskId){
		List<Comment> list = Lists.newArrayList();
		// 1.通过任务id获取任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 2.通过任务对象获取流程实例id
		String processInstanceId = task.getProcessInstanceId();
		// 3.通过流程实例id获取历史批注信息
		list = taskService.getProcessInstanceComments(processInstanceId);
		return null;
	}
	
	@Transactional(readOnly = false)
	public void delete(Travel travel) {
		super.delete(travel);
	}
	
}