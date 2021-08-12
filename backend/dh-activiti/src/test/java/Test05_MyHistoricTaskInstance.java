import com.hyn.Application;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/*
 * @Classname HistoricTaskInstance
 * @Description TODO
 * @Date 2020-12-03 22:41
 * @Created by 62538
 */
@SpringBootTest(classes = Application.class)
public class Test05_MyHistoricTaskInstance {

    @Autowired
    private HistoryService historyService;

    //根据用户名查询历史记录
    @Test
    public void HistoricTaskInstanceByUser() {
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .taskAssignee("TestUser01,TestUser02")
                .list();
        for (HistoricTaskInstance hi : list) {
            System.out.println("Id：" + hi.getId());
            System.out.println("ProcessInstanceId：" + hi.getProcessInstanceId());
            System.out.println("Name：" + hi.getName());

        }

    }


    //根据流程实例ID查询历史
    @Test
    public void HistoricTaskInstanceByPiID() {
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .processInstanceId("9d39c7a0-3574-11eb-aef6-005056c00001")
                .list();
        for (HistoricTaskInstance hi : list) {
            System.out.println("Id：" + hi.getId());
            System.out.println("ProcessInstanceId：" + hi.getProcessInstanceId());
            System.out.println("Name：" + hi.getName());

        }
    }

}
