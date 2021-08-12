import com.hyn.amqp.Application;
import com.hyn.amqp.sender.RabbitSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * @Classname Test01
 * @Description TODO
 * @Date 2021-01-02 22:19
 * @Created by 62538
 */
@SpringBootTest(classes = Application.class)
public class TestAmqp {

    @Autowired
    RabbitSender rabbitSender;

    @Test
    public void test01() {
        try {
            for (int i = 0; i < 100; i++) {
                Map headerMap = new HashMap();
                headerMap.put("cook", UUID.randomUUID().toString());
                headerMap.put("username", "hyn");
                rabbitSender.send("这是第" + i + "条消息！", headerMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
