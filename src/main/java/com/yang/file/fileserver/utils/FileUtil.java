package com.yang.file.fileserver.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FileUtil {

    /**
     * 获取当前日期
     * @return
     */
    public String getYYYYMMDD() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 根据当前文件名获取新文件名，防止名称重复
     * @param dirPath
     * @param dirName
     * @param fileName
     * @return
     */
    public String getNewFilePath(String dirPath, String dirName, String fileName) {
        File dir = new File(dirPath, dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 文件存在，则需要添加后缀名
        String filePath = "";
        do {
            String name = "";
            if (fileName.lastIndexOf(".") != -1) {
                // 存在点，需要分割
                name = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
            } else {
                // 不存在点，直接添加
                name = UUID.randomUUID().toString();
            }
            filePath = dirPath + "/" + dirName + "/" + name;

            File file = new File(filePath);
            if (!file.exists()) {
                break;
            }
        } while (true);
        return filePath;
    }

    /**
     * 保存文件至指定路径
     * @param bytes
     * @param newFilePath
     * @return
     */
    public void uploadFile(byte[] bytes, String newFilePath) throws IOException {
        try(FileOutputStream out = new FileOutputStream(newFilePath)) {
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    public List<Map<String, Object>> listAll(File dirs) {
        List<Map<String, Object>> list = new ArrayList<>();
        File files[] = dirs.listFiles();
        for (File file :
                files) {
            if (file.isDirectory()) {
                List<Map<String, Object>> l = listAll(file);
                list.addAll(l);
            } else {
                Map<String, Object> res = new HashMap<>();
                res.put("fileName", file.getName());
                res.put("filePath", file.getAbsolutePath());
                res.put("fileSize", file.length());
                list.add(res);
            }
        }
        return list;
    }
}
