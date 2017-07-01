package com.inetgoes.fangdd.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;

/**
 * 封装网络请求的方法类
 * Created by czz on 2015/10/29.
 */
public class HttpUtil {

    /**
     * 判断当前网络是否连接可用
     *
     * @param context 上下文
     * @return boolean
     */
    public static boolean isNetworkAble(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 实现网络访问文件，将获取到的数据保存在指定目录中
     *
     * @param url      访问网络的url地址
     * @param destFile 保存的文件
     * @return
     */
    public static boolean loadFileFromURL(String url, File destFile) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet requestGet = new HttpGet(url);
        HttpResponse httpResponse = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(destFile));
            httpResponse = httpClient.execute(requestGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                bis = new BufferedInputStream(entity.getContent());
                int len = -1;
                byte[] buffer = new byte[8 * 1024];
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                    bos.flush();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 上传图片到服务器
     *
     * @param webPath        服务器处理地址
     * @param uploadFilePath 上传文件地址
     * @return
     */
    public static String uploadFile(String webPath, String uploadFilePath) {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(webPath);
        try {
            File osFile = new File(uploadFilePath);
            if (!osFile.exists()) {
                return null;
            }
            FileBody fileBody = new FileBody(osFile);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            // image 是服务端读取文件的 key
            entity.addPart("image", fileBody);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //获取服务器返回结果
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                Log.e("czhongzhi", "返回结果:" + result);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本地ip 例：127.0.0.1
     *
     * @return
     */
    public static String getLocalIp() {
        String ip = "127.0.0.1";
        return ip;
    }

    /**
     * 获取网络ip
     *
     * @return
     */
    public static String getNetworkIp() {
        String ipaddress = "127.0.0.1";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                            .getHostAddress())) {
                        return ipaddress = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipaddress;
    }

    /**
     * HttpClient POST请求
     *
     * @param url
     * @param json
     * @return
     */
    public static String postHttpReq(String url, String json) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(json, HTTP.UTF_8));
            Log.e("czhongzhi", EntityUtils.toString(post.getEntity()));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HttpClient POST请求
     *
     * @param url
     * @param map
     * @return
     */
    public static String postHttpReq(String url, Map<String, Object> map) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        ObjectMapper oMapper = JacksonMapper.getObjectMapper();
        try {
            post.setEntity(new StringEntity(oMapper.writeValueAsString(map), HTTP.UTF_8));
            Log.e("czhongzhi", EntityUtils.toString(post.getEntity()));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HttpClient POST请求
     *
     * @param url
     * @param obj
     * @return
     */
    public static String postHttpReq(String url, Object obj) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        ObjectMapper oMapper = JacksonMapper.getObjectMapper();
        try {
            post.setEntity(new StringEntity(oMapper.writeValueAsString(obj), HTTP.UTF_8));
            Log.e("czhongzhi", EntityUtils.toString(post.getEntity()));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HttpClient GET请求
     *
     * @param strUrl
     * @return
     */
    public static String getHttpReq(String strUrl) {
        HttpClient client = new DefaultHttpClient();
        Log.e("czhongzhi", strUrl);
        try {
            HttpGet get = new HttpGet(strUrl);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
