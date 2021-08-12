package com.hyn.controller.org;

import com.hyn.bo.DeleteIdBo;
import com.hyn.common.IPageRequest;
import com.hyn.common.IResult;
import com.hyn.common.IResultUtil;
import com.hyn.common.PageReponse;
import com.hyn.pojo.Organization;
import com.hyn.service.organization.IOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/*
 * @Classname UserController
 * @Description TODO
 * @Date 2020/6/13 22:27
 * @Created by 62538
 */
@Api(value = "organization", tags = "机构管理模块")
@RestController
@RequestMapping("organization")
public class OrganizationController {

    @Autowired
    IOrganizationService iOrganizationService;

    @ApiOperation(value = "条件分页查询,条件")
    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PageReponse findAllOfPage(
            @ApiParam(value = "角色名称", required = false) @RequestParam(name = "name", required = false) String name,
            @ApiParam(value = "排序", required = false) @RequestParam(name = "sorter", required = false) String sorter,
            @ApiParam(value = "页码", required = false) @RequestParam(name = "current", required = false) Integer current,
            @ApiParam(value = "页大小", required = false) @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        IPageRequest iPageRequest = new IPageRequest(sorter, current, pageSize);
        PageReponse datepage = iOrganizationService.getNameList(name, iPageRequest.getRequestPage());
        return datepage;
    }

    @ApiOperation(value = "新增角色信息")
    @RequestMapping(value = "/", method = RequestMethod.POST, produces =MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Organization> addOrgization(
            @ApiParam(value = "角色信息", required = true) @RequestBody Organization organization) {
        String resultCode = iOrganizationService.save(organization);
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "更新角色信息")
    @RequestMapping(value = "/", method = RequestMethod.PUT, produces =MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Organization> updateOrgization(
            @ApiParam(value = "角色信息", required = true) @RequestBody Organization organization) {
        String resultCode = iOrganizationService.update(organization);
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "删除角色信息")
    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces =MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Organization> deleteOrgization(
            @ApiParam(value = "角色信息", required = true) @RequestBody DeleteIdBo ids) {
        String resultCode = iOrganizationService.delete(ids.getIds());
        return IResultUtil.responseMsg(resultCode, null);
    }

}
