# Sipder Tools

Sipder Tools 是我个人制作的的爬虫工具集，主要封装了一些常用的工具在里面，调用方便，使开发大型爬虫更方便

基于[jsoup](https://mvnrepository.com/artifact/org.jsoup/jsoup/1.11.3)开发而成

## 工具列表
1. 线程安全URL储存器
2. 抓取完整页面，可自定http头部
3. html内容筛选，目前可筛 超链接、图片链接
4. 多线程下载图片
5. TODO 多线程下载文档
6. TODO PhantomJS 和 Selenium 支持

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

## 使用到的Maven依赖
```xml
        <!-- https://mvnrepository.com/artifact/org.jsoup/jsoup -->
        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.11.3</version>
        </dependency>
```
## 
![图片加载失败](https://github.com/50Death/CipheredSocketChat/blob/master/Pictures/%E6%94%AF%E4%BB%98%E5%AE%9D%E7%BA%A2%E5%8C%85.jpg)