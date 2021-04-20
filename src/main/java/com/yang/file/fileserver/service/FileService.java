package com.yang.file.fileserver.service;

import com.yang.file.fileserver.config.FileConfig;
import com.yang.file.fileserver.entity.ResultInfo;
import com.yang.file.fileserver.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
            log.info("文件路径不存在，正在创建");
            dir.mkdirs();
        }

        // 设置文件的路径
        String filePath = fileUtil.getNewFilePath(fileConfig.getPath(), fileUtil.getYYYYMMDD(), file.getOriginalFilename());
        File f = new File(filePath);
        if (!f.exists()) {
            log.info("文件路径不存在，正在创建");
            f.createNewFile();
        }

        // 保存文件
        fileUtil.uploadFile(file.getBytes(), filePath);
        log.info("附件上传成功，获取文件路径:" + filePath);
        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("fileName", file.getOriginalFilename());
        resultInfo.put("filePath", filePath);
        resultInfo.put("fileSize", file.getSize());
        return resultInfo;
    }

    // 删
    public void deleteDir(Map<String, String> req) throws Exception {
        if (req == null) {
            // 删除所有文件
            File dir = new File(fileConfig.getPath());
            if(dir.exists()) {
                log.info("开始删除文件夹:"+dir.getAbsolutePath());
                fileUtil.deleteDirs(dir);
            } else {
                throw new FileNotFoundException();
            }
        } else {
            // 删除指定文件夹
            File file = null;
            if (req.containsKey("filePath")) {
                String filePath = req.get("filePath");
                file = new File(filePath);
            } else if (req.containsKey("fileName")) {
                String fileName = req.get("fileName");
                String filePath = fileUtil.searchDir(new File(fileConfig.getPath()), fileName);
                if (filePath.equals("")) {
                    throw new FileNotFoundException();
                }
                file = new File(filePath);
            } else {
                throw new Exception();
            }
            if (file.exists()) {
                log.info("开始删除文件夹:"+file.getAbsolutePath());
                fileUtil.deleteDirs(file);
                file.delete();
            } else {
                throw new FileNotFoundException();
            }
        }
    }

    public void deleteFile(Map<String, String> req) throws Exception {
        // 删除指定文件夹
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
        if (file.exists()) {
            log.info("开始删除文件:"+file.getAbsolutePath());
            file.delete();
        } else {
            throw new FileNotFoundException();
        }
    }

    // 改
    public Map<String, Object> updateFile(MultipartFile newFile, String fileName, String filePath) throws Exception {
        File rootDir = new File(fileConfig.getPath());

        // 保存路径不存在
        if (!rootDir.exists()) {
            log.info("文件路径不存在，正在创建");
            rootDir.mkdirs();
        }

        // 获取需要替换文件的文件信息
        File oldFile = null;
        if (filePath != null) {
            oldFile = new File(filePath);
        } else if (fileName != null) {
            String oldFilePath = fileUtil.searchFile(new File(fileConfig.getPath()), fileName);
            if (oldFilePath.equals("")) {
                throw new FileNotFoundException();
            }
            oldFile = new File(oldFilePath);
        } else {
            throw new Exception();
        }

        if (oldFile.exists()) {
            // 先保存新文件
            // 设置文件的路径
            String newFilePath = fileUtil.getNewFilePath(fileConfig.getPath(), fileUtil.getYYYYMMDD(), newFile.getOriginalFilename());
            File f = new File(newFilePath);
            if (!f.exists()) {
                log.info("新文件路径不存在，正在创建");
                f.createNewFile();
            }
            // 保存文件
            fileUtil.uploadFile(newFile.getBytes(), newFilePath);
            log.info("新附件上传成功，获取文件路径:" + newFilePath);

            // 后删除
            oldFile.delete();

            Map<String, Object> resultInfo = new HashMap<>();
            resultInfo.put("fileName", newFile.getOriginalFilename());
            resultInfo.put("filePath", f.getAbsolutePath());
            resultInfo.put("fileSize", f.length());
            return resultInfo;

        } else {
            throw new FileNotFoundException();
        }
    }

    // 查
    public ResultInfo listTree() {
        ResultInfo resultInfo = new ResultInfo();
        File dir = new File(fileConfig.getPath());

        // 保存路径不存在
        if (!dir.exists()) {
            log.info("文件路径不存在，正在创建");
            dir.mkdirs();
        }

        List<Map<String, Object>> list = fileUtil.listTree(dir);
        resultInfo.setData(list);
        return resultInfo;
    }

    public ResultInfo listAll() {
        ResultInfo resultInfo = new ResultInfo();
        File dir = new File(fileConfig.getPath());

        // 保存路径不存在
        if (!dir.exists()) {
            log.info("文件路径不存在，正在创建");
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
            log.info("文件路径不存在，正在创建");
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
            log.info("下载文件参数信息未找到");
            return null;
        }
        if (!file.exists()) {
            log.info("文件路径不存在,无法下载");
            return null;
        }
        return file;
    }

}
