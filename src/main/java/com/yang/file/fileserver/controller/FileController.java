package com.yang.file.fileserver.controller;

import com.yang.file.fileserver.entity.ResultInfo;
import com.yang.file.fileserver.exception.FileEmptyException;
import com.yang.file.fileserver.service.FileService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
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
        log.info("---->请求上传单个文件");
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
        log.info("<----上传单个文件成功");
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
        log.info("---->请求上传多个文件");
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
        log.info("<----上传多个文件成功");
        return res;
    }

    // 删
    @ApiOperation("删除所有文件")
    @ApiResponses({
            @ApiResponse(code = 0, message = "上传成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @DeleteMapping("/delete/all")
    public ResultInfo deleteAll() throws Exception {
        log.info("---->请求删除所有文件");
        ResultInfo resultInfo = new ResultInfo();

        fileService.deleteDir(null);

        resultInfo.setMsg("删除完成");
        resultInfo.setStatus(0);
        log.info("<----删除所有文件成功");
        return resultInfo;
    }

    @ApiOperation("删除指定文件夹")
    @ApiImplicitParams({
            @ApiImplicitParam("查找文件的名称或文件路径:fileName 或者 filePath"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "上传成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @DeleteMapping("/delete/dirs")
    public ResultInfo deleteDirs(@RequestBody Map<String, String> req) throws Exception {
        log.info("---->请求删除文件夹");
        ResultInfo resultInfo = new ResultInfo();

        fileService.deleteDir(req);

        resultInfo.setMsg("删除完成");
        resultInfo.setStatus(0);
        log.info("<----删除文件夹成功");
        return resultInfo;
    }

    @ApiOperation("删除指定单个文件")
    @ApiImplicitParams({
            @ApiImplicitParam("查找文件的名称或文件路径:fileName 或者 filePath"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "上传成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @DeleteMapping("/delete/file")
    public ResultInfo deleteFile(@RequestBody Map<String, String> req) throws Exception {
        log.info("---->请求删除文件");
        ResultInfo resultInfo = new ResultInfo();

        fileService.deleteFile(req);

        resultInfo.setMsg("删除完成");
        resultInfo.setStatus(0);
        log.info("<----删除文件成功");
        return resultInfo;
    }

    @ApiOperation("删除指定多个文件")
    @ApiImplicitParams({
            @ApiImplicitParam("查找文件的名称或文件路径:fileName 或者 filePath"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "上传成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @DeleteMapping("/delete/files")
    public ResultInfo deleteFiles(@RequestBody List<Map<String, String>> req) throws Exception {
        log.info("---->请求删除文件");
        ResultInfo resultInfo = new ResultInfo();
        for (Map<String, String> map :
                req) {
            fileService.deleteFile(map);
        }

        resultInfo.setMsg("删除完成");
        resultInfo.setStatus(0);
        log.info("<----删除文件成功");
        return resultInfo;
    }

    // 改
    @ApiOperation("修改指定的文件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "需要被覆盖的文件名称", required = false),
            @ApiImplicitParam(value = "需要被覆盖的文件路径", required = false),
            @ApiImplicitParam(value = "新文件")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "请求成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @PostMapping("/updateFile")
    public ResultInfo updateFile(@RequestParam(value = "fileName", required = false) String fileName, @RequestParam(value = "filePath", required = false) String filePath, @RequestParam("file") MultipartFile file) throws Exception {
        log.info("---->请求修改文件");
        Map<String, Object> data = fileService.updateFile(file, fileName, filePath);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(data);
        resultInfo.setMsg("修改完成");
        resultInfo.setStatus(0);
        log.info("<----修改文件成功");
        return resultInfo;
    }

    // 查
    @ApiOperation("查询文件树结构")
    @ApiResponses({
            @ApiResponse(code = 0, message = "请求成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @GetMapping("/listTree")
    public ResultInfo getFileTree() {
        log.info("---->请求获取文件结构树");
        ResultInfo res = fileService.listTree();
        res.setMsg("请求成功");
        res.setStatus(0);
        log.info("<----获取文件结构树成功");
        return res;
    }

    // 获取所有文件
    @ApiOperation("查询所有文件信息")
    @ApiResponses({
            @ApiResponse(code = 0, message = "请求成功", response = ResultInfo.class),
            @ApiResponse(code = -1, message = "文件服务器异常", response = ResultInfo.class),
            @ApiResponse(code = -2, message = "文件为空", response = ResultInfo.class),
            @ApiResponse(code = -3, message = "文件传输异常", response = ResultInfo.class),
    })
    @GetMapping("/list")
    public ResultInfo list() {
        log.info("---->请求获取所有文件");
        ResultInfo res = fileService.listAll();
        res.setMsg("请求成功");
        res.setStatus(0);
        log.info("<----获取所有文件成功");
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
        log.info("---->请求获取文件夹树");
        ResultInfo res = fileService.dirAll();
        res.setMsg("请求成功");
        res.setStatus(0);
        log.info("<----获取文件夹树成功");
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
        log.info("---->请求搜索文件");
        Map<String, Object> rsp = fileService.searchFile(req);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(rsp);
        resultInfo.setStatus(0);
        resultInfo.setMsg("请求成功");
        log.info("<----搜索文件成功");
        return resultInfo;
    }

    // 下载
    @ApiOperation("下载文件")
    @ApiImplicitParams({
            @ApiImplicitParam("下载文件的文件名称或者文件路径:fileName 或者 filePath"),
    })
    @PostMapping("/downloadFile")
    public void downloadFile(@RequestBody Map<String, String> req, HttpServletResponse response) {
        log.info("---->请求下载文件");
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
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Expose-Headers", "Content-Type,Content-Disposition,attachment");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,Content-Disposition,attachment");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("<----下载文件成功");
    }

    @ApiOperation("下载多个文件并压缩")
    @ApiImplicitParams({
            @ApiImplicitParam("下载文件的文件名称或者文件路径:fileName 或者 filePath"),
    })
    @PostMapping("/downloadZipFile")
    public void downloadZipFile(@RequestBody List<Map<String, String>> req, HttpServletResponse response) {
        log.info("---->请求下载压缩文件");
        List<File> files = new ArrayList<>();
        for (Map<String, String> map :
                req) {
            File file = fileService.getDownloadFile(map);
            if (file == null) return;
            files.add(file);
        }

        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("压缩包.zip", "UTF-8"));
            System.out.println(URLEncoder.encode("压缩包.zip", "UTF-8"));
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Expose-Headers", "Content-Type,Content-Disposition,attachment");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,Content-Disposition,attachment");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ;
        }
        // 告知浏览器文件的大小
        response.setContentType("application/zip;charset=utf-8");
        BufferedInputStream bis = null;
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            for (File file :
                    files) {
                ZipEntry ze = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(ze);

                bis = new BufferedInputStream(new FileInputStream(file),1024 * 10);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = bis.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, len);
                }
                bis.close();
            }

            zipOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    return;
                }
            }
            return;
        }


        log.info("<----下载压缩文件成功");
    }
}
