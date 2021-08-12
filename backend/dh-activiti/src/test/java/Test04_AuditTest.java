import com.hyn.Application;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/*
 * @Classname AuditTest
 * @Description TODO
 * @Date 2020-12-03 10:23
 * @Created by 62538
 */
@SpringBootTest(classes = Application.class)
public class Test04_AuditTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    //通过bpmn部署流程
    @Test
    public void initDeploymentBPMN() {
        String filename = "bpmn/Test04.bpmn";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(filename)
                .name("Test04")
                .deploy();
        System.out.println(deployment.getName());
    }

    @Test
    public void getDefinitions() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .list();
        for (ProcessDefinition pd : list) {
            System.out.println("------流程定义--------");
            System.out.println("Name：" + pd.getName());
            System.out.println("Key：" + pd.getKey());
            System.out.println("ResourceName：" + pd.getResourceName());
            System.out.println("DeploymentId：" + pd.getDeploymentId());
            System.out.println("Version：" + pd.getVersion());
            System.out.println("");
        }
    }

    //初始化流程实例
    @Test
    public void initProcessInstance() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Test04", "AuditTest04");
        System.out.println("流程实例ID：" + processInstance.getProcessDefinitionId());
    }

    //任务查询
    @Test
    public void getTasks() {
        List<Task> list = taskService.createTaskQuery().list();
        for (Task tk : list) {
            System.out.println("------流程实例--------");
            System.out.println("Id：" + tk.getId());
            System.out.println("Name：" + tk.getName());
            System.out.println("Assignee：" + tk.getAssignee());
            System.out.println("-------------------\n");
        }
    }

    //查询我的代办任务
    @Test
    public void getTasksByAssignee() {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("employee")
                .list();
        for (Task tk : list) {
            System.out.println("------查询我的代办任务--------");
            System.out.println("Id：" + tk.getId());
            System.out.println("Name：" + tk.getName());
            System.out.println("Assignee：" + tk.getAssignee());
            System.out.println("--------------------------\n");
        }

    }

    //执行任务
    @Test
    public void completeTask() {
        try {
            taskService.complete("30f76e4d-3511-11eb-bc42-005056c00001");
            System.out.println("完成任务");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //拾取任务
    @Test
    public void claimTask() {
        try {
            Task task = taskService.createTaskQuery().taskId("a3356a84-3573-11eb-ac3f-005056c00001").singleResult();
            taskService.claim("a3356a84-3573-11eb-ac3f-005056c00001", "TestUser01,TestUser02");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //归还与交办任务
    @Test
    public void setTaskAssignee() {
        try {
            Task task = taskService.createTaskQuery().taskId("1f2a8edf-cefa-11ea-84aa-dcfb4875e032").singleResult();
            taskService.setAssignee("1f2a8edf-cefa-11ea-84aa-dcfb4875e032", "null");//归还候选任务
            taskService.setAssignee("1f2a8edf-cefa-11ea-84aa-dcfb4875e032", "wukong");//交办
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
