package com.hyn.configs.security.othersys.login;

import com.hyn.configs.security.LoginSysUser;
import com.hyn.pojo.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OtherSysUserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
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
        response.sendRedirect("http://127.0.0.1:8000/welcome");
    }
}
