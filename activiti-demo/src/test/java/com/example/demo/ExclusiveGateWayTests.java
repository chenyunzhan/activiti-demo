package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExclusiveGateWayTests {
	
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	private static final String PROCESS_DEFINITION_KEY = "exclusive";
	private static final String BUSSINESS_KEY = "exclusive";


	
	@Test
	public void deploy() {
		this.repositoryService.deleteDeployment("2501");
		this.repositoryService.createDeployment().addClasspathResource("processes/002.bpmn").deploy();
	}
	
	@Test
	public void start() {
		this.identityService.setAuthenticatedUserId("001");
		this.runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY,BUSSINESS_KEY);
		Task task = this.taskService.createTaskQuery().processInstanceBusinessKey(BUSSINESS_KEY).singleResult();
		this.taskService.complete(task.getId());
	}
	
	@Test
	public void refused() {
		Task task = this.taskService.createTaskQuery().taskAssignee("002").singleResult();
		Map<String,Object> globalMap = new HashMap<String,Object>(){

			private static final long serialVersionUID = 1L;

			{
				put("pass",false);
			}
		};
		this.taskService.complete(task.getId(),globalMap);
	}
	
	
	@Test
	public void reapply() {
		Task task = this.taskService.createTaskQuery().taskAssignee("001").singleResult();
		this.taskService.complete(task.getId());
	}
	
	@Test
	public void agree() {
		Task task = this.taskService.createTaskQuery().taskAssignee("002").singleResult();
		Map<String,Object> globalMap = new HashMap<String,Object>(){

			private static final long serialVersionUID = 1L;

			{
				put("pass",true);
			}
		};
		this.taskService.complete(task.getId(),globalMap);
	}

}
