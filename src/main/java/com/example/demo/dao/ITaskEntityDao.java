package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.task.TaskEntity;

public interface ITaskEntityDao extends JpaRepository<TaskEntity, Integer> {

}
