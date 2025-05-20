package com.book.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;

import java.io.IOException;

/**
 * 访问http的访问逻辑类
 */
public final class HttpClientUtils {

    public static JSONObject execute(String url, HttpMethod httpMethod){
        HttpRequestBase http = null;
        HttpClient client = HttpClients.createDefault();
        // 根据 httpMethod 进行HttpRequest的创建
        if(httpMethod == HttpMethod.GET){
            http = new HttpGet(url);
        }else {
            http = new HttpPost(url);
        }
        HttpEntity entity = null;
        try {
            entity = client.execute(http).getEntity();
            return JSONObject.parseObject(EntityUtils.toString(entity));
        }catch (IOException ex){
            throw new RuntimeException("Request failed. url = "+ url);
        }finally {
            http.releaseConnection();
        }
    }
}
