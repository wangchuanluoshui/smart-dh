package com.hyn.controller.role;

import com.hyn.VO.PermissionsVO;
import com.hyn.VO.RoleVO;
import com.hyn.common.*;
import com.hyn.pojo.Role;
import com.hyn.service.role.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/*
 * @Classname UserController
 * @Description TODO
 * @Date 2020/6/13 22:27
 * @Created by 62538
 */
@Api(value = "role", tags = "角色管理模块")
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    IRoleService iRoleService;

    @ApiOperation(value = "条件分页查询,条件")
    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PageReponse findAllOfPage(
            @ApiParam(value = "角色名称", required = false) @RequestParam(name = "roleChinaName", required = false) String roleChinaName,
            @ApiParam(value = "创建时间", required = false) @RequestParam(name = "createdTime", required = false) String createdTime,
            @ApiParam(value = "排序", required = false) @RequestParam(name = "sorter", required = false) String sorter,
            @ApiParam(value = "页码", required = false) @RequestParam(name = "current", required = false) Integer current,
            @ApiParam(value = "页大小", required = false) @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        IPageRequest iPageRequest = new IPageRequest(sorter, current, pageSize);
        PageReponse datepage = iRoleService.getRoleChinaNameList(roleChinaName, createdTime, iPageRequest.getRequestPage());
        return datepage;
    }

    @ApiOperation(value = "新增角色信息")
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Role> addRole(
            @ApiParam(value = "角色信息", required = true) @RequestBody Role Role) {
        String resultCode = iRoleService.save(Role);
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "更新角色信息")
    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Role> updateRole(
            @ApiParam(value = "角色信息", required = true) @RequestBody Role Role) {
        String resultCode = iRoleService.update(Role);
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "删除角色信息")
    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Role> deleteRole(
            @ApiParam(value = "角色信息", required = true) @RequestParam(name = "id", required = false) String id) {
        String resultCode = iRoleService.delete(Arrays.asList(id));
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "更新角色权限信息")
    @RequestMapping(value = "/permissions", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Role> updatePermissions(
            @ApiParam(value = "角色信息", required = true) @RequestBody PermissionsVO permissionsVO) {
        String resultCode = iRoleService.updatePermissions(permissionsVO);
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "查询角色权限信息")
    @RequestMapping(value = "/permissions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult getPermissions(
            @ApiParam(value = "角色信息", required = true) @RequestParam(name = "id", required = false) String id) {
        String[] result = iRoleService.getPermissions(id);
        return IResultUtil.responseMsg(ICodes.CODE_0000, result);
    }

    @ApiOperation(value = "查询角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult findAllOfPage(){
        List<RoleVO> roleList = iRoleService.getRoleList();
        return IResultUtil.responseMsg(ICodes.CODE_0000, roleList);
    }

}
