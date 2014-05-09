IShare.com电子商务网站使用方式：

配置需求：Tomcat服务器 + MySQL数据库

运行步骤：
1. 在Tomcat服务器部署ExtJS官方项目，项目名称为ext.4.2；
2. 打开ishare.war包，修改WEB-INF目录下的hib-config.xml文件中的MySQL数据库配置信息；
3. 将修改后的ishare.war包复制到Tomcat服务器webapps目录下；
4. 启动Tomcat服务器，访问127.0.0.1/system/init进行系统初始化；
5. 系统初始化完成后访问127.0.0.1/control/login，使用默认账户admin.ishare.com(ishare.com.admin)进入系统后台。