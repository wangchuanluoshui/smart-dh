package com.hyn.configs.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyn.common.ICodes;
import com.hyn.common.IResult;
import com.hyn.common.IResultUtil;
import com.hyn.pojo.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登陆成功处理类
 *
 * @version ： 1.0
 * @Title:：LoginSuccessHandler.java
 * @Package ：com.summit.homs.global.cfg @Description： TODO @author： hyn @date：
 * 2018年8月16日 下午5:22:01
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 获取当前用户(domain接收)
        LoginSysUser loginSysUser = (LoginSysUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Users user = new Users();
        user.setId(loginSysUser.getId());
        user.setUserName(loginSysUser.getUsername());
        user.setPassword(loginSysUser.getPassword());
        user.setMobile(loginSysUser.getMobile());
        user.setBirthday(loginSysUser.getBirthday());
        user.setNickName(loginSysUser.getNickName());
        user.setFacePath(loginSysUser.getFace());
        user.setSex(loginSysUser.getSex());
        user.setRoleId(loginSysUser.getRoleId());
        // 设置session
        request.getSession().setAttribute("LOGIN_USER", user);
        logger.debug("登陆成功，用户：" + user.getUserName());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        // 返回JSON字符串
        PrintWriter writer = response.getWriter();
        IResult<Object> respMsg = IResultUtil.responseMsg(ICodes.CODE_0000);
        JSONObject json = (JSONObject) JSON.toJSON(respMsg);
        json.put("status", "200");
        writer.write(json.toJSONString());
    }

}
