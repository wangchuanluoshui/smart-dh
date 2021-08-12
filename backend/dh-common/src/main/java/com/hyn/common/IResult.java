package com.hyn.common;


import io.swagger.annotations.ApiModelProperty;

/**
 * @param <T>
 * @version ： 1.0
 * @Title:：IResult.java
 * @Package ：com.summit.homs.util
 * @Description： TODO
 * @author： hyn
 * @date： 2018年8月21日 下午2:23:03
 */
public class IResult<T> {
    /**
     * 错误码
     */
    @ApiModelProperty(value = "返回码", name = "code", example = "0000")
    private Object code;

    /**
     * 提示信息
     */
    @ApiModelProperty(value = "返回信息", name = "msg", example = "操作成功")
    private String msg;

    /**
     * 具体内容
     */
    @ApiModelProperty(value = "返回数据", name = "data")
    private T data;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
