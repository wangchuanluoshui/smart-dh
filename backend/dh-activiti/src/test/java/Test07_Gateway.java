import com.hyn.Application;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/*
 * @Classname Test07_Gateway
 * @Description TODO
 * @Date 2020-12-06 17:03
 * @Created by 62538
 */
@SpringBootTest(classes = Application.class)
public class Test07_Gateway {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    //启动流程实例带参数，执行执行人
    @Test
    public void initProcessInstanceWithArgs() {
        //流程变量
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(
                        "ParallelGatewayTest"
                        , "ParallelGatewayTest");
        System.out.println("流程实例ID：" + processInstance.getProcessDefinitionId());
    }

    //查询我的代办任务
    @Test
    public void getTasksByAssignee() {
        List<Task> list = taskService.createTaskQuery()
                .taskAssigneeIds(Arrays.asList("manager1", "manager2"))
                .list();
        for (Task tk : list) {
            System.out.println("------查询我的代办任务--------");
            System.out.println("Id：" + tk.getId());
            System.out.println("Name：" + tk.getName());
            System.out.println("Assignee：" + tk.getAssignee());
            System.out.println("--------------------------\n");
        }

    }

    //完成任务带参数，指定流程变量测试
    @Test
    public void completeTaskWithArgs() {
        try {
            taskService.complete("69aaba69-37a5-11eb-a330-005056c00001");
            System.out.println("完成任务");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
