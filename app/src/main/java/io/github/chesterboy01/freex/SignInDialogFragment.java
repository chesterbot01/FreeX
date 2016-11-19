package io.github.chesterboy01.freex;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
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

    boolean isTest = false;

    boolean result1 = false;
    boolean flag1 = false;

    User conUser;

    loginProtocol login;
    //用于服务返回的时候允许登陆的flag

    String out;
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

                        Toast.makeText(getActivity(),
                                "用户名是" + conUser.getUsername() + " 密码是" + conUser.getPassword(),
                                Toast.LENGTH_LONG).show();

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
        protected void onPreExecute() {

        }
        protected Boolean doInBackground(User... params) {
            //params[0]就是我要传进来的user对象
            login = new loginProtocol(params[0]);
            result1 = login.checkLogin(params[0]);
            flag1 = true;

            return result1;
        }
        protected void onPostExecute(Boolean... params) {
        }
    }
}
