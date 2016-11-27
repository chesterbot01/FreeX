package io.github.chesterboy01.freex;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

import io.github.chesterboy01.freex.entity.User;

import static io.github.chesterboy01.freex.R.layout.signup_fragment;
import static io.github.chesterboy01.freex.RegularExpressionEmailAddress.isEmailAddressMatched;

/**
 * Created by Administrator on 10/10/2016.
 * 注册的弹窗
 */

public class SignUpDialogFragment extends DialogFragment {
    /* public static SignInDialogFragment newInstance() {
             return new SignInDialogFragment();
         }*/
    public static boolean isEmailValid = false;
    public static boolean isUserNameValid = false;
    public static boolean isPasswordValid = false;

    boolean result_signup = false;
    boolean flag_signup = false;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(signup_fragment, null);
        //获取注册的4个textview的信息
        final EditText tv21 = (EditText) view.findViewById(R.id.id_txt_useremail);
        final EditText tv22 = (EditText) view.findViewById(R.id.id_txt_username_1);
        final EditText tv23 = (EditText) view.findViewById(R.id.id_txt_password_1);
        final EditText tv24 = (EditText) view.findViewById(R.id.id_txt_password_2);




        final Button signup_login_signup = (Button) view.findViewById(R.id.signup_sign_up_button);
        //锁住注册按钮!
        signup_login_signup.setBackgroundColor(Color.parseColor("#005500"));
        signup_login_signup.setEnabled(false);
        Button signup_cancel = (Button) view.findViewById(R.id.signup_cancel_button);
        //专门用于提示注册信息填写错误的
        final TextView errorPrompt = (TextView) view.findViewById(R.id.error_prompt_text);
        tv21.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(isEmailAddressMatched(tv21.getText().toString())){
                    isEmailValid = true;
                    errorPrompt.setVisibility(View.INVISIBLE);
                    //signup_login_signup.setEnabled(true);
                    //Button.setBackground早就过时了！
                    //signup_login_signup.setBackgroundResource(R.drawable.signin_signup_button);
                }
                else{
                    //给予一定的提示
                    errorPrompt.setText("Invalid Email Address!");
                    errorPrompt.setVisibility(View.VISIBLE);
                }
            }
        });

        tv22.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!tv22.getText().toString().isEmpty()){
                    errorPrompt.setVisibility(View.INVISIBLE);
                    isUserNameValid = true;
                }
                else {
                    isUserNameValid = false;
                    errorPrompt.setText("Username cannot be empty!");
                    errorPrompt.setVisibility(View.VISIBLE);
                }
            }
        });

        tv24.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(tv24.getText().toString().equals(tv23.getText().toString())) {
                    isPasswordValid = true;
                    errorPrompt.setVisibility(View.INVISIBLE);
                }

                else {
                    isPasswordValid = false;
                    errorPrompt.setText("Confirmed password is not consistent!");
                    errorPrompt.setVisibility(View.VISIBLE);
                }

                if(isEmailValid&&isPasswordValid&&isUserNameValid){
                    signup_login_signup.setEnabled(true);
                    signup_login_signup.setBackgroundResource(R.drawable.signin_signup_button);
                }
                else{
                    signup_login_signup.setEnabled(false);
                    signup_login_signup.setBackgroundColor(Color.parseColor("#005500"));
                }


                    //signup_login_signup.setEnabled(true);
                    //Button.setBackground早就过时了！
                    //signup_login_signup.setBackgroundResource(R.drawable.signin_signup_button);

            }
        });


        signup_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            //the action of launching the login page will be executed here.
            public void onClick(View v) {
                String useremail = tv21.getText().toString();
                String username_signup = tv22.getText().toString();
                String password_signup_1 = tv23.getText().toString();
                String password_signup_2 = tv24.getText().toString();

                User current_user = new User();
                current_user.setEmail(useremail);
                current_user.setPassword(password_signup_2);
                current_user.setUsername(username_signup);
                new LoginAsyncSignUp().execute(current_user);

                while(!flag_signup);

                if (result_signup == true)
                    Toast.makeText(getActivity(), "注册成功！", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "失败", Toast.LENGTH_LONG).show();

                dismiss();
            }
        });

        signup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            //the action of launching the login page will be executed here.
            public void onClick(View v) {
                //要remove dialog fragment from activity的话
                // dismiss()函数才是有效的，onDestroy()和
                // getFragmentManager.remove(...).commit()根本不管用

                dismiss();
            }
        });


        return builder.setView(view).create();
    }
    public class LoginAsyncSignUp extends AsyncTask<User, Void, Boolean> {
        protected void onPreExecute() {

        }
        @SuppressWarnings("deprecation")
        protected Boolean doInBackground(User... params) {
            //params[0]就是我要传进来的user对象
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/register.action";
                String result = null;
                HttpPost request = new HttpPost(URL);;
                try{
                    JSONObject json = new JSONObject();
                    json.put("username",params[0].getUsername());
                    json.put("password",params[0].getPassword());
                    json.put("email",params[0].getEmail());
                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);
                    HttpResponse response = new DefaultHttpClient().execute(request);
                    int code = response.getStatusLine().getStatusCode();
                    if (code == 200){
                        result = EntityUtils.toString(response.getEntity());
                    }
                }
                catch(ClientProtocolException e){
                    e.printStackTrace();
                    result = "ClientProtocolException:network is not available";
                }
                catch(IOException e) {
                    e.printStackTrace();
                    result = "IOException:network is not available";
                }

                if (result.equals("RegisterFail"))
                    result_signup = false;
                else{
                    JSONObject obj = new JSONObject(result);
                    if (obj.getInt("uid") > 0) {
                        result_signup = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            flag_signup = true;
            return result_signup;
        }
        protected void onPostExecute(Boolean... params) {
        }
    }
}
