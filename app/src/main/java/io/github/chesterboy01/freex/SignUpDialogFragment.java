package io.github.chesterboy01.freex;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static io.github.chesterboy01.freex.FullscreenActivityBeforeLogin.communication;
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
                //if (!password_signup_2.equals(password_signup_1)) {
                    //Toast.makeText(getActivity(), "Password is not consistent, please try again", Toast.LENGTH_LONG).show();
                    //dismiss();
                    //SignUpDialogFragment dialog_signup = new SignUpDialogFragment();
                    //dialog_signup.show(getFragmentManager(), "signupDialog");
                //} else {
                new FreeXUser(username_signup, useremail, password_signup_1);
                communication();

                //}
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
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        /*builder.setView(view)
                // Add action buttons
                .setPositiveButton("Start Trading!",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //获取输入的信息
                                String useremail = tv21.getText().toString();
                                String username_signup = tv22.getText().toString();
                                String password_signup_1 = tv23.getText().toString();
                                String password_signup_2 = tv24.getText().toString();
                                if (!password_signup_2.equals(password_signup_1)) {
                                    Toast.makeText(getActivity(), "Password is not consistent, please try again", Toast.LENGTH_LONG).show();
                                    onDestroy();
                                    SignUpDialogFragment dialog_signup = new SignUpDialogFragment();
                                    dialog_signup.show(getFragmentManager(), "signupDialog");
                                } else {
                                    new FreeXUser(username_signup, useremail, password_signup_1);
                                    communication();

                                }
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onDestroy();
                    }
                });
        tv21.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isEmailAddressMatched(tv21.getText().toString())){

                }
                else{}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return builder.create();*/

    }
}
