package io.github.chesterboy01.freex.net;


import org.json.JSONObject;

import io.github.chesterboy01.freex.entity.User;

/**
 * Created by Administrator on 11/7/2016.
 */

public class loginProtocol {
    public User user;
    public JSONObject obj;
    public loginProtocol (User user){
        this.user = user;
    }
    public boolean checkLogin(User user){

        //Log.v("user..xcxcx.",user.getUsername());
        try {
            //下面这行是把用户名和密码写到url里去，而不是Json
            //格式是http://192.168.95.1:8080/FreeX_Server/login.action?user.username=123&user.password=123   ?是第一个参数 &是第二个参数
            //String queryString = "?user.username=" + user.getUsername() + "&user.password=" + user.getPassword();

            String URL = "http://192.168.95.1:8080/FreeX_Server/signup.action";
            //就这一句话就把向服务器查询和返回的数据全部要回来了
            String result = HttpUtil.queryStringForPost(URL,user);
            //读取服务器返回的json数据
            //Log.v("resultsdsds",result);
            if (result.equals("LoginFail"))
                return false;
            else{
                // JSONArray array = new JSONArray("{"+result+"}");
                // JSONObject obj = array.getJSONObject(0);
                obj = new JSONObject(result);
                if (obj.getInt("uid") == 0) {
                    return false;
                }
                setUser(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setUser(User user) throws Exception{
        user.setUsername(obj.getString("username"));
        user.setEmail(obj.getString("email"));
        user.setPassword(obj.getString("password"));
        user.setUid(obj.getInt("uid"));
    }
}
