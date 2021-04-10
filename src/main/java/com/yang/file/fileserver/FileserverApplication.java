package com.yang.file.fileserver;

import com.yang.file.fileserver.config.FileConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@SpringBootApplication
public class FileserverApplication implements CommandLineRunner {

    @Resource
    FileConfig fileConfig;

    public static void main(String[] args) {
        SpringApplication.run(FileserverApplication.class, args);
        log.info("<---------------文件服务启动成功--------------->");
    }

    @Override
    public void run(String... args) throws Exception {
        // 1 文件路径配置是否存在
        if (fileConfig.getPath().isEmpty()) {
            // 获取当前路径
            String path = System.getProperty("user.dir");
            log.info("根目录配置未找到，正在配置默认根目录文件夹:"+path + "/file_server");
            // 设置路径
            fileConfig.setPath(path + "/file_server");
        }

        // 2 文件路径是否存在
        File rootDir = new File(fileConfig.getPath());
        if (!rootDir.exists()) {
            log.info("正在创建根目录文件夹:"+rootDir.getAbsolutePath());
            // 3 创建文件路径
            rootDir.mkdirs();
        } else {
            log.info("已找到根目录文件夹:"+rootDir.getAbsolutePath());
        }
    }
}
