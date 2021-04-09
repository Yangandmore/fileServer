package com.yang.file.fileserver.exception;

import com.yang.file.fileserver.entity.ResultInfo;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResultInfo handleException(Exception e) {
        return ResultInfo.error();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
    public ResultInfo handleException(IOException e) {
        return ResultInfo.error(-3, "文件传输异常");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileEmptyException.class)
    public ResultInfo handleException(FileEmptyException e) {
        return ResultInfo.error(-2, "文件为空");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileNotFoundException.class)
    public ResultInfo handleException(FileNotFoundException e) {
        return ResultInfo.error(-4, "文件未找到");
    }

}
