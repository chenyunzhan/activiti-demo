package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiDemoApplicationTests {
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	private static final String PROCESS_DEFINITION_KEY = "activiti_001";

	@Test
	public void contextLoads() {
	}
	
	
	@Test
	public void testRepositoryService() {
		repositoryService.createDeployment().addClasspathResource("processes/001.bpmn").name("activiti_001").deploy();
		
		Deployment deployment = repositoryService.createDeploymentQuery().deploymentName("activiti_001").singleResult();
		System.out.println(deployment);
		
//		repositoryService.deleteDeployment(deployment.getId());

	}
	
	@Test
	public void testIdentityService() {
		User user = new UserEntity();
		user.setEmail("huangyali08@126.com");
		user.setFirstName("亚莉");
		user.setLastName("黄");
		user.setId("002");
		user.setPassword("002");
		identityService.saveUser(user);
		
//		identityService.setAuthenticatedUserId("003");

	}
	
	@Test
	public void testRuntimeService() {
		String businessKey = "请假001";
		Map<String,Object> variables = new HashMap<String,Object>(){
			
			private static final long serialVersionUID = 1L;

			{
				put("global","global");
			}
		};
		runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, businessKey, variables);
		
	}
	
	
	@Test
	public void testTaskService() {
		List<Task> list = taskService.createTaskQuery().taskAssignee("001").orderByTaskCreateTime().asc().list();
		for(Task task: list) {
			Map<String,Object> variables = new HashMap<String,Object>(){
				private static final long serialVersionUID = 1L;
				{
					put("local","local");
				}
			};
			taskService.setVariablesLocal(task.getId(), variables);

			taskService.complete(task.getId());
		}
	}

}
