文件服务器
---

最小的文件服务器，没有数据库。配置好路径、java环境，即可使用。  
👉 支持配套前端[fileServer-react](https://github.com/Yangandmore/fileserver-react)提供页面管理。

* [x] springboot
* [x] log4j2
* [x] swagger2
* [x] 可配置跨域
* [ ] 加密
* [x] 更全面的文件管理功能
* [x] 更小巧的文件服务器大小

# API

### 增
* 上传单个文件
* 上传多个文件

### 删
* 删除指定文件
* 删除指定文件夹
* 清空文件夹根目录

### 改
* 上传新文件覆盖

### 查
* 获取文件树结构
* 查询所有文件夹下文件信息
* 查询指定文件夹下文件信息
* 查询指定文件信息

### 下载
* 下载指定文件
* 下载多个指定文件并压缩