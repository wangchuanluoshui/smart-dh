import com.hyn.Application;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/*
 * @Classname ProcessIntanceTest
 * @Description TODO
 * @Date 2020-12-02 17:06
 * @Created by 62538
 */
@SpringBootTest(classes = Application.class)
public class Test03_ProcessInstanceTest {

    @Autowired
    private RuntimeService runtimeService;

    //初始化流程实例
    @Test
    public void initProcessInstance() {
        //1、获取页面表单填报的内容，请假时间，请假事由，String fromData
        //2、fromData 写入业务表，返回业务表主键ID==businessKey
        //3、把业务数据与Activiti7流程数据关联
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1", "mytest01");
        System.out.println("流程实例ID：" + processInstance.getProcessDefinitionId());

    }

    //获取流程实例列表
    @Test
    public void getProcessInstances() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance pi : list) {
            System.out.println("--------流程实例------");
            System.out.println("ProcessInstanceId：" + pi.getProcessInstanceId());
            System.out.println("ProcessDefinitionId：" + pi.getProcessDefinitionId());
            System.out.println("isEnded:" + pi.isEnded());
            System.out.println("isSuspended：" + pi.isSuspended());
            System.out.println("business：" + pi.getBusinessKey());

        }

    }


    //暂停与激活流程实例
    @Test
    public void activitieProcessInstance() {
        try {
            runtimeService.suspendProcessInstanceById("0477252d-347f-11eb-87ce-005056c00001");
            System.out.println("挂起流程实例");
            getProcessInstances();
            runtimeService.activateProcessInstanceById("0477252d-347f-11eb-87ce-005056c00001");
            System.out.println("激活流程实例");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除流程实例
    @Test
    public void delProcessInstance() {
        try {
            runtimeService.deleteProcessInstance("0477252d-347f-11eb-87ce-005056c00001", "删着玩");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("删除流程实例");
    }
}
