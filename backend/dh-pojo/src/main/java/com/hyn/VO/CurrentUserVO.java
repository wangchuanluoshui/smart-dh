package com.hyn.VO;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class CurrentUserVO {
    String name;
    String userid;
    String facePath;
    String group="蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED";
    String email;
    String phone;
    Integer notifyCount=12;
    String title="交互专家";
    Integer unreadCount=11;
    String signature="蒹葭苍苍，白露为霜。所谓伊人，在水一方";
    String address="陕西省西安市";
    String country="China";
    List<JSONObject> access;


}
