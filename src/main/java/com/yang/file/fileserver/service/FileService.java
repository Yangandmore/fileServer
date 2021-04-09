package com.yang.file.fileserver.service;

import com.yang.file.fileserver.config.FileConfig;
import com.yang.file.fileserver.entity.ResultInfo;
import com.yang.file.fileserver.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
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

        Map<String, Object> resultInfo = new HashMap<>();
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
        resultInfo.setData(list);
        return resultInfo;
    }

    public ResultInfo dirAll() {
        ResultInfo resultInfo = new ResultInfo();
        File dir = new File(fileConfig.getPath());

        // 保存路径不存在
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<Map<String, Object>> list = fileUtil.dirAll(dir);
        resultInfo.setData(list);
        return resultInfo;
    }

    // 查找文件
    public Map<String, Object> searchFile(Map<String, String> req) throws Exception {
        File file = null;
        if (req.containsKey("filePath")) {
            String filePath = req.get("filePath");
            file = new File(filePath);
        } else if (req.containsKey("fileName")) {
            String fileName = req.get("fileName");
            String filePath = fileUtil.searchFile(new File(fileConfig.getPath()), fileName);
            if (filePath.equals("")) {
                throw new FileNotFoundException();
            }
            file = new File(filePath);
        } else {
            throw new Exception();
        }

        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("fileName", file.getName());
        resultInfo.put("filePath", file.getAbsolutePath());
        resultInfo.put("fileSize", file.length());
        return resultInfo;
    }

    // 下载
    public File getDownloadFile(Map<String, String> req) {
        File file = null;
        if (req.containsKey("filePath")) {
            String filePath = req.get("filePath");
            file = new File(filePath);
        } else if (req.containsKey("fileName")) {
            String fileName = req.get("fileName");
            String filePath = fileUtil.searchFile(new File(fileConfig.getPath()), fileName);
            if (filePath.equals("")) {
                return null;
            }
            file = new File(filePath);
        } else {
            return null;
        }
        if (!file.exists()) {
            return null;
        }
        return file;
    }

}
