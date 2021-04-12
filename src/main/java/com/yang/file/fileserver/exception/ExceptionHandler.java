package com.yang.file.fileserver.exception;

import com.yang.file.fileserver.entity.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResultInfo handleException(Exception e) {
        log.error("<----请求过程异常-Exception");
        e.printStackTrace();
        return ResultInfo.error();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
    public ResultInfo handleException(IOException e) {
        log.error("<----请求过程异常-IOException");
        return ResultInfo.error(-3, "文件传输异常");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileEmptyException.class)
    public ResultInfo handleException(FileEmptyException e) {
        log.error("<----请求过程异常-FileEmptyException");
        return ResultInfo.error(-2, "文件为空");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileNotFoundException.class)
    public ResultInfo handleException(FileNotFoundException e) {
        log.error("<----请求过程异常-FileNotFoundException");
        return ResultInfo.error(-4, "文件未找到");
    }

}
