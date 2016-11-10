package io.github.chesterboy01.freex.net;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.chesterboy01.freex.entity.User;

/**
 * Created by Administrator on 11/7/2016.
 */

public class loginProtocol {
    protected User user;
    public loginProtocol (User user){
        this.user = user;
    }
    public boolean checkLogin(User user){

        //Log.v("user..xcxcx.",user.getUsername());
        try {
            //下面这行是把用户名和密码写到url里去，而不是Json
            //格式是http://192.168.95.1:8080/FreeX_Server/login.action?user.username=123&user.password=123   ?是第一个参数 &是第二个参数
            //String queryString = "?user.username=" + user.getUsername() + "&user.password=" + user.getPassword();

            String URL = HttpUtil.BASE_URL ;

            String result = HttpUtil.queryStringForPost(URL,user);
            //读取服务器返回的json数据
            Log.v("result",result);

            JSONArray array = new JSONArray("{"+result+"}");
            JSONObject obj = array.getJSONObject(0);
            Log.v("obj",obj.toString());
            String flag  = obj.getString("loginStatus");

            if (flag.equals("login"))
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
