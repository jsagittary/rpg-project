package com.dykj.rpg.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    /**
     * post 请求返回String
     * @param url
     * @param parms
     * @return
     */
    public static String sendPost(String url, Map<String, String> parms) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = getRequestConfig();
        HttpPost httpPost = new HttpPost(url);
        final List<NameValuePair> nvps = new ArrayList<>();
        if (parms != null && parms.size() > 0) {
            parms.forEach((key, value) -> {
                nvps.add(new BasicNameValuePair(key, value));
            });
        }
        if (nvps.size() > 0) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        httpPost.setConfig(requestConfig);
        String text = null;
        CloseableHttpResponse response=null;
        try {
             response = httpClient.execute(httpPost);
            if (response == null) {
                logger.error("response is null  url {} parms{}", url, Arrays.toString(nvps.toArray()));
                return null;
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity responseEntity = response.getEntity();
            text = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }



    /**
     * post 请求返回String
     * @param url
     * @param parms
     * @return
     */
    public static String sendHttpPost(String url, Map<String, String> parms) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = getRequestConfig();
        HttpPost httpPost = new HttpPost(url);
        final List<NameValuePair> nvps = new ArrayList<>();
        if (parms != null && parms.size() > 0) {
            parms.forEach((key, value) -> nvps.add(new BasicNameValuePair(key, value)));
        }
        if (nvps.size() > 0) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        httpPost.setConfig(requestConfig);
        String text = null;
        CloseableHttpResponse response=null;
        try {
            response = httpClient.execute(httpPost);
            if (response == null) {
                logger.error("response is null  url {} parms{}", url, Arrays.toString(nvps.toArray()));
                return null;
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity responseEntity = response.getEntity();
            text = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }


    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(10000).build();
    }


    private static RequestConfig getHttpRequestConfig() {
        return RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
    }





    private HttpPost creatHttpPost(String url){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = getRequestConfig();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        return  httpPost;

    }


    /**
     * @param url
     * @param body
     * @return
     * @throws Throwable
     */
    public String httpPost(String url,String body) throws Throwable{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = getRequestConfig();
        ContentType contentType = ContentType.create("application/json",
                Consts.UTF_8);
        StringEntity entity = new StringEntity(body, contentType);
        entity.setChunked(true);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            if (response == null) {
                return null;
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity responseEntity = response.getEntity();
            String text = EntityUtils.toString(responseEntity, "UTF-8");
            return text;
        } finally {
            response.close();
        }
    }

    /**
     * @param url
     * @return
     * @throws Throwable
     */
    public static  String httpGet(String url, Map<String ,String > parms)  throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = getRequestConfig();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            if (response == null) {
                logger.error("response is null  url {} ", url);
                return null;
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity responseEntity = response.getEntity();
            String text = EntityUtils.toString(responseEntity, "UTF-8");
            return text;
        } finally {
            response.close();
        }
    }

}
