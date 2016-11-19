package io.github.chesterboy01.freex.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import io.github.chesterboy01.freex.R;
import io.github.chesterboy01.freex.UserPass;
import io.github.chesterboy01.freex.entity.User;


public class FragIndex extends Fragment {

    private ListView lview;
    public UserPass mListener;
    User conUser;

    EditText cadInput;
    EditText rmbInput;
    EditText usdInput;

    double rmb_cad = 5.12;
    double rmb_usd = 6.87;

    public FragIndex() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_index,container,false);
        conUser = mListener.getUser();

        cadInput = (EditText) v.findViewById(R.id.cad_calculator);
        rmbInput = (EditText) v.findViewById(R.id.rmb_calculator);
        usdInput = (EditText) v.findViewById(R.id.usd_calculator);

        cadInput.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s){
                String usdAmount = usdInput.getText().toString();
                if(!usdAmount.matches("")){
                    double usdAmountDouble = Double.parseDouble(usdAmount);
                    cadInput.setText(new String(new Double(usdAmountDouble/rmb_usd*rmb_cad).toString()));
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
                String usdAmount = usdInput.getText().toString();
                if(!usdAmount.matches("")){
                    double usdAmountDouble = Double.parseDouble(usdAmount);
                    rmbInput.setText(new String(new Double(usdAmountDouble*rmb_usd).toString()));
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
                String cadAmount = cadInput.getText().toString();
                if(!cadAmount.matches("")){
                    double cadAmountDouble = Double.parseDouble(cadAmount);
                    usdInput.setText(new String(new Double(cadAmountDouble/rmb_cad*rmb_usd).toString()));
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
}
