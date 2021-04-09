package com.yang.file.fileserver.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;

@Data
public class ResultInfo {

    @ApiModelProperty(value = "请求状态码")
    private int status;

    @ApiModelProperty(value = "请求信息")
    private String msg;

    @ApiModelProperty(value = "返回数据")
    private Object data;

    public static ResultInfo success() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setStatus(0);
        return resultInfo;
    }

    public static ResultInfo error() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setStatus(-1);
        resultInfo.setMsg("文件服务器异常");
        return resultInfo;
    }

    public static ResultInfo error(int code, String msg) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setStatus(code);
        resultInfo.setMsg(msg);
        return resultInfo;
    }
}
