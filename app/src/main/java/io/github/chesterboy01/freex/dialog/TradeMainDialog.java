package io.github.chesterboy01.freex.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import io.github.chesterboy01.freex.R;

/**
 * Created by Administrator on 11/10/2016.
 */

public class TradeMainDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.trade_main, null);

        Spinner s_in = (Spinner) view.findViewById(R.id.spinner_intype);
        Spinner s_out = (Spinner) view.findViewById(R.id.spinner_outtype);
        //number only
        final EditText inAmount = (EditText) view.findViewById(R.id.amount_in);
        final EditText tradeRate = (EditText) view.findViewById(R.id.trade_rate);

        return builder.setView(view).create();
    }
}
