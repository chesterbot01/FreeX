package io.github.chesterboy01.freex.Fragment;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.github.chesterboy01.freex.R;
import io.github.chesterboy01.freex.UserPass;
import io.github.chesterboy01.freex.entity.Balance;
import io.github.chesterboy01.freex.entity.User;
import io.github.chesterboy01.freex.net.CookieApplication;


public class FragUser extends Fragment {
    Application appCtx;
    Activity act;
    public UserPass mListener;
    User conUser;

    boolean fetch_flag;
    boolean fetch_result;

    TextView userName;
    TextView userEmail;
    TextView cadBal;
    TextView rmbBal;
    TextView usdBal;
    ImageView img;

    Balance cadBalance;
    Balance rmbBalance;
    Balance usdBalance;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appCtx = getActivity().getApplication();
        act = getActivity();
        View v = inflater.inflate(R.layout.frag_user,container,false);
        conUser = mListener.getUser();

        new Fetch3Balance().execute(conUser);


        userName = (TextView) v.findViewById(R.id.user_frag_username_value);
        userEmail = (TextView) v.findViewById(R.id.user_frag_useremail_value);
        cadBal = (TextView) v.findViewById(R.id.user_frag_cadBalance_value);
        rmbBal = (TextView) v.findViewById(R.id.user_frag_rmbBalance_value);
        usdBal = (TextView) v.findViewById(R.id.user_frag_usdBalance_value);
        img = (ImageView) v.findViewById(R.id.imageView);

        userName.setText(conUser.getUsername());
        userEmail.setText(conUser.getEmail());
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Fetch3Balance().execute(conUser);
                Toast.makeText(act, "Updating, please wait.", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof UserPass){
            //对传递进来的Activity进行接口转换
            mListener = ((UserPass) context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class Fetch3Balance extends AsyncTask<User, Void, Void> {
        HttpClient client;
        String result;
        protected void onPreExecute() {

        }
        @SuppressWarnings("deprecation")
        protected Void doInBackground(User... params) {
            //params[0]就是我要传进来的user对象
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/fetchBalance.action";
                result = null;
                HttpPost request = new HttpPost(URL);;
                try{
                    JSONObject json = new JSONObject();
                    json.put("username",params[0].getUsername());
                    json.put("password",params[0].getPassword());
                    json.put("email",params[0].getEmail());
                    CookieApplication appCookie = (CookieApplication) appCtx;
                    List<Cookie> cookies = appCookie.getCookie();
                    Log.v("cookies 在Fraguser里是什么", cookies.toString());
                    Log.v("cookies的get(0) 是什么", cookies.get(0).toString());
                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);

                    //set http header cookie信息
                    Cookie tmp = cookies.get(0);
                    Log.v("tmp的getVersion是什么", tmp.getValue());
                    request.setHeader("cookie", "JSESSIONID=" + tmp.getValue());
                    client = new DefaultHttpClient();
                    HttpResponse response = client.execute(request);;
                    int code = response.getStatusLine().getStatusCode();
                    if (code == 200){
                        result = EntityUtils.toString(response.getEntity());
                        Log.v("服务器给我返回了result吗", result);
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

                if (result.equals("FetchFail"))
                    fetch_result = false;
                else{
                    JSONArray jsonArray = new JSONArray(result);

                    JSONObject objcad = (JSONObject) jsonArray.get(0);
                    JSONObject objrmb = (JSONObject) jsonArray.get(1);
                    JSONObject objusd = (JSONObject) jsonArray.get(2);
                    Log.v("objcad有值吗？？？", objcad.toString());
                    if (objcad.getInt("buid") > 0) {
                        fetch_result = true;
                    }
                    cadBalance = new Balance();
                    rmbBalance = new Balance();
                    usdBalance = new Balance();
                    setBalance(objcad,cadBalance);
                    Log.v("objcad是什么！！！！", objcad.toString());
                    Log.v("cadBalance是什么！！！！", cadBalance.toString());
                    setBalance(objrmb,rmbBalance);
                    setBalance(objusd,usdBalance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            fetch_flag = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            //while(!fetch_flag);
            if(fetch_result == true) {
                // Log.v("到底访问了没！！？？", "sfdsdfsfsfsfsdfsdf");
                if(cadBalance.getBamount().lastIndexOf(".") == -1){
                    cadBal.setText(cadBalance.getBamount());
                }else if(cadBalance.getBamount().length()>9){
                    cadBal.setText(cadBalance.getBamount().substring(0,9));
                }
                else{
                    cadBal.setText(cadBalance.getBamount());
                }
                /*else{
                    int point = cadBalance.getBamount().lastIndexOf(".");
                    cadBal.setText(cadBalance.getBamount().substring(0,point+3));
                }*/
                if(rmbBalance.getBamount().lastIndexOf(".") == -1){
                    rmbBal.setText(rmbBalance.getBamount());
                }else if(rmbBalance.getBamount().length()>9){
                    rmbBal.setText(rmbBalance.getBamount().substring(0,9));
                }
                else{
                    rmbBal.setText(rmbBalance.getBamount());
                }

                if(usdBalance.getBamount().lastIndexOf(".") == -1){
                    usdBal.setText(usdBalance.getBamount());
                }else if(usdBalance.getBamount().length()>9){
                    usdBal.setText(usdBalance.getBamount().substring(0,9));
                }
                else{
                    usdBal.setText(usdBalance.getBamount());
                }

                Toast.makeText(act, "Balances are up to date now.", Toast.LENGTH_LONG).show();
            }
        }

        void setBalance(JSONObject jo, Balance bal) throws JSONException {
            bal.setBamount(jo.getString("bamount"));
            bal.setBid(jo.getInt("bid"));
            bal.setBuid(jo.getInt("buid"));
            bal.setBcid(jo.getInt("bcid"));
        }
    }

}
