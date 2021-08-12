package com.hyn.bo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/*
 * @Classname UserBO
 * @Description TODO
 * @Date 2020/6/13 23:17
 * @Created by 62538
 */
@Data
public class UserBO {

    @NotEmpty(message = "用戶名不能为空！")
    String userName;

    @NotEmpty(message = "用户密码不能为空！")
    String password;
}
