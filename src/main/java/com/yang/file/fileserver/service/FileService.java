package com.yang.file.fileserver.service;

import com.yang.file.fileserver.config.FileConfig;
import com.yang.file.fileserver.entity.ResultInfo;
import com.yang.file.fileserver.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {

    @Resource
    FileConfig fileConfig;

    @Resource
    FileUtil fileUtil;

    // 增
    public ResultInfo uploadFile(MultipartFile file) throws IOException {
        File dir = new File(fileConfig.getPath());

        // 保存路径不存在
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 设置文件的路径
        String filePath = fileUtil.getNewFilePath(fileConfig.getPath(), fileUtil.getYYYYMMDD(), file.getOriginalFilename());
        File f = new File(filePath);
        if (!f.exists()) {
            f.createNewFile();
        }

        // 保存文件
        fileUtil.uploadFile(file.getBytes(), filePath);

        ResultInfo resultInfo = new ResultInfo();
        resultInfo.put("fileName", file.getOriginalFilename());
        resultInfo.put("filePath", filePath);
        resultInfo.put("fileSize", file.getSize());
        return resultInfo;
    }

    // 删

    // 改

    // 查
    public ResultInfo listAll() {
        ResultInfo resultInfo = new ResultInfo();
        File dir = new File(fileConfig.getPath());

        // 保存路径不存在
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<Map<String, Object>> list = fileUtil.listAll(dir);
        resultInfo.put("data", list);
        return resultInfo;
    }

    public ResultInfo dirAll() {
        ResultInfo resultInfo = new ResultInfo();

        return resultInfo;
    }

}
