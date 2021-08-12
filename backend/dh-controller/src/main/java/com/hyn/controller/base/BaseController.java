package com.hyn.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyn.VO.CurrentUserVO;
import com.hyn.common.ICodes;
import com.hyn.common.IResult;
import com.hyn.common.IResultUtil;
import com.hyn.fdfs.service.FdfsService;
import com.hyn.pojo.Users;
import com.hyn.service.role.IRoleService;
import com.hyn.utils.MultipartFileToFileUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/")
public class BaseController {

    @Autowired
    FdfsService fdfsService;

    @Autowired
    IRoleService iRoleService;

    @Value("${system-bussines.bmpn-file-path}")
    private String sysBpmnFilePath;

    @ApiOperation(value = "登录功能，以获取session")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void login(
            @ApiParam(value = "用戶信息,example:{\"username\":\"hyn\",\"password\":\"123456\"}", required = true) @RequestBody String jsonstr) {
    }

    @ApiOperation(value = "退出用户功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return IResultUtil.responseMsg(ICodes.CODE_9980);
    }

    @ApiOperation(value = "查询当前用户信息")
    @RequestMapping(value = "/currentUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult getCurrentUser(@ApiIgnore @SessionAttribute("LOGIN_USER") Users users) {
        CurrentUserVO currentUserVO = new CurrentUserVO();
        currentUserVO.setName(users.getNickName());
        currentUserVO.setUserid(users.getId());
        currentUserVO.setFacePath(users.getFacePath());
        String[] access = iRoleService.getAccess(users.getRoleId());
        List accessVO = new ArrayList();
        for (String acces : access) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(acces, new Boolean(true));
            accessVO.add(acces);
        }
        currentUserVO.setAccess(accessVO);
        return IResultUtil.responseMsg(ICodes.CODE_0000, currentUserVO);
    }

    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/upload-file", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String result = ICodes.CODE_0000;
        String fileFullPath = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String filepath = sysBpmnFilePath + "/" + sdf.format(new Date());
            fileFullPath = MultipartFileToFileUtils.saveFile(multipartFile, filepath);
        } catch (Exception e) {
            result = ICodes.CODE_9999;
            e.printStackTrace();
        }
        return IResultUtil.responseMsg(result, fileFullPath);
    }

    private List<Users> userList = new ArrayList<>();

    @ApiOperation(value = "内存溢出测试")
    @RequestMapping(value = "/creat_OutOfMemory", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void OutOfMemoryTest() {
        int i = 0;
        while (true) {
            Users users = new Users();
            users.setId(UUID.randomUUID().toString());
            userList.add(users);
        }
    }

    @ApiOperation(value = "认证")
    @RequestMapping(value = "/login/outLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object outLogin() {
        return JSON.parse("{\"data\":{},\"success\":true}");
    }

}
