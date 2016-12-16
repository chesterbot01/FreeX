package io.github.chesterboy01.freex.dialog;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.github.chesterboy01.freex.R;
import io.github.chesterboy01.freex.common.BarcodeEncoder;
import io.github.chesterboy01.freex.entity.Balance;
import io.github.chesterboy01.freex.entity.Transaction_history;
import io.github.chesterboy01.freex.entity.User;
import io.github.chesterboy01.freex.net.CookieApplication;


public class TradeMainDialog extends DialogFragment {
    boolean confirmation;
    Fragment fragment = this;
    Application appCtx;
    //We need entity here which contains userid.
    Transaction_history singleTransaction;
    User conUser;
    Balance conBalence;
    Activity act;
    String str_new;
    String str_left;
    String str_rate;
    AlertDialog.Builder builder1;
    ImageView image;
    boolean buyOrsell;

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
    Spinner s_in;
    Spinner s_out;
    EditText inAmount;
    EditText tradeRate;
    Button submitButton;
    Button QRcodeGenOrScan;


    //flag need by AsyncTask for returning result.
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
        act = getActivity();
        appCtx = getActivity().getApplication();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.trade_main, null);

        singleTransaction = new Transaction_history();

        //determined by which type of four.
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

        image = (ImageView) view.findViewById(R.id.iv_qr_image);
        //pop up the keyboard which only support digits input
        inAmount.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        tradeRate.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        disableAll();
        enableAccordingToType(typeOfTransaction);

        //The logics of the spinner which is in charge of the type of flew-in currency.
        s_in.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.currency_type);

                switch(languages[pos]){
                    case "CAD":
                        singleTransaction.setCidin(1);
                        break;
                    case "CNY":
                        singleTransaction.setCidin(2);
                        break;
                    case "USD":
                        singleTransaction.setCidin(3);
                        break;
                    default:
                        singleTransaction.setCidin(0);
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
                    case "CNY":
                        singleTransaction.setCidout(2);
                        break;
                    case "USD":
                        singleTransaction.setCidout(3);
                        break;
                    default:
                        singleTransaction.setCidout(0);
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        //Define something that happens after SUBMIT button is clicked.
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ensure one transaction is launched after this button is clicked
                //singleTransaction is just a global temporary variable.
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

                        Toast.makeText(act, "Deposit is done",
                                Toast.LENGTH_LONG).show();
                        dismiss();
                        break;
                    case WITHDRAWL:
                        singleTransaction.setThamount(inAmount.getText().toString());

                        tranNew.setThuid(conUser.getUid());
                        tranNew.setCidout(singleTransaction.getCidout());
                        tranNew.setThamount(singleTransaction.getThamount());

                        //Then turn to request to the server
                        //and send tranNew;
                        new LoginAsyncTradeWithdrawl().execute(tranNew);
                        dismiss();
                        break;
                    case BUY:
                        singleTransaction.setThamount(inAmount.getText().toString());

                        tranNew.setThuid(conUser.getUid());
                        tranNew.setCidin(singleTransaction.getCidin());
                        tranNew.setCidout(singleTransaction.getCidout());
                        tranNew.setThamount(singleTransaction.getThamount());
                        buyOrsell = true;
                        new LoginAsyncTradeBuy().execute(tranNew);

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
                        //will the server accept the rate designated by the user?
                        //Then turn to request to the server
                        buyOrsell = false;
                        new LoginAsyncTradeBuy().execute(tranNew);

                        dismiss();
                        break;
                    default:
                        Toast.makeText(act, "Error",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        //define the logics after click action of QRCODE button.
        QRcodeGenOrScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Transaction_history tranNew = new Transaction_history();

                switch(typeOfTransaction){
                    case BUY:
                        //We change the place of triggering scanning QRCode.
                        Toast.makeText(act, "Please switch to INDEX page, and trigger the title bar!",
                                Toast.LENGTH_LONG).show();
                        dismiss();
                        break;
                    case SELL:
                        singleTransaction.setThamount(inAmount.getText().toString());
                        singleTransaction.setRate(tradeRate.getText().toString());
                        singleTransaction.setThuid(conUser.getUid());
                        JSONObject json = new JSONObject();
                        try {
                            json.put("thamount",singleTransaction.getThamount());
                            json.put("cidout",singleTransaction.getCidout());
                            json.put("thuid",singleTransaction.getThuid());
                            json.put("cidin",singleTransaction.getCidin());
                            json.put("rate",singleTransaction.getRate());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String textQR = json.toString();
                        Log.v("json",textQR);
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try{
                            BitMatrix bitMatrix = multiFormatWriter.encode(textQR, BarcodeFormat.QR_CODE,400,400);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            image.setImageBitmap(bitmap);
                        }
                        catch (WriterException e){
                            e.printStackTrace();
                        }
                        break;
                    default:
                        Toast.makeText(act, "Error",
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

                if ((s.toString().contains("-")||s.toString().contains("+")||s.toString().contains(".")))
                    inAmount.setText("");

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

        //dynamically calculate the sum of output money, window II
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

        return builder.setView(view).create();
    }

    //after disableAll(), enable widgts respectively.
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
                Toast.makeText(act, "Error",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    //While initialization, disable widgets accoring to different transaction type
    private void disableAll(){
        s_in.setEnabled(false);
        s_out.setEnabled(false);
        //inAmount.setEnabled(false);
        tradeRate.setEnabled(false);
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
            //params[0] is instances of Transaction_history that passed in.
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/deposit.action";
                result = null;
                HttpPost request = new HttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    json.put("thamount",params[0].getThamount());
                    json.put("cidin",params[0].getCidin());
                    json.put("thuid",params[0].getThuid());
                    CookieApplication appCookie = (CookieApplication) appCtx;
                    List<Cookie> cookies = appCookie.getCookie();

                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);

                    //set http header cookie信息
                    request.setHeader("cookie", "JSESSIONID=" + cookies.get(0).getValue());
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
        int state = 0;
        protected void onPreExecute() {

        }
        protected Boolean doInBackground(Transaction_history... params) {
            //params[0] is instances of Transaction_history that passed in.
            String result;
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/withdrawal.action";
                result = null;
                HttpPost request =  new HttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    json.put("thamount",params[0].getThamount());
                    json.put("cidout",params[0].getCidout());
                    json.put("thuid",params[0].getThuid());
                    CookieApplication appCookie = (CookieApplication) appCtx;
                    List<Cookie> cookies = appCookie.getCookie();

                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);

                    //set http header cookie信息
                    request.setHeader("cookie", "JSESSIONID=" + cookies.get(0).getValue());
                    HttpResponse response = new DefaultHttpClient().execute(request);
                    int code = response.getStatusLine().getStatusCode();
                    if (code == 200){
                        result = EntityUtils.toString(response.getEntity());
                        Log.v("FUCK",result);
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

                if (result.equals("WithdrawalFail")) {
                    result_withdrawl = false;
                    state = 2;
                }
                else{
                    JSONObject obj = new JSONObject(result);
                    result_withdrawl = true;
                    conBalence.setBamount(obj.getString("bamount"));
                    conBalence.setBid(obj.getInt("bid"));
                    conBalence.setBcid(obj.getInt("bcid"));
                    conBalence.setBuid(obj.getInt("buid"));
                    state = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            flag_withdrawl = true;
            return result_withdrawl;
        }
        protected void onPostExecute(Boolean params) {
            if(state == 2 || state == 0)
                Toast.makeText(act, "Money Not Enough!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(act, "Withdrawl is done",
                        Toast.LENGTH_LONG).show();
        }
    }

    /*public class LoginAsyncTradeSell extends AsyncTask<Transaction_history, Void, Boolean> {
        protected void onPreExecute() {

        }
        protected Boolean doInBackground(Transaction_history... params) {
            //params[0]就是我要传进来的Transaction_history对象
            String result;
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/AddNewTransaction.action";
                result = null;
                HttpPost request = new HttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    json.put("thamount",params[0].getThamount());
                    json.put("cidout",params[0].getCidout());
                    json.put("thuid",params[0].getThuid());
                    json.put("cidin",params[0].getCidin());
                    json.put("rate",params[0].getRate());

                    CookieApplication appCookie = (CookieApplication) appCtx;
                    List<Cookie> cookies = appCookie.getCookie();

                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);

                    //set http header cookie信息
                    request.setHeader("cookie", "JSESSIONID=" + cookies.get(0).getValue());

                    HttpResponse response =  new DefaultHttpClient().execute(request);;
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

                if (result.equals("TransactionFail")) {
                    result_sell = false;
                    Toast.makeText(act, "Transaction is failed!!!", Toast.LENGTH_LONG).show();
                }
                else if (result.equals("MoneyNotEnough")) {
                    result_sell = false;
                    Toast.makeText(act, "Money is not enough!!!", Toast.LENGTH_LONG).show();
                }
                else{
                    JSONObject obj = new JSONObject(result);
                    if (!obj.getString("1").matches("")) {
                        result_sell = true;
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
    }*/

    public class LoginAsyncTradeBuy extends AsyncTask<Transaction_history, Void, Void> {
        boolean buyOrsell = true;
        protected void onPreExecute() {

        }
        protected Void doInBackground(Transaction_history... params) {
            String result;
            //params[0] is instances of Transaction_history that passed in.

            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/AddNewTransaction.action";
                result = null;
                HttpPost request = new HttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    json.put("thamount",params[0].getThamount());
                    json.put("cidout",params[0].getCidout());
                    json.put("thuid",params[0].getThuid());
                    json.put("cidin",params[0].getCidin());

                    CookieApplication appCookie = (CookieApplication) appCtx;
                    List<Cookie> cookies = appCookie.getCookie();

                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);

                    //set http header cookie information
                    request.setHeader("cookie", "JSESSIONID=" + cookies.get(0).getValue());

                    HttpResponse response = new DefaultHttpClient().execute(request);;
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

                if (result.equals("TransactionFail")){
                    result_buy = false;
                    Toast.makeText(act, "Transaction is failed!!!", Toast.LENGTH_LONG).show();
                }
                else if (result.equals("MoneyNotEnough")) {
                    result_sell = false;
                    Toast.makeText(act, "Money is not enough!!!", Toast.LENGTH_LONG).show();
                }
                else{
                    JSONObject obj = new JSONObject(result);
                    Log.v("afsadfsafsaf","fsafdsafafafa");
                    if (!obj.getString("1").matches("")) {
                        result_buy = true;
                        str_new = obj.getString("1");
                        str_left = obj.getString("2");
                        str_rate = obj.getString("3");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            flag_buy = true;
            return null;
        }
        @Override
        protected void onPostExecute(Void params) {
            builder1 = new AlertDialog.Builder(act);
            builder1.setTitle("Confirmation");
            builder1.setMessage("Are you sure to continue?\n In-currency type is " + singleTransaction.getCidin() + "; Unfinished in-amount is " + str_new + ";\n Out-currency type is " + singleTransaction.getCidout()+" Flew-out amount is " + str_left+ ";\n Exchange Rate is "+ str_rate + ".");
            builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //然后向服务器发送数据
                    confirmation = true;
                    new ConfirmationAsync().execute(confirmation);
                    if(buyOrsell == false)
                        {Toast.makeText(act, "Sell is done",
                                Toast.LENGTH_LONG).show();}
                    else
                        {Toast.makeText(act, "Buy is done",
                            Toast.LENGTH_LONG).show();}
                }
            });
            builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmation = false;
                    new ConfirmationAsync().execute(confirmation);
                    Toast.makeText(act, "Cancelled !",
                            Toast.LENGTH_LONG).show();
                }
            });
            builder1.show();
        }
    }


    public class ConfirmationAsync extends AsyncTask<Boolean, Void, Boolean> {
        int state;
        protected void onPreExecute() {

        }
        protected Boolean doInBackground(Boolean... params) {

            String result;
            try {
                state = 0;
                String URL = "http://192.168.95.1:8080/FreeX_Server/ExecuteNewTransaction.action";
                result = null;
                HttpPost request = new HttpPost(URL);
                try{
                    JSONObject json = new JSONObject();
                    int according_to_confirmation;
                    if (confirmation == true)
                        according_to_confirmation = 1;
                    else
                        according_to_confirmation = 0;
                    json.put("flag",according_to_confirmation);
                    CookieApplication appCookie = (CookieApplication) appCtx;
                    List<Cookie> cookies = appCookie.getCookie();

                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);

                    //set http header cookie信息
                    request.setHeader("cookie", "JSESSIONID=" + cookies.get(0).getValue());

                    HttpResponse response =  new DefaultHttpClient().execute(request);;
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

                if (result.equals("Fail")) {
                    result_sell = false;
                    state = 2;
                }
                else if (result.equals("Success")){
                    result_sell = true;
                    state = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            flag_sell = true;
            return result_sell;
        }
        protected void onPostExecute(Boolean params) {
            if (state != 2) {
                Toast.makeText(act, "Transaction is done!", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(act, "Transaction is cancelled!!!", Toast.LENGTH_LONG).show();
        }
    }


}
