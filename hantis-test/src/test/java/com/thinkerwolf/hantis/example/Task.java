package com.thinkerwolf.hantis.example;

import com.thinkerwolf.hantis.orm.annotation.*;

import java.io.Serializable;

/**
 * Auto genarated by Hantis 
 */
@Entity(name = "task")
public class Task implements Serializable {
	/** 任务ID */
	@Id
	@Column(name = "task_id")
	@GeneratedValue
	private String taskId;
	
	/** 任务名称 */
	@Column(name = "name")
	private String name;
	
	/** 任务介绍 */
	@Column(name = "intro")
	@GeneratedValue
	private String intro;
	
	
	public Task() {}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
		
	public String getTaskId() {
		return this.taskId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
		
	public String getName() {
		return this.name;
	}
	
	public void setIntro(String intro) {
		this.intro = intro;
	}
		
	public String getIntro() {
		return this.intro;
	}
	
	
	// Self code start
	
	// Self code end
}