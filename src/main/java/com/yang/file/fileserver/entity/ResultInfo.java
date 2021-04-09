package com.yang.file.fileserver.entity;

import lombok.Data;

import java.util.HashMap;

@Data
public class ResultInfo extends HashMap<String, Object> {

    public static ResultInfo success() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.put("status", 0);
        return resultInfo;
    }

    public static ResultInfo error() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.put("status", -1);
        resultInfo.put("msg", "文件服务器异常");
        return resultInfo;
    }

    public static ResultInfo error(int code, String msg) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.put("status", code);
        resultInfo.put("msg", msg);
        return resultInfo;
    }
}
