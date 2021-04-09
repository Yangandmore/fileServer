package com.yang.file.fileserver;

import com.yang.file.fileserver.config.FileConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class FileserverApplication implements CommandLineRunner {

    @Resource
    FileConfig fileConfig;

    public static void main(String[] args) {
        SpringApplication.run(FileserverApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 1 文件路径配置是否存在
        if (fileConfig.getPath().isEmpty()) {
            // 获取当前路径
            String path = System.getProperty("user.dir");
            // 设置路径
            fileConfig.setPath(path + "/file_server");
        }

        // 2 文件路径是否存在

        // 3 创建文件路径
    }
}
