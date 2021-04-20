package com.yang.file.fileserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class FileConfig {

    // 文件路径
    @Value("${file-server.path}")
    private String path;

    @Value("${file-server.cors}")
    private boolean cors;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCors() {
        return cors;
    }
}
