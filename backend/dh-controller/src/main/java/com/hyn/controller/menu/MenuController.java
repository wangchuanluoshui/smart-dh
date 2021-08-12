package com.hyn.controller.menu;

import com.hyn.VO.MenuVO;
import com.hyn.common.*;
import com.hyn.pojo.Menu;
import com.hyn.service.menu.IMenuService;
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
@Api(value = "menu", tags = "菜单管理模块")
@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    IMenuService iMenuService;

    @ApiOperation(value = "条件分页查询,条件")
    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PageReponse findAllOfPage(
            @ApiParam(value = "菜单名称", required = false) @RequestParam(name = "name", required = false) String name,
            @ApiParam(value = "排序", required = false) @RequestParam(name = "sorter", required = false) String sorter,
            @ApiParam(value = "页码", required = false) @RequestParam(name = "current", required = false) Integer current,
            @ApiParam(value = "页大小", required = false) @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        IPageRequest iPageRequest = new IPageRequest(sorter, current, pageSize);
        PageReponse datepage = iMenuService.getNameList(name, iPageRequest.getRequestPage());
        return datepage;
    }

    @ApiOperation(value = "新增菜单信息")
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Menu> addMenu(
            @ApiParam(value = "菜单信息", required = true) @RequestBody Menu Menu) {
        String resultCode = iMenuService.save(Menu);
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "更新菜单信息")
    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Menu> updateMenu(
            @ApiParam(value = "角色信息", required = true) @RequestBody Menu Menu) {
        String resultCode = iMenuService.update(Menu);
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "删除菜单信息")
    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<Menu> deleteMenu(
            @ApiParam(value = "角色信息", required = true) @RequestParam(name = "id", required = false) String id) {
        String resultCode = iMenuService.delete(Arrays.asList(id));
        return IResultUtil.responseMsg(resultCode, null);
    }

    @ApiOperation(value = "查询menu列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<List<MenuVO>> findAllOfPage() {
        List<MenuVO> menuList = iMenuService.getMenuList();
        return IResultUtil.responseMsg(ICodes.CODE_0000, menuList);
    }

}
