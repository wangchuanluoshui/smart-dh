package com.hyn.service.role;

import com.hyn.VO.PermissionsVO;
import com.hyn.VO.RoleVO;
import com.hyn.common.ICodes;
import com.hyn.common.IPageResponse;
import com.hyn.common.PageReponse;
import com.hyn.pojo.Menu;
import com.hyn.pojo.Role;
import com.hyn.pojo.RoleMenuRelation;
import com.hyn.repository.IMenuRepository;
import com.hyn.repository.IRoleMenuRelRepository;
import com.hyn.repository.IRoleRepository;
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
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/*
 * @Classname IUserService
 * @Description TODO
 * @Date 2020/9/20 16:53
 * @Created by 62538
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    IRoleRepository iRoleRepository;

    @Autowired
    IRoleMenuRelRepository iRoleMenuRelRepository;

    @Autowired
    IMenuRepository iMenuRepository;

    @Override
    public PageReponse getRoleChinaNameList(String roleChinaName, String createdTime, Pageable pageable) {
        Page<Role> roles = iRoleRepository.findAll(this.getWhereClause(roleChinaName, createdTime), pageable);
        return new IPageResponse<Role>(roles.getContent(), pageable, roles.getTotalElements()).buildMyPage();
    }

    private Specification<Role> getWhereClause(String roleChinaName, String createdTime) {
        return new Specification<Role>() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                if (!StringUtils.isEmpty(roleChinaName)) {
                    predicate.getExpressions()
                            .add(criteriaBuilder.like(root.get("roleChinaName"), "%" + roleChinaName + "%"));
                }
                if (!StringUtils.isEmpty(createdTime)) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    predicate.getExpressions().add(criteriaBuilder.between(root.get("createdTime"),
                            Date.from(LocalDate.parse(createdTime).atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()),
                            Date.from(LocalDate.parse(createdTime).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant())));
                }
                return predicate;
            }
        };
    }

    @Override
    public String save(Role role) {

        String roleChinaName = role.getRoleChinaName();
        String fullName = role.getRoleFullName();
        List<Role> roles = iRoleRepository.findByRoleChinaNameOrRoleFullName(roleChinaName, fullName);

        if (roles.size() > 0) {
            return ICodes.CODE_1003;
        }
        iRoleRepository.save(role);
        return ICodes.CODE_0000;
    }

    @Override
    public String update(Role role) {
        String roleChinaName = role.getRoleChinaName();
        String fullName = role.getRoleFullName();
        List<Role> roles = iRoleRepository.findByRoleChinaNameOrRoleFullName(roleChinaName, fullName);

        Role currentRole = null;
        int roleListSize = roles.size();
        for (Role cRole : roles) {
            if (cRole.getId().equals(role.getId())) {
                currentRole = cRole;
                roleListSize--;
            }
        }
        if (roleListSize != 0) {
            return ICodes.CODE_1003;
        }
        String resultCode = ICodes.CODE_0000;
        try {
            role.setCreatedTime(currentRole.getCreatedTime());
            iRoleRepository.saveAndFlush(role);
        } catch (Exception e) {
            e.printStackTrace();
            resultCode = ICodes.CODE_9993;
        }
        return resultCode;
    }

    @Override
    public String delete(List<String> roleIds) {
        List<Role> roleList = iRoleRepository.findByIdIn(roleIds);
        iRoleRepository.deleteAll(roleList);
        return ICodes.CODE_0000;
    }

    @Override
    @Transactional
    public String updatePermissions(PermissionsVO permissionsVO) {
        String roleId = permissionsVO.getRoleId();
        Optional<Role> roleOptional = iRoleRepository.findById(roleId);
        if (roleOptional.isPresent()) {
            String[] menuList = permissionsVO.getMenuList();
            if (menuList.length == 0) {
                return ICodes.CODE_1005;
            }
            iRoleMenuRelRepository.deleteByRoleId(permissionsVO.getRoleId());
            List<RoleMenuRelation> menuIdFromPermissions = getMenuIdFromPermissions(permissionsVO);
            iRoleMenuRelRepository.saveAll(menuIdFromPermissions);
        }
        return ICodes.CODE_0000;
    }

    @Override
    public String[] getPermissions(String id) {
        List<RoleMenuRelation> roleMenuList = iRoleMenuRelRepository.findByRoleId(id);
        List<String> collect = roleMenuList.stream().map(RoleMenuRelation::getMenuId).collect(Collectors.toList());
        return collect.stream().toArray(String[]::new);
    }

    @Override
    public List<RoleVO> getRoleList() {
        List<Role> roles = iRoleRepository.findAll();
        return convertToRoleVO(roles);
    }

    @Override
    public String[] getAccess(String id) {
        List<RoleMenuRelation> roleMenuList = iRoleMenuRelRepository.findByRoleId(id);
        List<String> collect = roleMenuList.stream().map(RoleMenuRelation::getMenuId).collect(Collectors.toList());
        List<Menu> menus = iMenuRepository.findByIdIn(collect);
        return menus.stream().map(Menu::getShowTag).toArray(String[]::new);
    }

    private List<RoleMenuRelation> getMenuIdFromPermissions(PermissionsVO permissionsVO) {
        List roleMenuRels = new ArrayList();
        String[] menuList = permissionsVO.getMenuList();
        String roleId = permissionsVO.getRoleId();
        Arrays.asList(menuList).stream().map(menuVO -> {
            RoleMenuRelation roleMenuRelation = new RoleMenuRelation();
            roleMenuRelation.setRoleId(roleId);
            roleMenuRelation.setMenuId(menuVO);
            roleMenuRels.add(roleMenuRelation);
            return null;
        }).collect(Collectors.toList());

        return roleMenuRels;
    }

    private List<RoleVO> convertToRoleVO(List<Role> roles) {
        ArrayList<RoleVO> roleVOs = new ArrayList<>();
        roles.stream().map(role -> {
            RoleVO roleVO = new RoleVO();
            roleVO.setLabel(role.getRoleChinaName());
            roleVO.setValue(role.getId());
            roleVOs.add(roleVO);
            return null;
        }).collect(Collectors.toList());
        return roleVOs;
    }
}
