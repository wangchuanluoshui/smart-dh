package com.hyn.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.hyn.VO.AuthUrl;
import com.hyn.auth.AuthTargetRequest;
import com.hyn.auth.Response;
import com.hyn.auth.service.AuthUserService;
import com.hyn.common.ICodes;
import com.hyn.common.IResult;
import com.hyn.common.IResultUtil;
import com.hyn.configs.security.othersys.login.Oauth2LoginConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2019/2/19 9:28
 * @since 1.8
 */
@Slf4j
@Api(value = "base", tags = "系统基础")
@Controller
@RequestMapping("/oauth")
public class RestAuthController {

    @Autowired
    private AuthTargetRequest authTargetRequest;

    @Autowired
    private AuthUserService authUserService;

    @ApiOperation(value = "第三方登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResult renderAuth(@ApiParam(value = "其他系统代码", required = true) @RequestParam(name = "sourcesyscode", required = true) String sourcesyscode, HttpServletResponse response) throws IOException {
        log.info("进入render：" + sourcesyscode);
        boolean contains = Arrays.asList(Oauth2LoginConstant.SYS_SOURCE).contains(sourcesyscode);
        if (!contains) {
            return IResultUtil.responseMsg(ICodes.CODE_9979);
        }
        AuthRequest authRequest = authTargetRequest.getAuthRequest(sourcesyscode);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        log.info(authorizeUrl);
        AuthUrl authUrl=new AuthUrl();
        authUrl.setAuthUrl(authorizeUrl);
        return IResultUtil.responseMsg(ICodes.CODE_0000,authUrl);
    }

    /**
     * oauth平台中配置的授权回调地址，以本项目为例，在创建github授权应用时的回调地址应为：http://127.0.0.1:8443/oauth/callback/github
     */
    @RequestMapping("/callback/{source}")
    public ModelAndView login(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request) {
        log.info("进入callback：" + source + " callback params：" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = authTargetRequest.getAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        log.info(JSONObject.toJSONString(response));

        if (response.ok()) {
            authUserService.save(response.getData());
            return new ModelAndView("redirect:/users");
        }

        Map<String, Object> map = new HashMap<>(1);
        map.put("errorMsg", response.getMsg());

        return new ModelAndView("error", map);
    }

    @RequestMapping("/revoke/{source}/{uuid}")
    @ResponseBody
    public Response revokeAuth(@PathVariable("source") String source, @PathVariable("uuid") String uuid) throws IOException {
        AuthRequest authRequest = authTargetRequest.getAuthRequest(source.toLowerCase());

        AuthUser user = authUserService.getByUuid(uuid);
        if (null == user) {
            return Response.error("用户不存在");
        }
        AuthResponse<AuthToken> response = null;
        try {
            response = authRequest.revoke(user.getToken());
            if (response.ok()) {
                authUserService.remove(user.getUuid());
                return Response.success("用户 [" + user.getUsername() + "] 的 授权状态 已收回！");
            }
            return Response.error("用户 [" + user.getUsername() + "] 的 授权状态 收回失败！" + response.getMsg());
        } catch (AuthException e) {
            return Response.error(e.getErrorMsg());
        }
    }

    @RequestMapping("/refresh/{source}/{uuid}")
    @ResponseBody
    public Object refreshAuth(@PathVariable("source") String source, @PathVariable("uuid") String uuid) {
        AuthRequest authRequest = authTargetRequest.getAuthRequest(source.toLowerCase());

        AuthUser user = authUserService.getByUuid(uuid);
        if (null == user) {
            return Response.error("用户不存在");
        }
        AuthResponse<AuthToken> response = null;
        try {
            response = authRequest.refresh(user.getToken());
            if (response.ok()) {
                user.setToken(response.getData());
                authUserService.save(user);
                return Response.success("用户 [" + user.getUsername() + "] 的 access token 已刷新！新的 accessToken: " + response.getData().getAccessToken());
            }
            return Response.error("用户 [" + user.getUsername() + "] 的 access token 刷新失败！" + response.getMsg());
        } catch (AuthException e) {
            return Response.error(e.getErrorMsg());
        }
    }


}
