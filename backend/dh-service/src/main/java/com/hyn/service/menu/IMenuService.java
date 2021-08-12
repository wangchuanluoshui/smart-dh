package com.hyn.service.menu;

import com.hyn.VO.MenuVO;
import com.hyn.common.PageReponse;
import com.hyn.pojo.Menu;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
 * @Classname IUserService
 * @Description TODO
 * @Date 2020/9/20 16:53
 * @Created by 62538
 */
public interface IMenuService {
    PageReponse getNameList(String name, Pageable pageable);

    String save(Menu menu);

    String update(Menu menu);

    String delete(List<String> menuIds);

    List<MenuVO> getMenuList();
}
