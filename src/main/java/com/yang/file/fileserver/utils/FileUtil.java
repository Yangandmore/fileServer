package com.yang.file.fileserver.utils;

import org.springframework.stereotype.Component;

import java.io.File;
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

    /**
     * 获取文件夹结构树
     * @param dirs
     * @return
     */
    public List<Map<String, Object>> listTree(File dirs) {
        List<Map<String, Object>> list = new ArrayList<>();
        File files[] = dirs.listFiles();
        for (File file :
                files) {
            if (file.isDirectory()) {
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> l = listTree(file);
                map.put("files", l);
                map.put("fileName", file.getName());
                list.add(map);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("fileName", file.getName());
                map.put("filePath", file.getAbsolutePath());
                map.put("fileSize", file.length());
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 获取所有文件信息
     * @param dirs
     * @return
     */
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

    /**
     * 获取所有文件夹信息
     * @param dirs
     * @return
     */
    public List<Map<String, Object>> dirAll(File dirs) {
        List<Map<String, Object>> list = new ArrayList<>();

        File files[] = dirs.listFiles();
        long fileSize = 0;
        int count = 0;
        for (File file :
                files) {
            if (file.isDirectory()) {
                List<Map<String, Object>> l = dirAll(file);

                Map<String, Object> res = new HashMap<>();
                res.put("dirName", file.getName());
                res.put("dirPath", file.getAbsolutePath());
                res.putAll(l.get(0));
                list.add(res);
            } else {
                fileSize += file.length();
                count++;
                if (count == files.length) {
                    Map<String, Object> res = new HashMap<>();
                    res.put("dirSize", fileSize);
                    list.add(res);
                }

            }
        }
        return list;
    }

    /**
     * 根据文件夹名称删除文件夹
     * @param dirs
     * @param dirName
     * @return
     */
    public String searchDir(File dirs, String dirName) {
        File files[] = dirs.listFiles();
        for (File file :
                files) {
            if (file.isDirectory()) {
                if (file.getName().equals(dirName)) {
                    return file.getAbsolutePath();
                }
                String path = searchFile(file, dirName);
                if (!path.equals("")) {
                    return path;
                }
            }
        }
        return "";
    }

    /**
     * 根据文件名查找文件路径
     * @param dirs
     * @param fileName
     * @return
     */
    public String searchFile(File dirs, String fileName) {
        File files[] = dirs.listFiles();
        for (File file :
                files) {
            if (file.isDirectory()) {
                String path = searchFile(file, fileName);
                if (!path.equals("")) {
                    return path;
                }
            } else {
                if (file.getName().equals(fileName)) {
                    // 找到该文件
                    return file.getAbsolutePath();
                }
            }
        }
        return "";
    }

    /**
     * 删除文件夹和文件夹内所有文件
     * @param dirs
     */
    public void deleteDirs(File dirs) {
        File[] files = dirs.listFiles();
        for (File file :
                files) {
            if (file.isDirectory()) {
                deleteDirs(file);
                file.delete();
            } else {
                file.delete();
            }
        }
    }
}
