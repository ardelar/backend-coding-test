package com.example.demo.service;

import java.util.List;

import com.example.demo.task.TaskEntity;

public interface ITaskEntityService {
	
	public List<TaskEntity> findAll();
	
	public TaskEntity findById(Integer id);
	
	public TaskEntity save(TaskEntity taskEntity);
	
	public void delete(int id);
}
