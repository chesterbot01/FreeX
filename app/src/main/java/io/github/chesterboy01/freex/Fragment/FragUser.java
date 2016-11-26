package io.github.chesterboy01.freex.Fragment;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
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
import io.github.chesterboy01.freex.net.HttpUtil;


public class FragUser extends Fragment {
    Application appCtx;
    public UserPass mListener;
    User conUser;

    boolean fetch_flag;
    boolean fetch_result;

    TextView userName;
    TextView userEmail;
    TextView cadBal;
    TextView rmbBal;
    TextView usdBal;

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
        View v = inflater.inflate(R.layout.frag_user,container,false);
        conUser = mListener.getUser();

        new Fetch3Balance().execute(conUser);

        userName = (TextView) v.findViewById(R.id.user_frag_username_value);
        userEmail = (TextView) v.findViewById(R.id.user_frag_useremail_value);
        cadBal = (TextView) v.findViewById(R.id.user_frag_cadBalance_value);
        rmbBal = (TextView) v.findViewById(R.id.user_frag_rmbBalance_value);
        usdBal = (TextView) v.findViewById(R.id.user_frag_usdBalance_value);

        userName.setText(conUser.getUsername());
        userEmail.setText(conUser.getEmail());

        return v;
    }

    @Override
    public void onResume(){

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
        protected void onPreExecute() {

        }
        @SuppressWarnings("deprecation")
        protected Void doInBackground(User... params) {
            //params[0]就是我要传进来的user对象
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/fetchBalance.action";
                String result = null;
                HttpPost request = HttpUtil.getHttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    json.put("username",params[0].getUsername());
                    json.put("password",params[0].getPassword());
                    json.put("email",params[0].getEmail());
                    CookieApplication appCookie = (CookieApplication) appCtx;
                    List<Cookie> cookies = appCookie.getCookie();

                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);

                    //set http header cookie信息
                    request.setHeader("cookie", "JSESSIONID=" + cookies.get(0).getValue());
                    HttpResponse response = HttpUtil.getHttpResponse(request);
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

                if (result.equals("FetchFail"))
                    fetch_result = false;
                else{
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject objcad = (JSONObject) jsonArray.get(0);
                    JSONObject objrmb = (JSONObject) jsonArray.get(1);
                    JSONObject objusd = (JSONObject) jsonArray.get(2);
                    if (objcad.getInt("uid") > 0) {
                        fetch_result = true;
                    }
                    setBalance(objcad,cadBalance);
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
            while(!fetch_flag);
            cadBal.setText(cadBalance.getBamount());
            rmbBal.setText(rmbBalance.getBamount());
            usdBal.setText(usdBalance.getBamount());
        }

        void setBalance(JSONObject jo, Balance bal) throws JSONException {
            bal.setBamount(jo.getString("bamount"));
            bal.setBid(jo.getInt("bid"));
            bal.setBuid(jo.getInt("buid"));
            bal.setBcid(jo.getInt("bcid"));
        }
    }
}
