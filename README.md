微博信息可信度分析
====================

开发环境及基本架构
-----------------------
### 开发环境
		javase 1.7
		eclipse jee 4.5.2 RC3
		mysql Ver 14.14 Distrib 5.7.11
		Apache Tomcat 7
### 程序基本架构
		前台数据获取接口：jersey restful
		数据库持久化框架：mybatis3

工程下载及导入eclipse
-----------------------
1.下载工程文件\[[下载地址](https://github.com/tanganjie/weibo/archive/master.zip)\]，并解压  
2.打开eclipse导入，eclipse->Import->Existing Projects into Workspace->选择加压目录
3.等待maven构建编译结束

调试运行
-----------------------
1.安装mysql（注：编码设置为utf8）  
2.测试数据导入，通过shell或者cmd进入mysql（命令：mysql -u 用户名 -p），建立一个数据库名dbname，利用如下命令导入数据 <br> 
> use [dbname];
>
source  [sqlpath];

3.更改src/main/resources下config.properties数据库配置信息   
4.配置eclipse中的tomcat，Servers标签下右键，点击new server，选择对应版本的tomcat即可。   
5.右键配置好的tomcat->Add and Remove，添加本项目   
6.run server   
7.浏览器打开 http://localhost:8080/weibo


