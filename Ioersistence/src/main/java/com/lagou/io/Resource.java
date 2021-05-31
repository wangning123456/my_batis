package com.lagou.io;

import java.io.InputStream;

/**
 * @author wangning
 */
public class Resource {
    /**
     *
     * 获取配置文件流
     */
    public static InputStream getResourceAsStream(String path){
        //类加载获取
        InputStream resourceAsStream = Resource.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }
}
