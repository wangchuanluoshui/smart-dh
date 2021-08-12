import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//import com.hyn.activiti.common.SecurityUtil;

/*
 * @Classname Test07_ProcessRunTime
 * @Description TODO
 * @Date 2020-12-08 17:25
 * @Created by 62538
 */
public class Test08_ProcessRunTime {

    @Autowired
    ProcessRuntime processRuntime;

//    @Autowired
//    SecurityUtil securityUtil;

    @Test
    public void getProcessInstance() {
        try {
            //  securityUtil.logInAs("hyn");
            Page<ProcessInstance> processInstancePage = processRuntime
                    .processInstances(Pageable.of(0, 100));
            System.out.println("流程实例数量：" + processInstancePage.getTotalItems());
            List<ProcessInstance> list = processInstancePage.getContent();
            for (ProcessInstance pi : list) {
                System.out.println("-----------------------");
                System.out.println("getId：" + pi.getId());
                System.out.println("getName：" + pi.getName());
                System.out.println("getStartDate：" + pi.getStartDate());
                System.out.println("getStatus：" + pi.getStatus());
                System.out.println("getProcessDefinitionId：" + pi.getProcessDefinitionId());
                System.out.println("getProcessDefinitionKey：" + pi.getProcessDefinitionKey());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //初始化流程实例
    @Test
    public void startProcessInstance() {
        try {
            //   securityUtil.logInAs("hyn");
            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey("ParallelGatewayTest")
                    .withName("第一个流程实例名称")
                    .withBusinessKey("自定义bKey")
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除流程实例
    @Test
    public void delProcessInstance() {
        try {
            ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId("6fcecbdb-d3e0-11ea-a6c9-dcfb4875e032")
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        //    securityUtil.logInAs("bajie");

    }

    //挂起流程实例
    @Test
    public void suspendProcessInstance() {
        try {
            ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                    .suspend()
                    .withProcessInstanceId("1f2314cb-cefa-11ea-84aa-dcfb4875e032")
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        //    securityUtil.logInAs("bajie");

    }

    //激活流程实例
    @Test
    public void resumeProcessInstance() {
        try {
            ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                    .resume()
                    .withProcessInstanceId("1f2314cb-cefa-11ea-84aa-dcfb4875e032")
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        //    securityUtil.logInAs("bajie");

    }

    //流程实例参数
    @Test
    public void getVariables() {
        try {
            //   securityUtil.logInAs("bajie");
            List<VariableInstance> list = processRuntime.variables(ProcessPayloadBuilder
                    .variables()
                    .withProcessInstanceId("2b2d3990-d3ca-11ea-ae96-dcfb4875e032")
                    .build()
            );
            for (VariableInstance vi : list) {
                System.out.println("-------------------");
                System.out.println("getName：" + vi.getName());
                System.out.println("getValue：" + vi.getValue());
                System.out.println("getTaskId：" + vi.getTaskId());
                System.out.println("getProcessInstanceId：" + vi.getProcessInstanceId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
