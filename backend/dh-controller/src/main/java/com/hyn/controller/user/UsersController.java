package com.hyn.controller.user;

import com.hyn.common.IPageRequest;
import com.hyn.common.IResult;
import com.hyn.common.IResultUtil;
import com.hyn.common.PageReponse;
import com.hyn.pojo.Users;
import com.hyn.service.user.IUsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/*
 * @Classname UserController
 * @Description TODO
 * @Date 2020/6/13 22:27
 * @Created by 62538
 */
@Api(value = "users", tags = "用户管理模块")
@RestController
@RequestMapping("user")
public class UsersController {

    @Autowired
    IUsersService iUsersService;

    @ApiOperation(value = "条件分页查询,条件")
    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PageReponse findAllOfPage(
            @ApiParam(value = "用户名称", required = false) @RequestParam(name = "userName", required = false) String userName,
            @ApiParam(value = "性别", required = false) @RequestParam(name = "sex", required = false) Integer sex,
            @ApiParam(value = "电话", required = false) @RequestParam(name = "mobile", required = false) String mobile,
            @ApiParam(value = "创建时间", required = false) @RequestParam(name = "createdTime", required = false) String createdTime,
            @ApiParam(value = "排序", required = false) @RequestParam(name = "sorter", required = false) String sorter,
            @ApiParam(value = "页码", required = false) @RequestParam(name = "current", required = false) Integer current,
            @ApiParam(value = "页大小", required = false) @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        IPageRequest iPageRequest = new IPageRequest(sorter, current, pageSize);
        PageReponse datepage = iUsersService.getUserList(userName, sex, mobile, createdTime, iPageRequest.getRequestPage());
        return datepage;
    }

    @ApiOperation(value = "新增用户信息")
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Users> addUsers(
            @ApiParam(value = "用户信息", required = true) @RequestBody Users users) {
        String resultCode = iUsersService.save(users);
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "更新用户信息")
    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Users> updateUsers(
            @ApiParam(value = "用户信息", required = true) @RequestBody Users users) {
        String resultCode = iUsersService.update(users);
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "删除用户信息")
    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Users> deleteUsers(@ApiParam(value = "用户id", required = false) @RequestParam(name = "id", required = false) String id) {
        String resultCode = iUsersService.delete(Arrays.asList(id));
        return IResultUtil.responseMsg(resultCode, null);
    }

}
