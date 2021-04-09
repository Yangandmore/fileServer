package com.yang.file.fileserver.controller;

import com.yang.file.fileserver.entity.ResultInfo;
import com.yang.file.fileserver.exception.FileEmptyException;
import com.yang.file.fileserver.service.FileService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "文件接口")
public class FileController {

    @Autowired
    FileService fileService;


    // 增
    // 单个文件
    @ApiOperation("上传单个文件")
    @ApiImplicitParam("单个文件,需要使用file为key")
    @ApiResponses({
            @ApiResponse(code = 0, message = "上传成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @PostMapping("/uploadFile")
    public ResultInfo addFile(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException, FileEmptyException {
        ResultInfo res = new ResultInfo();
        if (file.isEmpty()) {
            // 文件为空
            throw new FileEmptyException("文件为空");
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(fileService.uploadFile(file));
            res.setData(list);
            res.setStatus(0);
            res.setMsg("上传成功");
        }
        return res;
    }

    // 多文件上传
    @ApiOperation("上传多个文件")
    @ApiImplicitParam("多个文件,需要使用files为key")
    @ApiResponses({
            @ApiResponse(code = 0, message = "上传成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @PostMapping("/uploadMoreFile")
    public ResultInfo addFiles(@RequestParam("files") MultipartFile[] files) throws FileEmptyException, IOException {
        ResultInfo res = new ResultInfo();
        List<Map<String, Object>> list = new ArrayList<>();
        for (MultipartFile file :
                files) {
            if (file.isEmpty()) {
                // 文件为空
                throw new FileEmptyException("文件为空");
            } else {
                Map<String, Object> r = fileService.uploadFile(file);
                list.add(r);
            }
        }
        res.setData(list);
        res.setStatus(0);
        res.setMsg("上传成功");
        return res;
    }

    // 删

    // 改

    // 查
    // 查询文件路径结构
    @ApiOperation("查询所有文件信息")
    @ApiResponses({
            @ApiResponse(code = 0, message = "请求成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @GetMapping("/list")
    public ResultInfo list() {
        ResultInfo res = fileService.listAll();
        res.setMsg("请求成功");
        res.setStatus(0);
        return res;
    }

    @ApiOperation("查询所有文件夹信息")
    @ApiResponses({
            @ApiResponse(code = 0, message = "请求成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @GetMapping("/dir")
    public ResultInfo dirList() {
        ResultInfo res = fileService.dirAll();
        res.setMsg("请求成功");
        res.setStatus(0);
        return res;
    }

    @ApiOperation("查找文件")
    @ApiImplicitParams({
            @ApiImplicitParam("查找文件的名称或文件路径:fileName 或者 filePath"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "请求成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
            @ApiResponse(code = -4, message = "文件未找到", response = ResultInfo.class),
    })
    @PostMapping("/searchFile")
    public ResultInfo searchFile(@RequestBody Map<String, String> req) throws Exception {
        Map<String, Object> rsp = fileService.searchFile(req);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(rsp);
        resultInfo.setStatus(0);
        resultInfo.setMsg("请求成功");
        return resultInfo;
    }

    // 下载
    @ApiOperation("下载文件")
    @ApiImplicitParams({
            @ApiImplicitParam("下载文件的文件名称或者文件路径:fileName 或者 filePath"),
    })
    @PostMapping("/downloadFile")
    public void downloadFile(@RequestBody Map<String, String> req, HttpServletResponse response) {

        File file = fileService.getDownloadFile(req);

        if (file == null) return;

        //
        try (
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        ) {
            byte[] buffer = new byte[bis.available()];
            bis.read(buffer);

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
