import com.hyn.Application;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

@SpringBootTest(classes = Application.class)
public class Test01_DeploymentTest {

    @Autowired
    private RepositoryService repositoryService;

    //通过bpmn部署流程
    @Test
    public void initDeploymentBPMN() {
        String filename = "bpmn/Test6_ParallelGateway.bpmn";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(filename)
                .name("ParallelGatewayTest")
                .deploy();
        System.out.println(deployment.getName());
    }

    //查询流程部署
    @Test
    public void getDeployments() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for (Deployment dep : list) {
            System.out.println("Id：" + dep.getId());
            System.out.println("Name：" + dep.getName());
            System.out.println("DeploymentTime：" + dep.getDeploymentTime());
            System.out.println("Key：" + dep.getKey());
        }

    }

}
