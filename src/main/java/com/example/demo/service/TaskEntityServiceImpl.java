package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.ITaskEntityDao;
import com.example.demo.task.TaskEntity;

@Service
public class TaskEntityServiceImpl implements ITaskEntityService {

	@Autowired
	private ITaskEntityDao taskEntityDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<TaskEntity> findAll() {
		return taskEntityDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public TaskEntity findById(Integer id) {
		return taskEntityDao.findById(id).orElse(null);
	}

	@Override
	public TaskEntity save(TaskEntity taskEntity) {
		return taskEntityDao.save(taskEntity);
	}

	@Override
	public void delete(int id) {
		taskEntityDao.deleteById(id);
	}

}
