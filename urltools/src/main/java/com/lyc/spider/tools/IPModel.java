package com.lyc.spider.tools;

import com.sun.istack.internal.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;


/**
 * 用来存放一个ip地址的信息
 */
public class IPModel implements Comparable<IPModel> {

    private static final String VALIDATE_URL = "https://baidu.com";

    public String ip;          //IP地址
    public int port;           //IP端口号
    public String location;    //IP所在地
    public String type;        //IP类型
    public double speed;       //IP速度，越大越慢
    public double time;        //IP连接时间, 越大越慢
    public String life;        //IP存活时间
    public String validate;    //IP验证时间
    public boolean status;      //IP状态，方便使用时筛选

    /**
     * 实现比较器接口，用于比较
     *
     * @param o
     * @return
     */
    public int compareTo(@NotNull IPModel o) {
        double speed1 = this.speed;
        double speed2 = o.speed;
        return Double.compare(speed1, speed2);
    }

    /**
     * 空构造函数
     */
    public IPModel() {
    }

    /**
     * 含参构造函数
     *
     * @param ip
     * @param port
     * @param location
     * @param type
     * @param speed
     * @param time
     * @param life
     * @param validate
     */
    public IPModel(String ip, int port, String location, String type, double speed, double time, String life, String validate) {
        this.ip = ip;
        this.port = port;
        this.location = location;
        this.type = type;
        this.speed = speed;
        this.time = time;
        this.life = life;
        this.validate = validate;
    }

    public IPModel(String ip, int port, String location, String type, double speed, double time, String life, String validate, String status) {
        this.ip = ip;
        this.port = port;
        this.location = location;
        this.type = type;
        this.speed = speed;
        this.time = time;
        this.life = life;
        this.validate = validate;
        this.status = Boolean.parseBoolean(status);
    }

    /**
     * 添加一个IP对象
     *
     * @param ip
     * @param port
     * @param location
     * @param type
     * @param speed
     * @param time
     * @param life
     * @param validate
     */
    public synchronized void addIP(String ip, int port, String location, String type, double speed, double time, String life, String validate) {

        this.ip = ip;
        this.port = port;
        this.location = location;
        this.type = type;
        this.speed = speed;
        this.time = time;
        this.life = life;
        this.validate = validate;

    }

    @Override
    public String toString() {
        String str = ip + "," + port + "," + location + "," + type + "," + speed + "," + time + "," + life + "," + validate + "," + status;
        return str;
    }

    /**
     * 返回自己
     *
     * @return
     */
    public synchronized IPModel getIPModel() {
        return this;
    }

    /**
     * 验证该IP是否可用
     *
     * @param millis
     * @return
     */
    public synchronized boolean verify(int millis, int retry) {
        for (int i = -1; i < retry; i++) {
            try {
                Connection.Response res = Jsoup.connect(VALIDATE_URL).proxy(this.ip, this.port).headers(DefaultHeaders.getHeaders()).timeout(millis).execute();
                this.status = true;
                break;
            } catch (Exception e) {
                this.status = false;
            }
        }
        return this.status;
    }

}
