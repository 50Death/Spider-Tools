# Sipder Tools

Sipder Tools 是我个人制作的的爬虫工具集，主要封装了一些常用的工具在里面，调用方便，使开发大型爬虫更方便

基于[jsoup](https://mvnrepository.com/artifact/org.jsoup/jsoup/1.11.3)[(GitHub)](https://github.com/jhy/jsoup)开发而成

## 工具列表
1. [线程安全URL储存器](https://github.com/50Death/Spider-Tools/blob/master/urltools/src/main/java/com/lyc/spider/tools/HttpURL.java)
2. [抓取完整页面，可自定http头部](https://github.com/50Death/Spider-Tools/blob/master/urltools/src/main/java/com/lyc/spider/tools/GetURLPage.java)
3. [html内容筛选，目前可筛 超链接、图片链接](https://github.com/50Death/Spider-Tools/blob/master/urltools/src/main/java/com/lyc/spider/tools/URLFetch.java)
4. [多线程下载图片](https://github.com/50Death/Spider-Tools/blob/master/urltools/src/main/java/com/lyc/spider/tools/imgFetch.java)
5. [Http请求头生成器](https://github.com/50Death/Spider-Tools/blob/master/urltools/src/main/java/com/lyc/spider/tools/DefaultHeaders.java)  (2019.Jan.24 Updated)
6. [网页摘要存储器](https://github.com/50Death/Spider-Tools/blob/master/urltools/src/main/java/com/lyc/spider/tools/WebPage.java)    (2019.Jan.25 Updated)
7. [IP代理池](https://github.com/50Death/Spider-Tools/blob/master/urltools/src/main/java/com/lyc/spider/tools/IPool.java)  (2019.Jan.26 Updated)
8. TODO 多线程下载文档
9. TODO PhantomJS 和 Selenium 支持

## 使用方式

### 开发环境
java version "1.8.0_191"

Java(TM) SE Runtime Environment (build 1.8.0_191-b12)

Java HotSpot(TM) 64-Bit Server VM (build 25.191-b12, mixed mode)

### IDE
IntelliJ IDEA

### 使用方式

#### HttpURL.java
用来存放待操作的URL，线程安全，不会发生脏读情况

#### GetURLPage.java
获取页面完整HTML代码，可以设定HTTP头部

#### URLFetch.java
对获取到的页面进行超链接筛选，可筛选出连接或图片连接

#### imgFetch.java
多线程下载图片

#### DefaultHeaders.java
获得Google Chrome 或者从其他数十种Http头部里的随机请求头

#### WebPage.java
网页摘要存储器 存储诸如搜索结果的: 标题，网址，摘要

#### IPModel.java
代理IP的存储器 存储一个代理IP的 IP地址，端口号，所在地，类型，链接速度，连接时间，存活时间，网站验证时间，IP状态
并提供了验证此IP是否能连通的测试方法
实现了Comparable compareTo接口，可以在IPool中直接排序

#### IPool.java
代理池储存器 储存一个代理池并按需获取代理IP 提供了TXT文件直接读取的方法

#### IPoolInitializer.java extends IPool.java
初始化IP代理池 爬取[西刺代理](https://www.xicidaili.com/nn/)的IP地址并验证 提供了读取本地TXT文件并验证的方法
单线程爬取代理网站[注1]
多线程验证IP是否可用
TODO：未来将会更新更多代理网站的爬取

## 使用到的Maven依赖
```xml
<!-- https://mvnrepository.com/artifact/org.jsoup/jsoup -->
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.11.3</version>
</dependency>
```
## 请捐赠打赏投食！！！
![图片加载失败](https://github.com/50Death/CipheredSocketChat/blob/master/Pictures/%E6%94%AF%E4%BB%98%E5%AE%9D%E7%BA%A2%E5%8C%85.jpg)

## Support Me on Patreon
(https://www.patreon.com/user?u=16747470)

## 更新记录
* 2019.Jan.24
* *  更新了DefaultHeaders.java 用来获取HTTP请求头部
* *  更新了GetURLPage.java 支持了代理功能
* 2019.Jan.25
* *  修复了BUG
* *  更新了Proxy代理设置
* *  更新了失败重连策略
* *  更新了网页摘要存储器-线程安全
* 2019.Jan.26
* *  修复了BUG
* *  更新了IP代理存储模块
* *  更新了IP代理池
* *  更新了IP代理池初始化模块
