import com.alibaba.fastjson.JSONObject;
import com.hyn.pojo.Users;
import com.hyn.service.Application;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @Classname MyTest
 * @Description TODO
 * @Date 2020/9/20 21:30
 * @Created by 62538
 */
@SpringBootTest(classes = Application.class)
public class BeanInjectTest {


    @Test
    public void batchSave() {
        for (int i = 0; i < 200; i++) {
            Users users = new Users();
            users.setNickName("aa" + i);
            users.setSex(1);
            users.setBirthday(new Date());
            users.setFacePath("");
            users.setPassword("");
            users.setUserName("bb" + i);
            users.setMobile("北京市");
            users.setCreatedTime(new Date());
            users.setEmail("625385032@qq.com");
            users.setRealname("cc" + i);
        }
    }


    @Test
    public void test01() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("test",new Boolean(true));
        System.out.println(jsonObject);
    }

    @Test
    public void test02() {

        Map<String,String> map=new HashMap();
        map.put("1","a1");
        map.put("2","b2");


        Map.Entry<String, String> v = map.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals("b3"))
                .findAny()
                .orElse(null);

        System.out.println(v);
    }
}
