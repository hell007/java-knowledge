package com.self.service;

import java.util.List;

import com.self.model.ScheduleJob;

public interface ScheduleJobService extends BaseService<ScheduleJob> {
	
	
	/**
	 * 根据jobstatus状态查询需要启动的定时任务
	 * @param jobstatus
	 * @return
	 */
	List<ScheduleJob> selectByJobstatus(byte jobstatus);
	
	/**
	 * 批量启动定时任务
	 */
	void scheduleJobs();
	
	/**
	 * 创建任务
	 * @param id
	 */
	void createJob(String id);
	
	/**
	 * 更新任务
	 * @param id
	 */
	void updateJob(String id);
	
	/**
	 * 移除任务
	 * @param id
	 */
	void deleteJob(String id);
	
	/**
	 * 暂停任务
	 * @param id
	 */
	void pauseJob(String id);
	
	/**
	 * 恢复任务
	 * @param id
	 */
	void resumeJob(String id);
    
	/**
	 * 启动scheduler
	 */
	void startJob();
	
	/**
	 * 备用scheduler
	 */
	void standbyJob();
	
	/**
	 * 关闭scheduler
	 */
	void shutdownJob();
	
}
