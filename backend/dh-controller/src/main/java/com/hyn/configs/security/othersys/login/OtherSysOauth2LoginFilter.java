package com.hyn.configs.security.othersys.login;

import com.hyn.auth.AuthTargetRequest;
import com.hyn.pojo.Users;
import com.hyn.service.user.IUsersService;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

@Component
public class OtherSysOauth2LoginFilter extends AbstractAuthenticationProcessingFilter {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final static String DEFAULT_PASSWORD = "123456";

    public OtherSysOauth2LoginFilter(AuthTargetRequest authTargetRequest) {
        super(new AntPathRequestMatcher("/oauth/callback/**"));
        this.authTargetRequest = authTargetRequest;
    }

    private final AuthTargetRequest authTargetRequest;

    @Autowired
    private AuthStateCache authStateCache;

    @Autowired
    private IUsersService iUsersService;

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        logger.info("OtherSysOauth2LoginFilter start...");
        UsernamePasswordAuthenticationToken token = null;

        // 解析地址，获得第三方平台syscode
        String path = request.getServletPath();
        String[] sysArr = path.split("/oauth/callback/");
        String sys = sysArr[sysArr.length - 1];
        // 判断当前系统是否支持该平台登陆
        boolean contains = Arrays.asList(Oauth2LoginConstant.SYS_SOURCE).contains(sys);
        if (!contains) {
            throw new AuthenticationServiceException(
                    "暂不支持此系统登录（This system login is not currently supported）");
        }
        // 构造AuthRequest
        AuthRequest authRequest = authTargetRequest.getAuthRequest(sys);
        if (authRequest == null) {
            throw new AuthenticationServiceException(
                    "暂不支持此系统登录（This system login is not currently supported）");
        }
        AuthResponse<AuthUser> authResponse = authRequest.login(this.getAuthCallback(request));
        if (authResponse.ok()) {
            // 获取第三方登陆信息成功
            AuthUser data = authResponse.getData();
            // 用户id 一般是唯一的。建议通过uuid + source的方式唯一确定一个用户，这样可以解决用户身份归属的问题。
            String id = data.getUuid();

            Users user = iUsersService.findBySourceTypeAndSourceUuid(sys, id);
            if (user == null) {
                user = convertAuthUserToUsers(data, sys);
                iUsersService.save(user);
            }
            token = new UsernamePasswordAuthenticationToken(user.getUserName(), DEFAULT_PASSWORD);
        }
        return this.getAuthenticationManager().authenticate(token);
    }

    private AuthCallback getAuthCallback(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        AuthCallback authCallback = new AuthCallback();
        if (parameterMap.get("code")!=null && parameterMap.get("code").length > 0) {
            authCallback.setCode(parameterMap.get("code")[0]);
        }
        if (parameterMap.get("auth_code")!=null && parameterMap.get("auth_code").length > 0) {
            authCallback.setAuth_code(parameterMap.get("auth_code")[0]);
        }
        if (parameterMap.get("state")!=null && parameterMap.get("state").length > 0) {
            authCallback.setState(parameterMap.get("state")[0]);
        }
        if (parameterMap.get("authorization_code")!=null && parameterMap.get("authorization_code").length > 0) {
            authCallback.setAuthorization_code(parameterMap.get("authorization_code")[0]);
        }
        if (parameterMap.get("oauth_token")!=null && parameterMap.get("oauth_token").length > 0) {
            authCallback.setOauth_token(parameterMap.get("oauth_token")[0]);
        }
        if (parameterMap.get("oauth_verifier")!=null && parameterMap.get("oauth_verifier").length > 0) {
            authCallback.setOauth_verifier(parameterMap.get("oauth_verifier")[0]);
        }
        return authCallback;
    }

    private Users convertAuthUserToUsers(AuthUser authUser, String sourceType) {
        Users users = iUsersService.createDefaultUser();
        users.setUserName(StringUtils.isEmpty(authUser.getUsername())?users.getUserName():authUser.getUsername());
        users.setFacePath(authUser.getAvatar());
        users.setSourceUuid(authUser.getUuid());
        users.setSourceType(sourceType);
        users.setEmail(StringUtils.isEmpty(authUser.getEmail())?users.getEmail():authUser.getEmail());
        users.setNickName(StringUtils.isEmpty(authUser.getNickname())?users.getNickName():authUser.getNickname());
        users.setSex(Integer.valueOf(authUser.getGender().getCode()));
        return users;
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

}
