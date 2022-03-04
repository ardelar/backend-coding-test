package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ITaskEntityService;
import com.example.demo.task.TaskEntity;

@CrossOrigin(origins= {"http://localhost:8443"})
@RestController
@RequestMapping("/api")
public class TaskEntityRestController {

	@Autowired
	private ITaskEntityService taskEntityService;
	
	@GetMapping("/tasks")
	public List<TaskEntity> index() {
		return taskEntityService.findAll();
	}
	
	@GetMapping("/tasks/{id}")
	public ResponseEntity<?> show(@PathVariable Integer id) {
		
		TaskEntity taskEntity = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			taskEntity = taskEntityService.findById(id);
		} catch(DataAccessException e) {
			response.put("message", "Error connecting to database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(taskEntity == null) {
			response.put("message", "The task: ".concat(id.toString().concat(" does not exist in the database!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<TaskEntity>(taskEntity, HttpStatus.OK);
	}
	
	@PostMapping("/tasks")
	public ResponseEntity<?> create(@Validated @RequestBody TaskEntity taskEntity, BindingResult result) {
		
		TaskEntity newTaskEntity = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "The field '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			newTaskEntity = taskEntityService.save(taskEntity);
		} catch(DataAccessException e) {
			response.put("message", "Error inserting in the database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "The task has been created succesfully!");
		response.put("task", newTaskEntity);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/tasks/{id}")
	public ResponseEntity<?> update(@Validated @RequestBody TaskEntity taskEntity, BindingResult result, @PathVariable 	Integer id) {

		TaskEntity currentTaskEntity = taskEntityService.findById(id);

		TaskEntity updatedTaskEntity = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "The field '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (currentTaskEntity == null) {
			response.put("message", "Error: it was not possible editing the task ID: "
					.concat(id.toString().concat(" it does no exist in database!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			currentTaskEntity.setDescription(taskEntity.getDescription());
			currentTaskEntity.setCompleted(taskEntity.isCompleted());
			currentTaskEntity.setPriority(taskEntity.getPriority());
			
			updatedTaskEntity = taskEntityService.save(currentTaskEntity);
		} catch (DataAccessException e) {
			response.put("message", "Error updating the task in the database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("message", "The task has been succesfully updated!");
		response.put("task", updatedTaskEntity);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/tasks/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			taskEntityService.delete(id);
		} catch (DataAccessException e) {
			response.put("message", "Error deleting the task from database.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "The task has been succesfully deleted!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
