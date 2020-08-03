/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.oa.entity.Travel;
import com.thinkgem.jeesite.modules.oa.service.TravelService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 出差申请Controller
 * @author Guo
 * @version 2018-09-27
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/travel")
public class TravelController extends BaseController {

	@Autowired
	private TravelService travelService;

	/**
	 * 1.保存出差数据，提交审批并启动流程实例
	 * @param travel
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("oa:travel:edit")
	@RequestMapping(value = "save")
	public String save(Travel travel, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, travel)){
			return form(travel, model);
		}
		travelService.save(travel);
		addMessage(redirectAttributes, "保存出差申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/travel/?repage";
	}

	
	/**
	 * 2.查看任务，根据登录的用户名获取任务
	 * @param travel
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oa:travel:view")
	@RequestMapping(value = "findTask")
	public String findTask(Travel travel, HttpServletRequest request, HttpServletResponse response, Model model) {
		String loginName = UserUtils.getUser().getLoginName();
		List<Task> list = travelService.findTaskByName(loginName);
		model.addAttribute("list", list);
		return "modules/oa/travelTaskList";
	}

	
	/**
	 * 3.办理任务，跳转到办理任务表单
	 */
	@RequiresPermissions("oa:travel:view")
	@RequestMapping(value = "travelformAudit")
	public String travelformAudit(String taskId, Model model) {
		// 1.获取Travel（申请出差）数据
		Travel travel = travelService.getTravelByTaskId(taskId);				
		// 2.获取连线数据
		List<String> flowList = travelService.findFlowNameByTaskId(taskId);
		// 3.获取历史审核信息，帮助当前人审核
		// Activiti工作流表中存在一个审批记录表act_hi_comment，用来在完成任务的时候，添加审批记录
		List<Comment> commentList = travelService.findCommentByTaskId(taskId);
		String formUrl = travelService.getTaskFormKeyByTaskId(taskId);
		model.addAttribute("travel", travel);
		model.addAttribute("flowList", flowList);
		model.addAttribute("formUrl", formUrl);
		model.addAttribute("commentList", commentList);
		return "modules/oa/"+formUrl;
	}
	
	
	
	@ModelAttribute
	public Travel get(@RequestParam(required=false) String id) {
		Travel entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = travelService.get(id);
		}
		if (entity == null){
			entity = new Travel();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:travel:view")
	@RequestMapping(value = {"list", ""})
	public String list(Travel travel, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Travel> page = travelService.findPage(new Page<Travel>(request, response), travel); 
		model.addAttribute("page", page);
		return "modules/oa/travelList";
	}

	@RequiresPermissions("oa:travel:view")
	@RequestMapping(value = "form")
	public String form(Travel travel, Model model) {
		model.addAttribute("travel", travel);
		return "modules/oa/travelForm";
	}
	
	/**
	 * 跳转到任务表单
	 */
	@RequiresPermissions("oa:travel:view")
	@RequestMapping(value = "travelTaskform")
	public String travelTaskform(String taskId, Model model) {
		String formUrl = travelService.getTaskFormKeyByTaskId(taskId);
		formUrl += "?taskId="+taskId;
		model.addAttribute("formUrl", formUrl);
		return "modules/oa/"+formUrl;
	}

	
	@RequiresPermissions("oa:travel:edit")
	@RequestMapping(value = "delete")
	public String delete(Travel travel, RedirectAttributes redirectAttributes) {
		travelService.delete(travel);
		addMessage(redirectAttributes, "删除出差申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/travel/?repage";
	}

}