import com.hyn.Application;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/*
 * @Classname Test06
 * @Description TODO
 * @Date 2020-12-06 16:24
 * @Created by 62538
 */
@SpringBootTest(classes = Application.class)
public class Test06_UEL {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    //启动流程实例带参数，执行执行人
    @Test
    public void initProcessInstanceWithArgs() {
        //流程变量
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("zhixingren", "wukong");
        //variables.put("ZhiXingRen2", "aaa");
        //variables.put("ZhiXingRen3", "wukbbbong");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(
                        "UEL_Test01"
                        , "UEL_Test01"
                        , variables);
        System.out.println("流程实例ID：" + processInstance.getProcessDefinitionId());

    }

    //完成任务带参数，指定流程变量测试
    @Test
    public void completeTaskWithArgs() {
        try {
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("pay", "101");
            taskService.complete("a616ea19-d3a7-11ea-9e14-dcfb4875e032", variables);
            System.out.println("完成任务");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //启动流程实例带参数，使用实体类
    @Test
    public void initProcessInstanceWithClassArgs() {
        try {
            UEL_POJO uel_pojo = new UEL_POJO();
            uel_pojo.setZhixingren("bajie");

            //流程变量
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("uelpojo", uel_pojo);

            ProcessInstance processInstance = runtimeService
                    .startProcessInstanceByKey(
                            "myProcess_uelv3"
                            , "bKey002"
                            , variables);
            System.out.println("流程实例ID：" + processInstance.getProcessDefinitionId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //任务完成环节带参数，指定多个候选人
    @Test
    public void initProcessInstanceWithCandiDateArgs() {
        try {
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("houxuanren", "wukong,tangseng");
            taskService.complete("4f6c9e23-d3ae-11ea-82ba-dcfb4875e032", variables);
            System.out.println("完成任务");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //直接指定流程变量
    @Test
    public void otherArgs() {
        try {
            runtimeService.setVariable("4f6c9e23-d3ae-11ea-82ba-dcfb4875e032", "pay", "101");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        runtimeService.setVariables();
//        taskService.setVariable();
//        taskService.setVariables();

    }

}
