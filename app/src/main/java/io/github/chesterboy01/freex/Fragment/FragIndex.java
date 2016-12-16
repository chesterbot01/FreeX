package io.github.chesterboy01.freex.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.github.chesterboy01.freex.R;
import io.github.chesterboy01.freex.UserPass;
import io.github.chesterboy01.freex.entity.User;


public class FragIndex extends Fragment {

    private ListView lview;
    public UserPass mListener;
    User conUser;
    Activity act;

    Button buttonGetRate;
    EditText cadInput;
    EditText rmbInput;
    EditText usdInput;

    //just to avoid cold start
    double rmb_cad = 5.10;
    double rmb_usd = 6.88;
    double usd_cad = 1.35;

    static final String API_KEY = "e1c20e456196553c9d26e4f036e6b540";
    static final String API_URL = "http://www.apilayer.net/api/live?access_key=e1c20e456196553c9d26e4f036e6b540";

    public FragIndex() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        View v = inflater.inflate(R.layout.frag_index,container,false);
        conUser = mListener.getUser();

        buttonGetRate = (Button) v.findViewById(R.id.button_get_rate_api);
        cadInput = (EditText) v.findViewById(R.id.cad_calculator);
        rmbInput = (EditText) v.findViewById(R.id.rmb_calculator);
        usdInput = (EditText) v.findViewById(R.id.usd_calculator);

        buttonGetRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });

        cadInput.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s){
                //一个为焦点的时候，更新另外两个的值
                if (cadInput.isFocused() == true) {
                    String cadAmount = cadInput.getText().toString();
                    //判断是否为空
                    if (!cadAmount.matches("")) {
                        double cadAmountDouble = Double.parseDouble(cadAmount);
                        double rmbAmountDouble = cadAmountDouble * rmb_cad;
                        double usdAmountDouble = cadAmountDouble * usd_cad;
                        String str_rmb = Double.toString(rmbAmountDouble);
                        String str_usd = Double.toString(usdAmountDouble);
                        if(str_rmb.length()>10)
                            str_rmb = str_rmb.substring(0,10);
                        if(str_usd.length()>10)
                            str_usd = str_usd.substring(0,10);
                        rmbInput.setText(str_rmb);
                        usdInput.setText(str_usd);
                    }
                }
            }
        });

        rmbInput.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s){
                //一个为焦点的时候，更新另外两个的值
                if (rmbInput.isFocused() == true) {
                    String rmbAmount = rmbInput.getText().toString();
                    //判断是否为空
                    if (!rmbAmount.matches("")) {
                        double rmbAmountDouble = Double.parseDouble(rmbAmount);
                        double cadAmountDouble = rmbAmountDouble /rmb_cad;
                        double usdAmountDouble = rmbAmountDouble /rmb_usd;
                        String str_cad = Double.toString(cadAmountDouble);
                        String str_usd = Double.toString(usdAmountDouble);
                        if(str_cad.length()>10)
                            str_cad = str_cad.substring(0,10);
                        if(str_usd.length()>10)
                            str_usd = str_usd.substring(0,10);
                        cadInput.setText(str_cad);
                        usdInput.setText(str_usd);
                    }
                }
            }
        });

        usdInput.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s){
                //一个为焦点的时候，更新另外两个的值
                if (usdInput.isFocused() == true) {
                    String usdAmount = usdInput.getText().toString();
                    //判断是否为空
                    if (!usdAmount.matches("")) {
                        double usdAmountDouble = Double.parseDouble(usdAmount);
                        double rmbAmountDouble = usdAmountDouble / rmb_usd;
                        double cadAmountDouble = usdAmountDouble / usd_cad;
                        String str_rmb = Double.toString(rmbAmountDouble);
                        String str_cad = Double.toString(cadAmountDouble);
                        if(str_rmb.length()>10)
                            str_rmb = str_rmb.substring(0,10);
                        if(str_cad.length()>10)
                            str_cad = str_cad.substring(0,10);
                        rmbInput.setText(str_rmb);
                        cadInput.setText(str_cad);
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        //从MainActivity中获取
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

        void onFragmentInteraction(Uri uri);
    }

    public void refresh() {

        initViews();
    }

    private void initViews() {

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        String quotes;
        Double USD_CAD;
        Double USD_CNY;
        Double CAD_CNY;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(API_URL + "&apiKey=" + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
                Toast.makeText(act, response, Toast.LENGTH_LONG).show();
                return;
            }
            try{
                JSONObject obj = new JSONObject(response);
                quotes = obj.getString("quotes");
                JSONObject obj_inner = new JSONObject(quotes);
                USD_CAD = obj_inner.getDouble("USDCAD");
                USD_CNY = obj_inner.getDouble("USDCNY");
                CAD_CNY = new Double(USD_CNY.doubleValue()/USD_CAD.doubleValue());
                rmb_usd = USD_CNY.doubleValue();
                rmb_cad = CAD_CNY.doubleValue();
                usd_cad = USD_CAD.doubleValue();
            }catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(act, "USDCAD:"+ USD_CAD.toString()+". USDCNY:" + USD_CNY.toString()+". CADCNY:" +CAD_CNY.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
