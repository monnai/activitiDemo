/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;

/**
 * 出差申请流程Entity
 * @author Guo
 * @version 2018-09-27
 */
public class Travel extends DataEntity<Travel> {
	
	private static final long serialVersionUID = 1L;
	private String processInstanceId;		// 流程实例编号
	private Date applyDate = DateUtils.parseDate(DateUtils.getDate());	// 申请日期
	private String applyType;				// 申请类型（1.出差、2.外出）
	private Date beginDate;					// 开始日期
	private Date endDate;					// 结束日期
	private String destination;				// 目的地
	private String reason;					// 事由
	private Date realityStartTime;			// 实际开始时间
	private Date realityEndTime;			// 实际结束时间
	
	public Travel() {
		super();
	}

	public Travel(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="申请日期不能为空")
	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
	@Length(min=1, max=20, message="外出长度必须介于 0 和 64 之间")
	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="开始日期不能为空")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="结束日期不能为空")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Length(min=1, max=255, message="目的地长度必须介于 1 和 255 之间")
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Length(min=1, max=255, message="事由长度必须介于 1 和 255 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRealityStartTime() {
		return realityStartTime;
	}

	public void setRealityStartTime(Date realityStartTime) {
		this.realityStartTime = realityStartTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRealityEndTime() {
		return realityEndTime;
	}

	public void setRealityEndTime(Date realityEndTime) {
		this.realityEndTime = realityEndTime;
	}
	
}