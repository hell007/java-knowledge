package com.self.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.self.model.ScheduleJob;
import com.self.schedule.ScheduleJobManager;

@Service
public class ScheduleJobService  extends BaseService<ScheduleJob> {
	
	@Autowired  
    public ScheduleJobManager scheduleJobManager; 
	
	
	/**
	 * 根据jobstatus状态查询需要启动的定时任务
	 * @param jobstatus
	 * @return
	 */
	public List<ScheduleJob> selectByJobstatus(byte jobstatus) {
		Example example = new Example(ScheduleJob.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("jobstatus", jobstatus);
		criteria.andEqualTo("status", 1);
		return mapper.selectByExample(example);
	}  
	
	/**
	 * 批量启动定时任务
	 */
	public void scheduleJobs() {
		//查询所有定时任务
        List<ScheduleJob> jobList = this.selectByJobstatus((byte) 1);
        for(ScheduleJob job:jobList){
        	scheduleJobManager.createScheduleJob(job);
        }
	}
	
	/**
	 * 创建任务
	 * @param id
	 */
	public void createJob(String id) {
		ScheduleJob job = this.selectByKey(id);
		job.setJobstatus((byte) 1);
		this.updateNotNull(job);
		scheduleJobManager.createScheduleJob(job);
    }
	
	/**
	 * 更新任务
	 * @param id
	 */
	public void updateJob(String id) {
		ScheduleJob job = this.selectByKey(id);
		scheduleJobManager.updateScheduleJob(job);
    }
	
	/**
	 * 移除任务
	 * @param id
	 */
	public void deleteJob(String id) {
		ScheduleJob job = this.selectByKey(id);	
		job.setJobstatus((byte) 0);
		this.updateNotNull(job);
		scheduleJobManager.removeScheduleJob(job);
    }
	
	/**
	 * 暂停任务
	 * @param id
	 */
	@Transactional(rollbackFor = Exception.class)
    public void pauseJob(String id) {
		ScheduleJob job = this.selectByKey(id);
		scheduleJobManager.pauseScheduleJob(job);
    }
	
	/**
	 * 恢复任务
	 * @param id
	 */
	@Transactional(rollbackFor = Exception.class)
    public void resumeJob(String id) {
		ScheduleJob job = this.selectByKey(id);
		scheduleJobManager.resumeScheduleJob(job);
    }
    
	/**
	 * 启动scheduler
	 */
	@Transactional(rollbackFor = Exception.class)
	public void startJob() {
		scheduleJobManager.startScheduleJob();
    }
	
	/**
	 * 备用scheduler
	 */
	@Transactional(rollbackFor = Exception.class)
	public void standbyJob() {
		scheduleJobManager.standbyScheduleJob();
    }
	
	/**
	 * 关闭scheduler
	 */
	@Transactional(rollbackFor = Exception.class)
	public void shutdownJob() {
		scheduleJobManager.shutdownScheduleJob();
    }
		
}
