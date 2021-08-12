package com.hyn.configs.security;

import com.hyn.pojo.Users;
import com.hyn.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @version ： 1.0
 * @Title:：UserDetailsServiceImpl.java
 * @Package ：com.summit.homs.service.imp @Description： TODO @author： hyn @date：
 * 2018年8月21日 下午2:22:36
 */
@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginName) {
        LoginSysUser loginSysUser = new LoginSysUser();

        Optional<Users> optional = userRepository.findByUserName(loginName);
        if (optional.isPresent()) {
            Users users = optional.get();
            loginSysUser.setId(users.getId());
            loginSysUser.setUserName(users.getUserName());
            loginSysUser.setPassword(users.getPassword());
            loginSysUser.setMobile(users.getMobile());
            loginSysUser.setBirthday(users.getBirthday());
            loginSysUser.setEmail(users.getEmail());
            loginSysUser.setSex(users.getSex());
            loginSysUser.setFace(users.getFacePath());
            loginSysUser.setNickName(users.getNickName());
            loginSysUser.setRoleId(users.getRoleId());
            return loginSysUser;
        } else {
            return null;
        }
    }
}
