package io.github.chesterboy01.freex.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

import io.github.chesterboy01.freex.R;
import io.github.chesterboy01.freex.entity.Balance;
import io.github.chesterboy01.freex.entity.Transaction_history;
import io.github.chesterboy01.freex.entity.User;
import io.github.chesterboy01.freex.net.HttpUtil;




public class TradeMainDialog extends DialogFragment {

//含有userid的完整对象需要传进来
    Transaction_history singleTransaction;
    User conUser;
    Balance conBalence;

    public static final int DEPOSIT = 0;
    public static final int WITHDRAWL = 1;
    public static final int BUY = 2;
    public static final int SELL = 3;

    public int typeOfTransaction;

    TextView _1of_4;
    TextView calculatedAmount;
    TextView title_typeIn;
    TextView title_typeOut;
    TextView textViewoutamount1;
    TextView textViewoutamount2;
    Spinner s_in;
    Spinner s_out;
    EditText inAmount;
    EditText tradeRate;
    Button submitButton;
    Button QRcodeGenOrScan;

    String inputRate;
    double currentRate;
    double currentAmount;

    //用于asynctask返回结果的boolean变量
    boolean result_deposit;
    boolean result_withdrawl;
    boolean result_sell;
    boolean result_buy;

    boolean flag_deposit;
    boolean flag_withdrawl;
    boolean flag_sell;
    boolean flag_buy;

    public void setType(int type){
        typeOfTransaction = type;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.trade_main, null);

        singleTransaction = new Transaction_history();

        //表示是4种功能里的哪一种
        _1of_4 = (TextView) view.findViewById(R.id.trade_main_type_title);
        calculatedAmount = (TextView) view.findViewById(R.id.textViewoutamount2);
        title_typeIn = (TextView) view.findViewById(R.id.textViewtypein);
        title_typeOut = (TextView) view.findViewById(R.id.textViewtypeout);
        textViewoutamount1 =  (TextView) view.findViewById(R.id.textViewoutamount1);


        s_in = (Spinner) view.findViewById(R.id.spinner_intype);
        s_out = (Spinner) view.findViewById(R.id.spinner_outtype);
        //number only
        inAmount = (EditText) view.findViewById(R.id.amount_in);
        tradeRate = (EditText) view.findViewById(R.id.trade_rate);

        submitButton = (Button) view.findViewById(R.id.submit_trade_button);
        QRcodeGenOrScan = (Button) view.findViewById(R.id.gen_or_scan_button);

        //弹出数字输入的键盘
        inAmount.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        tradeRate.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        disableAll();
        enableAccordingToType(typeOfTransaction);

