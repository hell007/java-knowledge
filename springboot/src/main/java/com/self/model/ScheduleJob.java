package com.self.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="jie_schedule_job")
public class ScheduleJob implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator(name="",sequenceName="SELECT LAST_INSERT_ID()")
    private String id;
	
	private String jobname;
	
	private String jobgroup;
	
	private Byte jobstatus;
	
	private Byte status;
	
	private String cronexpression;
	
	private String quartzclass;
	
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getJobgroup() {
		return jobgroup;
	}

	public void setJobgroup(String jobgroup) {
		this.jobgroup = jobgroup;
	}

	public Byte getJobstatus() {
		return jobstatus;
	}

	public void setJobstatus(Byte jobstatus) {
		this.jobstatus = jobstatus;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getCronexpression() {
		return cronexpression;
	}

	public void setCronexpression(String cronexpression) {
		this.cronexpression = cronexpression;
	}

	public String getQuartzclass() {
		return quartzclass;
	}

	public void setQuartzclass(String quartzclass) {
		this.quartzclass = quartzclass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ScheduleJob [id=" + id + ", jobname=" + jobname + ", jobgroup="
				+ jobgroup + ", jobstatus=" + jobstatus + ", status=" + status
				+ ", cronexpression=" + cronexpression + ", quartzclass="
				+ quartzclass + ", description=" + description + "]";
	}

}
