package com.hyn.service.menu;

import com.hyn.VO.MenuVO;
import com.hyn.bo.MenuBO;
import com.hyn.common.BeanUtils;
import com.hyn.common.ICodes;
import com.hyn.common.IPageResponse;
import com.hyn.common.PageReponse;
import com.hyn.pojo.Menu;
import com.hyn.repository.IMenuRepository;
import com.hyn.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @Classname IUserService
 * @Description TODO
 * @Date 2020/9/20 16:53
 * @Created by 62538
 */
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    IMenuRepository iMenuRepository;

    @Override
    public PageReponse getNameList(String name, Pageable pageable) {
        Page<Menu> menus = iMenuRepository.findAll(this.getWhereClause(name), pageable);

        List<Menu> menuList = menus.getContent();
        List<MenuBO> menuBOS = new LinkedList<>();
        if (!menuList.isEmpty()) {
            try {
                menuBOS = BeanUtils.batchTransform(MenuBO.class, menuList);
                menuBOS = TreeUtils.createTree(menuBOS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new IPageResponse<MenuBO>(menuBOS, pageable, menus.getTotalElements()).buildMyPage();
    }

    private Specification<Menu> getWhereClause(String name) {
        return new Specification<Menu>() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                if (!StringUtils.isEmpty(name)) {
                    predicate.getExpressions()
                            .add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }
                return predicate;
            }
        };
    }

    @Override
    public String save(Menu menu) {
        String name = menu.getName();
        String path = menu.getPath();
        List<Menu> menus = iMenuRepository.findByNameOrPath(name, path);

        if (menus.size() > 0) {
            return ICodes.CODE_1003;
        }
        iMenuRepository.save(menu);
        return ICodes.CODE_0000;
    }

    @Override
    public String update(Menu menu) {
        String name = menu.getName();
        String path = menu.getPath();
        List<Menu> menus = iMenuRepository.findByNameOrPath(name, path);

        Menu currentMenu = null;
        int menuListSize = menus.size();
        for (Menu cMenu : menus) {
            if (cMenu.getId().equals(menu.getId())) {
                currentMenu = cMenu;
                menuListSize--;
            }
        }
        if (menuListSize != 0) {
            return ICodes.CODE_1003;
        }
        String resultCode = ICodes.CODE_0000;
        try {
            menu.setCreatedTime(currentMenu.getCreatedTime());
            iMenuRepository.saveAndFlush(menu);
        } catch (Exception e) {
            e.printStackTrace();
            resultCode = ICodes.CODE_9993;
        }
        return resultCode;
    }

    @Override
    public String delete(List<String> menuIds) {
        List<Menu> menuList = iMenuRepository.findByIdIn(menuIds);
        iMenuRepository.deleteAll(menuList);
        return ICodes.CODE_0000;
    }

    @Override
    public List<MenuVO> getMenuList() {
        List<Menu> menuList = iMenuRepository.findAll(this.getWhereClause(null));
        List<MenuVO> menuVOS = convertToMenuVO(menuList);
        List<MenuVO> tree = TreeUtils.createTree(menuVOS, "key", "pKey", "children");
        return tree;
    }

    private List<MenuVO> convertToMenuVO(List<Menu> menuList) {
        List<MenuVO> collect = menuList.stream().map(menu -> {
            MenuVO menuVO = new MenuVO();
            menuVO.setKey(menu.getId());
            menuVO.setTitle(menu.getChineseName());
            menuVO.setPKey(menu.getPid());
            return menuVO;
        }).collect(Collectors.toList());
        return collect;
    }
}