        //流入货币类型的spinner的控件逻辑
        s_in.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.currency_type);

                switch(languages[pos]){
                    case "CAD":
                        singleTransaction.setCidin(1);
                        break;
                    case "RMB":
                        singleTransaction.setCidin(2);
                        break;
                    case "USD":
                        singleTransaction.setCidin(3);
                        break;
                    default:
                        singleTransaction.setCidin(3);
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        //流出货币类型的spinner的控件逻辑
        s_out.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.currency_type);

                switch(languages[pos]){
                    case "CAD":
                        singleTransaction.setCidout(1);
                        break;
                    case "RMB":
                        singleTransaction.setCidout(2);
                        break;
                    case "USD":
                        singleTransaction.setCidout(3);
                        break;
                    default:
                        singleTransaction.setCidout(3);
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        //发起交易的按键动作
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保证每按一次都生成一个交易，
                //那个全局的singleTransaction只是一个tmp
                Transaction_history tranNew = new Transaction_history();

                switch(typeOfTransaction){
                    case DEPOSIT:
                        //先获取able控件的值
                        //给服务器发送数据的时候不要解析，String就是String, 别转成double
                        singleTransaction.setThamount(inAmount.getText().toString());

                        tranNew.setThuid(conUser.getUid());
                        tranNew.setCidin(singleTransaction.getCidin());
                        tranNew.setThamount(singleTransaction.getThamount());
                        //userid之类的别忘了设置
                        //然后向服务器发送数据
                        //先不考虑确认和握手的事情

                        new LoginAsyncTradeDeposit().execute(tranNew);

                        Toast.makeText(getActivity(), "Deposit is done",
                                Toast.LENGTH_LONG).show();
                        dismiss();
                        break;
                    case WITHDRAWL:
                        singleTransaction.setThamount(inAmount.getText().toString());

                        tranNew.setThuid(conUser.getUid());
                        tranNew.setCidout(singleTransaction.getCidout());
                        tranNew.setThamount(singleTransaction.getThamount());

                        new LoginAsyncTradeWithdrawl().execute(tranNew);
                        //然后向服务器发送数据
                        //并且传入tranNew;

                        Toast.makeText(getActivity(), "Withdrawl is done",
                                Toast.LENGTH_LONG).show();
                        dismiss();
                        break;
                    case BUY:
                        singleTransaction.setThamount(inAmount.getText().toString());

                        tranNew.setThuid(conUser.getUid());
                        tranNew.setCidin(singleTransaction.getCidin());
                        tranNew.setCidout(singleTransaction.getCidout());
                        tranNew.setThamount(singleTransaction.getThamount());

                        new LoginAsyncTradeBuy().execute(tranNew);
                        //然后向服务器发送数据


                        Toast.makeText(getActivity(), "Buy is done",
                                Toast.LENGTH_LONG).show();
                        dismiss();
                        break;
                    case SELL:
                        singleTransaction.setThamount(inAmount.getText().toString());
                        singleTransaction.setRate(tradeRate.getText().toString());

                        tranNew.setThuid(conUser.getUid());
                        tranNew.setCidin(singleTransaction.getCidin());
                        tranNew.setCidout(singleTransaction.getCidout());
                        tranNew.setThamount(singleTransaction.getThamount());
                        tranNew.setRate(singleTransaction.getRate());
                        //用户指定的汇率服务器接受吗？

                        //然后向服务器发送数据
                        new LoginAsyncTradeSell().execute(tranNew);

                        Toast.makeText(getActivity(), "Sell is done",
                                Toast.LENGTH_LONG).show();
                        dismiss();
                        break;
                    default:
                        Toast.makeText(getActivity(), "Error",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        //扫描二维码的按键动作
        QRcodeGenOrScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Transaction_history tranNew = new Transaction_history();

                switch(typeOfTransaction){
                    case BUY:
                        singleTransaction.setThamount(inAmount.getText().toString());

                        break;
                    case SELL:
                        singleTransaction.setThamount(inAmount.getText().toString());
                        singleTransaction.setRate(tradeRate.getText().toString());

                        break;
                    default:
                        Toast.makeText(getActivity(), "Error",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        //动态计算输出的总额 窗口一
        inAmount.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s){
                String amount = inAmount.getText().toString();
                String rate = tradeRate.getText().toString();
                //两个输入框不为空，而且换入换出的货币类型不同
                if(!amount.matches("")&&!rate.matches("")&&(singleTransaction.getCidin()!=singleTransaction.getCidout())){
                    double amount_ = Double.parseDouble(amount);
                    double rate_ = Double.parseDouble(rate);
                    Double calAmount = Double.valueOf(amount_*rate_);
                    String toshow = calAmount.toString();
                    calculatedAmount.setText(toshow);
                }
            }
        });

        //动态计算输出的总额 窗口二
        tradeRate.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s){
                String amount = inAmount.getText().toString();
                String rate = tradeRate.getText().toString();
                if(!amount.matches("")&&!rate.matches("")&&(singleTransaction.getCidin()!=singleTransaction.getCidout())){
                    double amount_ = Double.parseDouble(amount);
                    double rate_ = Double.parseDouble(rate);
                    Double calAmount = Double.valueOf(amount_*rate_);
                    String toshow = calAmount.toString();
                    calculatedAmount.setText(toshow);
                }
            }
        });

       /* inputRate = tradeRate.getText().toString();
        if (inputRate.matches("")) {
            currentRate = parseDouble(inputRate);
        }
        else
            currentRate = 6.82;*/
        return builder.setView(view).create();
    }

    //根据交易的类型enable控件
    protected void enableAccordingToType (int type){
        switch(type){
            case DEPOSIT:
                _1of_4.setText("Deposit");
                //对于用户的balance而言数目变多了，所以是in
                s_in.setEnabled(true);
                title_typeOut.setEnabled(true);
                title_typeOut.setText("Which type to deposit?");
                break;
            case WITHDRAWL:
                _1of_4.setText("Withdrawl");
                s_out.setEnabled(true);
                title_typeIn.setEnabled(true);
                title_typeOut.setText("Which type to withdrawl?");
                break;
            case SELL:
                _1of_4.setText("Sell");
                s_in.setEnabled(true);
                s_out.setEnabled(true);
                tradeRate.setEnabled(true);
                QRcodeGenOrScan.setEnabled(true);
                QRcodeGenOrScan.setText("Generate QR");
                title_typeIn.setEnabled(true);
                title_typeOut.setEnabled(true);
                textViewoutamount1.setEnabled(true);
                calculatedAmount.setEnabled(true);
                break;
            case BUY:
                _1of_4.setText("Buy");
                s_in.setEnabled(true);
                s_out.setEnabled(true);
                QRcodeGenOrScan.setEnabled(true);
                QRcodeGenOrScan.setText("Scan QR");
                title_typeIn.setEnabled(true);
                title_typeOut.setEnabled(true);
                textViewoutamount1.setEnabled(true);
                calculatedAmount.setEnabled(true);
                break;
            default:
                Toast.makeText(getActivity(), "Error",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    //初始化时diable所有根据不同类型可能用不到的控件
    private void disableAll(){
        s_in.setEnabled(false);
        s_out.setEnabled(false);
        //inAmount.setEnabled(false);
        tradeRate.setEnabled(false);
        //反正4个功能都要submit和输入数目的
        //submitButton.setEnabled(false);
        QRcodeGenOrScan.setEnabled(false);
        title_typeIn.setEnabled(false);
        title_typeOut.setEnabled(false);
        textViewoutamount1.setEnabled(false);
        calculatedAmount.setEnabled(false);
    }

    public void setUser(User user){
        conUser = user;
    }


    public class LoginAsyncTradeDeposit extends AsyncTask<Transaction_history, Void, Boolean> {
        //DepositFail

        protected void onPreExecute() {

        }
        protected Boolean doInBackground(Transaction_history... params) {
            String result;
            //params[0]就是我要传进来的Transaction_history对象
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/deposit.action";
                result = null;
                HttpPost request = HttpUtil.getHttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    json.put("thamount",params[0].getThamount());
                    json.put("cidin",params[0].getCidin());
                    json.put("thuid",params[0].getThuid());
                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);
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

                if (result.equals("DepositFail"))
                    result_deposit = false;
                else{
                    JSONObject obj = new JSONObject(result);
                    if (obj.getInt("buid") > 0) {
                        result_deposit = true;
                        conBalence.setBamount(obj.getString("bamount"));
                        conBalence.setBid(obj.getInt("bid"));
                        conBalence.setBcid(obj.getInt("bcid"));
                        conBalence.setBuid(obj.getInt("buid"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            flag_deposit = true;
            return result_deposit;
        }
        protected void onPostExecute(Boolean... params) {
        }
    }

    public class LoginAsyncTradeWithdrawl extends AsyncTask<Transaction_history, Void, Boolean> {
        //WithdrawlFail
        protected void onPreExecute() {

        }
        protected Boolean doInBackground(Transaction_history... params) {
            //params[0]就是我要传进来的Transaction_history对象
            String result;
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/withdrawl.action";
                result = null;
                HttpPost request = HttpUtil.getHttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    json.put("thamount",params[0].getThamount());
                    json.put("cidout",params[0].getCidout());
                    json.put("thuid",params[0].getThuid());
                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);
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

                if (result.equals("WithdrawlFail"))
                    result_withdrawl = false;
                else{
                    JSONObject obj = new JSONObject(result);
                    if (obj.getInt("buid") > 0) {
                        result_withdrawl = true;
                        conBalence.setBamount(obj.getString("bamount"));
                        conBalence.setBid(obj.getInt("bid"));
                        conBalence.setBcid(obj.getInt("bcid"));
                        conBalence.setBuid(obj.getInt("buid"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            flag_withdrawl = true;
            return result_withdrawl;
        }
        protected void onPostExecute(Boolean... params) {
        }
    }

    public class LoginAsyncTradeSell extends AsyncTask<Transaction_history, Void, Boolean> {
        protected void onPreExecute() {

        }
        protected Boolean doInBackground(Transaction_history... params) {
            //params[0]就是我要传进来的Transaction_history对象
            String result;
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/sell.action";
                result = null;
                HttpPost request = HttpUtil.getHttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    json.put("thamount",params[0].getThamount());
                    json.put("cidout",params[0].getCidout());
                    json.put("thuid",params[0].getThuid());
                    json.put("cidin",params[0].getCidin());
                    json.put("rate",params[0].getRate());
                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);
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

                if (result.equals("RegisterFail"))
                    result_sell = false;
                else{
                    JSONObject obj = new JSONObject(result);
                    if (obj.getInt("uid") > 0) {
                        result_sell = true;
                        conBalence.setBamount(obj.getString("bamount"));
                        conBalence.setBid(obj.getInt("bid"));
                        conBalence.setBcid(obj.getInt("bcid"));
                        conBalence.setBuid(obj.getInt("buid"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            flag_sell = true;
            return result_sell;
        }
        protected void onPostExecute(Boolean... params) {
        }
    }

    public class LoginAsyncTradeBuy extends AsyncTask<Transaction_history, Void, Boolean> {
        protected void onPreExecute() {

        }
        protected Boolean doInBackground(Transaction_history... params) {
            String result;
            //params[0]就是我要传进来的Transaction_history对象
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/buy.action";
                result = null;
                HttpPost request = HttpUtil.getHttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    json.put("thamount",params[0].getThamount());
                    json.put("cidout",params[0].getCidout());
                    json.put("thuid",params[0].getThuid());
                    json.put("cidin",params[0].getCidin());
                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);
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

                if (result.equals("RegisterFail"))
                    result_buy = false;
                else{
                    JSONObject obj = new JSONObject(result);
                    if (obj.getInt("uid") > 0) {
                        result_buy = true;
                        conBalence.setBamount(obj.getString("bamount"));
                        conBalence.setBid(obj.getInt("bid"));
                        conBalence.setBcid(obj.getInt("bcid"));
                        conBalence.setBuid(obj.getInt("buid"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            flag_buy = true;
            return result_buy;
        }
        protected void onPostExecute(Boolean... params) {
        }
    }


}
