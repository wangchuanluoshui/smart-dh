package com.hyn.common;

import org.springframework.util.StringUtils;


/**
 * @version ： 1.0
 * @Title:：IResultUtil.java
 * @Package ：com.summit.homs.util
 * @Description： TODO
 * @author： hyn
 * @date： 2018年8月21日 下午2:23:20
 */
public class IResultUtil {

    public static <T> IResult<T> responseMsg(String respCode) {
        return responseMsg(respCode, null, null);
    }

    public static <T> IResult<T> responseMsg(String respCode, T obj) {
        return responseMsg(respCode, null, obj);
    }

    public static <T> IResult<T> responseMsg(String respCode, String respMsg, T obj) {
        IResult<T> result = new IResult<T>();
        if (StringUtils.isEmpty(respCode)) {
            result.setCode(ICodes.CODE_9999);
            result.setMsg(ICodes.respMsg.get(ICodes.CODE_9999));
            result.setData(null);
        } else {
            result.setCode(respCode);
            if (StringUtils.isEmpty(respMsg)) {
                respMsg = ICodes.respMsg.get(respCode);
            }
            result.setMsg(respMsg);
            result.setData(obj);
        }
        return result;
    }

}
