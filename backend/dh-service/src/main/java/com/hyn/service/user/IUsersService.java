package com.hyn.service.user;

import com.hyn.common.PageReponse;
import com.hyn.pojo.Users;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
 * @Classname IUserService
 * @Description TODO
 * @Date 2020/9/20 16:53
 * @Created by 62538
 */
public interface IUsersService {
    PageReponse getUserList(String userName, Integer sex, String mobile, String createdTime, Pageable pageable);

    String save(Users users);

    String update(Users users);

    String delete(List<String> userIds);

}
