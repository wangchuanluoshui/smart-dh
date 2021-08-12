package com.hyn.service.role;

import com.hyn.VO.PermissionsVO;
import com.hyn.VO.RoleVO;
import com.hyn.common.PageReponse;
import com.hyn.pojo.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
 * @Classname IUserService
 * @Description TODO
 * @Date 2020/9/20 16:53
 * @Created by 62538
 */
public interface IRoleService {
    PageReponse getRoleChinaNameList(String roleChinaName, String createdTime, Pageable pageable);

    String save(Role role);

    String update(Role role);

    String delete(List<String> roleIds);

    String updatePermissions(PermissionsVO permissionsVO);

    String[] getPermissions(String id);

    List<RoleVO> getRoleList();

    String[] getAccess(String id);

}
