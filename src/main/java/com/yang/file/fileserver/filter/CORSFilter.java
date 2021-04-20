package com.yang.file.fileserver.filter;

import com.yang.file.fileserver.config.FileConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration
public class CORSFilter implements Filter {

    @Resource
    FileConfig fileConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("CORS 跨域配置：" + fileConfig.isCors());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (fileConfig.isCors()) {
            HttpServletResponse res = (HttpServletResponse) servletResponse;
            // 设置允许Cookie
//        res.addHeader("Access-Control-Allow-Credentials", "true");
            // 允许http://www.xxx.com域（自行设置，这里只做示例）发起跨域请求
            res.addHeader("Access-Control-Allow-Origin", "*");
            // 设置允许跨域请求的方法
            res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            // 允许跨域请求包含content-type
            res.addHeader("Access-Control-Allow-Headers", "Content-Type,Content-Disposition,fileName");
            res.addHeader("Access-Control-Expose-Headers", "Content-Type,Content-Disposition,fileName");
            if (((HttpServletRequest) servletRequest).getMethod().equals("OPTIONS")) {
                servletResponse.getWriter().println("ok");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
