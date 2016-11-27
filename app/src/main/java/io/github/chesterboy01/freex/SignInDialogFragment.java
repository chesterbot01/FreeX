package io.github.chesterboy01.freex;

import android.app.Application;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.github.chesterboy01.freex.entity.User;
import io.github.chesterboy01.freex.net.CookieApplication;


/**
 * Created by Administrator on 10/10/2016.
 * 登陆的弹窗
 */

public class SignInDialogFragment extends DialogFragment {
    /* public static SignInDialogFragment newInstance() {
             return new SignInDialogFragment();
         }*/
    //用于第一次连接的时候发送Json给服务器
    Application appCtx;

    boolean isTest = false;

    boolean result1 = false;
    boolean flag1 = false;

    User conUser;

    //loginProtocol login;
    //用于服务返回的时候允许登陆的flag

    String out;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        appCtx = getActivity().getApplication();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.login_fragment, null);
        //获取用户名的textview
        final EditText tv1 = (EditText) view.findViewById(R.id.id_txt_username);
        final EditText tv2 = (EditText) view.findViewById(R.id.id_txt_password);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        Button login_signup = (Button) view.findViewById(R.id.sign_up_button);
        Button login_login = (Button) view.findViewById(R.id.sign_in_button);

        final TextView errorPrompt_login = (TextView) view.findViewById(R.id.error_prompt_login_text);
        errorPrompt_login.setVisibility(View.INVISIBLE);

        tv1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorPrompt_login.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorPrompt_login.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            //the action of launching the login page will be executed here.
            public void onClick(View v) {
                Toast.makeText(getActivity(), "注册", Toast.LENGTH_LONG).show();
                SignUpDialogFragment dialog_signup = new SignUpDialogFragment();
                dialog_signup.show(getFragmentManager(), "signupDialog");

            }
        });


        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            //the action of launching the login page will be executed here.
            public void onClick(View v) {
                //result1 = false;
                //flag1 = false;
                //获取输入的信息
                conUser = new User();
                conUser.setUsername(tv1.getText().toString());
                conUser.setPassword(tv2.getText().toString());
                new LoginAsync1().execute(conUser);


                //System.out.println(result);

                if(isTest) {
                    Intent intent_toMain = new Intent(getActivity(), MainActivity.class);
                    ((FullscreenActivityBeforeLogin) getActivity()).startActivity(intent_toMain);
                }
                else{
                    while (!flag1);
                    if (result1) {
                        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_LONG).show();

                       /* Toast.makeText(getActivity(),
                                "用户名是" + conUser.getUsername() + " 密码是" + conUser.getPassword(),
                                Toast.LENGTH_LONG).show();*/

                        Intent intent_toMain = new Intent(getActivity(), MainActivity.class);
                        //把要传的对象放到bundle里通过intent传进MainActivity中
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("user", conUser);
                        intent_toMain.putExtras(bundle);

                        (getActivity()).startActivity(intent_toMain);

                    } else {
                        Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return builder.setView(view).create();
    }


    public class LoginAsync1 extends AsyncTask<User, Void, Boolean> {
        JSONObject obj;
        String result;
        HttpClient client;
        protected void onPreExecute() {

        }
        protected Boolean doInBackground(User... params) {
            //params[0]就是我要传进来的user对象

            try {
                //下面这行是把用户名和密码写到url里去，而不是Json
                //格式是http://192.168.95.1:8080/FreeX_Server/login.action?user.username=123&user.password=123   ?是第一个参数 &是第二个参数
                //String queryString = "?user.username=" + user.getUsername() + "&user.password=" + user.getPassword();

                String URL = "http://192.168.95.1:8080/FreeX_Server/login.action";
                //就这一句话就把向服务器查询和返回的数据全部要回来了
                HttpPost request = new HttpPost(URL);;
                try{
                    JSONObject json = new JSONObject();
                    //Log.v("usersresr",user.getUsername());
                    json.put("username",params[0].getUsername());
                    json.put("password",params[0].getPassword());
                    Log.v("json",json.toString());

                    /*//从全局变量获取cookie
                    CookieApplication appCookie = (CookieApplication) appCtx;
                    List<Cookie> cookies = appCookie.getCookie();*/

                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);

                    //set http header cookie信息
                    //request.setHeader("cookie", "JSESSIONID=" + cookies.get(0).getValue());

                    //获得HttpResponse实例
                    client = new DefaultHttpClient();
                    HttpResponse response = client.execute(request);
                    //判断是否请求成功
                    int code = response.getStatusLine().getStatusCode();
                    List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
                    if(cookies.isEmpty()){
                        result = "network is not available";
                    }
                    else {
                            //保存cookie
                            //全局变量存储cookie
                            CookieApplication appCookie = (CookieApplication) appCtx;
                            appCookie.setCookie(cookies);
                            Log.v("session",appCookie.getCookie().get(0).getValue());
                        if (code == 200) {
                            //获得返回结果
                            result = EntityUtils.toString(response.getEntity());

                        }

                    }
                }
                catch(ClientProtocolException e){
                    e.printStackTrace();
                    result = "network is not available";
                }
                catch(IOException e){
                    e.printStackTrace();
                    result = "network is not available";
                }
                //读取服务器返回的json数据
                //Log.v("resultsdsds",result);
                if (result.equals("LoginFail"))
                    result1 = false;
                else{
                    // JSONArray array = new JSONArray("{"+result+"}");
                    // JSONObject obj = array.getJSONObject(0);
                    obj = new JSONObject(result);
                    if (obj.getInt("uid") == 0) {
                        result1 = false;
                    }
                    setUser(conUser);
                    result1 = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result1 = false;
            }
            flag1 = true;

            return result1;
        }
        protected void onPostExecute(Boolean... params) {
        }
        public void setUser(User user) throws Exception{
            user.setUsername(obj.getString("username"));
            user.setEmail(obj.getString("email"));
            user.setPassword(obj.getString("password"));
            user.setUid(obj.getInt("uid"));
        }
    }
}
