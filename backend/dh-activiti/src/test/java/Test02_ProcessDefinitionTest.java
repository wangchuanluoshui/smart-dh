import com.hyn.Application;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/*
 * @Classname ProcessDefinitionTest
 * @Description TODO
 * @Date 2020-12-01 20:42
 * @Created by 62538
 */
@SpringBootTest(classes = Application.class)
public class Test02_ProcessDefinitionTest {

    @Autowired
    private RepositoryService repositoryService;

    //查询流程定义
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

        }

    }

    //删除流程定义
    @Test
    public void delDefinition() {
        String pdID = "a18a0abe-33ce-11eb-819a-005056c00001";
        try {
            repositoryService.deleteDeployment(pdID, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("删除流程定义成功");
    }
}
