package io.github.chesterboy01.freex;

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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import io.github.chesterboy01.freex.entity.User;
import io.github.chesterboy01.freex.net.loginProtocol;

/**
 * Created by Administrator on 10/10/2016.
 * 登陆的弹窗
 */

public class SignInDialogFragment extends DialogFragment {
    /* public static SignInDialogFragment newInstance() {
             return new SignInDialogFragment();
         }*/
    //用于第一次连接的时候发送Json给服务器

    boolean isTest = true;

    boolean result1 = false;
    boolean flag1 = false;
    //用于服务返回的时候允许登陆的flag
    boolean result2 = false;
    boolean flag2 = false;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
                //获取输入的信息
                String username = tv1.getText().toString();
                String password = tv2.getText().toString();
                User current_user = new User();
                current_user.setUsername(username);
                current_user.setPassword(password);
                new LoginAsync1().execute(current_user);
                User conUser = new User();
                LoginAsync2 l2 = new LoginAsync2();
                l2.set(conUser);
                l2.execute();
                //System.out.println(result);


                if(isTest) {
                    Intent intent_toMain = new Intent(getActivity(), MainActivity.class);
                    ((FullscreenActivityBeforeLogin) getActivity()).startActivity(intent_toMain);
                }
                else{

                    while (!flag1);
                    while (!flag2);

                    if (result1 && result2) {
                        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_LONG).show();

                        Toast.makeText(getActivity(),
                                "用户名是" + conUser.getUsername() + " 密码是" + password,
                                Toast.LENGTH_LONG).show();

                        Intent intent_toMain = new Intent(getActivity(), MainActivity.class);
                        ((FullscreenActivityBeforeLogin) getActivity()).startActivity(intent_toMain);

                    } else {
                        Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_LONG).show();

                    }

                }
                //Intent intent_toMain = new Intent(getActivity(),MainActivity.class);
                //((FullscreenActivityBeforeLogin)getActivity()).startActivity(intent_toMain);
              /*  if (IsValidLogin(username,password)) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    dismiss();
                }
                else{
                    errorPrompt_login.setText("Wrong username or password.");
                    errorPrompt_login.setVisibility(View.VISIBLE);
                }*/


            }
        });
        return builder.setView(view).create();

            //在程序里面添加按键不太容易操控
            /*builder.setView(view)
                    // Add action buttons
                    .setPositiveButton("Sign In",

                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //获取输入的信息
                                    String username = tv1.getText().toString();
                                    String password = tv2.getText().toString();

                                    Toast.makeText(getActivity(),
                                            "用户名是" + username + " 密码是" + password,
                                            Toast.LENGTH_LONG).show();
                                }
                            })
                    .setNegativeButton("Sign Up & Start Trading", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getActivity(), "注册", Toast.LENGTH_LONG).show();
                            SignUpDialogFragment dialog_signup = new SignUpDialogFragment();
                            dialog_signup.show(getFragmentManager(), "signupDialog");
                        }
                    });
            return builder.create();*/
    }


    public class LoginAsync1 extends AsyncTask<User, Void, Boolean> {

        protected void onPreExecute() {

        }

        protected Boolean doInBackground(User... params) {

            //Log.v("user...",params[0].getUsername());

            loginProtocol login = new loginProtocol(params[0]);
            result1 = login.checkLogin(params[0]);
            flag1 = true;




            return result1;
        }


        protected void onPostExecute(Boolean... params) {

        }
    }


    public class LoginAsync2 extends AsyncTask<Void, Void, Void> implements MessageResponseFromAsyncToUIThread {

        MessageResponseFromAsyncToUIThread message;
        User user_fromJson;
        JSONObject jsonObject;

        protected void onPreExecute() {

        }

        protected Void doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost request;
            try {
                //request = new HttpPost(new URI("http://192.168.95.1:8080/FreeX_Server/login.action"));
                //request = new HttpPost(new URI("http://127.0.0.1:8080/FreeX_Server/text.action"));
                request = new HttpPost(new URI("http://10.0.2.2:8080/FreeX_Server/text.action"));
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    org.apache.http.HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String out = org.apache.http.util.EntityUtils.toString(entity, "UTF-8");
                        //登陆失败
                        if (out.equals("LoginFail")) {
                            result2 = false;
                            return null;
                        }
                        //登陆成功，开始解析JSON生成User
                        else {
                            JSONArray jsonArray = new JSONArray(out);
                            jsonObject = (JSONObject) jsonArray.get(0);
                            onReceivedSuccess(user_fromJson, jsonObject);
                            result2 = true;
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("从服务器获取数据出错了", e.toString());
            }
            finally{flag2 = true;}
            return null;
        }

        //抛出异常交给上一层去处理
        protected void onPostExecute() throws JSONException {
            message.onReceivedSuccess(user_fromJson,jsonObject);
        }

        @Override
        public void onReceivedSuccess(User user_process, JSONObject jsonobject) throws JSONException {
            user_process.setUsername(jsonobject.getString("username"));
            user_process.setUid(jsonobject.getInt("uid"));
            user_process.setEmail(jsonobject.getString("email"));
            user_process.setPassword(jsonobject.getString("password"));
        }

        public void set(User user) {
            user_fromJson = user;
        }


    }



}
