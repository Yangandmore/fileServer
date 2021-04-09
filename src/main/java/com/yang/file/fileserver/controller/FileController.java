package com.yang.file.fileserver.controller;

import com.yang.file.fileserver.entity.ResultInfo;
import com.yang.file.fileserver.exception.FileEmptyException;
import com.yang.file.fileserver.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    // 增

    // 单个文件
    @PostMapping("/uploadFile")
    public ResultInfo addFile(@RequestParam("file") MultipartFile file) throws IOException, FileEmptyException {
        ResultInfo res = new ResultInfo();
        if (file.isEmpty()) {
            // 文件为空
            throw new FileEmptyException("文件为空");
        } else {
            List<ResultInfo> list = new ArrayList<>();
            list.add(fileService.uploadFile(file));
            res.put("data", list);
            res.put("msg", "上传成功");
            res.put("status", 0);
        }
        return res;
    }

    // 多文件上传
    @PostMapping("/uploadMoreFile")
    public ResultInfo addFiles(@RequestParam("files") MultipartFile[] files) throws FileEmptyException, IOException {
        ResultInfo res = new ResultInfo();
        List<ResultInfo> list = new ArrayList<>();
        for (MultipartFile file :
                files) {
            if (file.isEmpty()) {
                // 文件为空
                throw new FileEmptyException("文件为空");
            } else {
                ResultInfo r = fileService.uploadFile(file);
                list.add(r);
            }
        }
        res.put("data", list);
        res.put("msg", "上传成功");
        res.put("status", 0);
        return res;
    }

    // 删

    // 改

    // 查
    // 查询文件路径结构
    @GetMapping("/list")
    public ResultInfo list() {
        ResultInfo res = fileService.listAll();
        res.put("status", 0);
        res.put("msg", "请求成功");
        return res;
    }

    @GetMapping("/dir")
    public ResultInfo dirList() {
        ResultInfo resultInfo = fileService.dirAll();

        return resultInfo;
    }
}
