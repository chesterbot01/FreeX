/*
package io.github.chesterboy01.freex.net;

import android.app.Application;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.github.chesterboy01.freex.entity.User;

*/
/**
 * Created by Administrator on 11/7/2016.
 *//*


public class HttpUtil {
    //声明Base URL常量
    //public static final String BASE_URL = "http://192.168.95.1:8080/FreeX_Server/login.action";
    //public static final String BASE_URL = "http://127.0.0.1:8080/FreeX_Server/text.action";
    //public static final String BASE_URL = "http://10.0.2.2:8080/FreeX_Server/text.action";
    //public static final String BASE_URL = R.strings.base_url+"login.action";
    public static final String BASE_URL = "http://192.168.95.1:8080/FreeX_Server/login.action";

    HttpClient client;

    //通过URL获得HttpGet对象
    public static HttpGet getHttpGet(String url){
        //实例化HttpGet
        HttpGet request = new HttpGet(url);
        return request;
    }

    //通过URL获得HttpPost对象
    public static HttpPost getHttpPost(String url){
        HttpPost request = new HttpPost(url);
        return request;
    }

    //通过HttpGet获得HttpResponse对象
    public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
        //实例化HttpResponse
        HttpResponse response = new DefaultHttpClient().execute(request);
        return response;
    }

    //通过HttpPost获得HttpResponse对象
    public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
        //实例化HttpResponse
        HttpResponse response = new DefaultHttpClient().execute(request);
        return response;
    }

    //通过HttpPost发送get请求，返回请求结果
    public static String queryStringForGet(String url){
        client = new DefaultHttpClient();
        //获得HttpGet实例
        HttpGet request = HttpUtil.getHttpGet(url);
        String result = null;
        try{
            //获得HttpResponse实例
            HttpResponse response = HttpUtil.getHttpResponse(request);
            List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200){
                //获得返回结果

                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        }
        catch(ClientProtocolException e){
            e.printStackTrace();
            result = "网络异常";
            return result;
        }
        catch(IOException e){
            e.printStackTrace();
            result = "网络异常";
            return result;
        }
        return null;
    }

    //通过URL发送post请求，返回请求结果
    public static String queryStringForPost(String url, User user,Application appCtx) throws JSONException {

        HttpPost request = HttpUtil.getHttpPost(url);

        String result = null;
        try{
            JSONObject json = new JSONObject();
            //Log.v("usersresr",user.getUsername());
            json.put("username",user.getUsername());
            json.put("password",user.getPassword());
            Log.v("json",json.toString());

            //从全局变量获取cookie
            CookieApplication appCookie = (CookieApplication) appCtx;
            List<Cookie> cookies = appCookie.getCookie();

            StringEntity se = new StringEntity(json.toString(),"utf-8");
            request.setEntity(se);

            //set http header cookie信息
            //request.setHeader("cookie", "JSESSIONID=" + cookies.get(0).getValue());

            //获得HttpResponse实例
            HttpResponse response = HttpUtil.getHttpResponse(request);
            //判断是否请求成功
            int code = response.getStatusLine().getStatusCode();
            if (code == 200){
                //获得返回结果
                result = EntityUtils.toString(response.getEntity());
                Log.v("codessss",result);
                return result;
            }
        }
        catch(ClientProtocolException e){
            e.printStackTrace();
            result = "network is not available";
            return result;
        }
        catch(IOException e){
            e.printStackTrace();
            result = "network is not available";
            return result;
        }
        return null;
    }
}
*/
