package io.github.chesterboy01.freex.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.github.chesterboy01.freex.R;
import io.github.chesterboy01.freex.UserPass;
import io.github.chesterboy01.freex.dialog.TradeMainDialog;
import io.github.chesterboy01.freex.entity.User;


public class FragTrade extends Fragment implements View.OnClickListener {
    public UserPass mListener;
    User conUser;
    TradeMainDialog dialog;

    private String[] imageNameDatabase
            = new String[] {"CAD", "RMB", "USD"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_trade,container,false);
        conUser = mListener.getUser();
       // initializeImageList();
        Button button_deposit = (Button) v.findViewById(R.id.button_deposit);
        Button button_withdrawl = (Button) v.findViewById(R.id.button_withdrawl);
        Button button_buy = (Button) v.findViewById(R.id.button_buy);
        Button button_sell = (Button) v.findViewById(R.id.button_sell);

        dialog = new TradeMainDialog();
        dialog.setUser(conUser); //传入用户信息


        button_deposit.setOnClickListener(this);
        button_withdrawl.setOnClickListener(this);
        button_buy.setOnClickListener(this);
        button_sell.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.button_deposit:
                dialog.setType(TradeMainDialog.DEPOSIT);
                dialog.show(getFragmentManager(), "trade_deposit");
                break;
            case R.id.button_withdrawl:
                dialog.setType(TradeMainDialog.WITHDRAWL);
                dialog.show(getFragmentManager(), "trade_withdrawl");
                break;
            case R.id.button_buy:
                dialog.setType(TradeMainDialog.BUY);
                dialog.show(getFragmentManager(), "trade_buy");
                break;
            case R.id.button_sell:
                dialog.setType(TradeMainDialog.SELL);
                dialog.show(getFragmentManager(), "trade_sell");
                break;
            default:
                break;
        }
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
}
