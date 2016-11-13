package io.github.chesterboy01.freex.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.github.chesterboy01.freex.R;
import io.github.chesterboy01.freex.entity.Transaction_history;

import static io.github.chesterboy01.freex.R.id.textViewoutamount;
import static io.github.chesterboy01.freex.R.id.trade_main_type_title;

/**
 * Created by Administrator on 11/10/2016.
 */

public class TradeMainDialog extends DialogFragment {

//含有userid的完整对象需要传进来
    Transaction_history singleTransaction;

    public static final int DEPOSIT = 0;
    public static final int WITHDRAWL = 1;
    public static final int BUY = 2;
    public static final int SELL = 3;

    public int typeOfTransaction;

    TextView _1of_4;
    TextView calculatedAmount;
    Spinner s_in;
    Spinner s_out;
    EditText inAmount;
    EditText tradeRate;
    Button submitButton;
    Button QRcodeGenOrScan;


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
        calculatedAmount = (TextView) view.findViewById(R.id.textViewoutamount);

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



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(typeOfTransaction){
                    case DEPOSIT:

                        Toast.makeText(getActivity(), "Deposit is done",
                                Toast.LENGTH_LONG).show();
                        break;
                    case WITHDRAWL:

                        Toast.makeText(getActivity(), "Withdrawl is done",
                                Toast.LENGTH_LONG).show();
                        break;
                    case BUY:

                        Toast.makeText(getActivity(), "Buy is done",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SELL:

                        Toast.makeText(getActivity(), "Sell is done",
                                Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getActivity(), "Error",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        QRcodeGenOrScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch(typeOfTransaction){
                    case BUY:
                        //扫码
                        break;
                    case SELL:
                        //生成二维码
                        break;
                    default:
                        Toast.makeText(getActivity(), "Error",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        return builder.setView(view).create();
    }

    protected void enableAccordingToType (int type){
        switch(type){
            case DEPOSIT:
                _1of_4.setText("Deposit");
                //对于用户的balance而言数目变多了，所以是in
                s_in.setEnabled(true);
                break;
            case WITHDRAWL:
                _1of_4.setText("Withdrawl");
                s_out.setEnabled(true);
                break;
            case SELL:
                _1of_4.setText("Sell");
                s_in.setEnabled(true);
                s_out.setEnabled(true);
                tradeRate.setEnabled(true);
                QRcodeGenOrScan.setEnabled(true);
                QRcodeGenOrScan.setText("Generate QR");
                break;
            case BUY:
                _1of_4.setText("Buy");
                s_in.setEnabled(true);
                s_out.setEnabled(true);
                QRcodeGenOrScan.setEnabled(true);
                QRcodeGenOrScan.setText("Scan QR");
                break;
            default:
                Toast.makeText(getActivity(), "Error",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
    private void disableAll(){
        s_in.setEnabled(false);
        s_out.setEnabled(false);
        //inAmount.setEnabled(false);
        tradeRate.setEnabled(false);
        //反正4个功能都要submit和输入数目的
        //submitButton.setEnabled(false);
        QRcodeGenOrScan.setEnabled(false);
    }
}
