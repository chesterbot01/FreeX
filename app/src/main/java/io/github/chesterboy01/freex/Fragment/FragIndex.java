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
                //一个为焦点的时候，更新另外两个的值
                if (cadInput.isFocused() == true) {
                    String cadAmount = cadInput.getText().toString();
                    //判断是否为空
                    if (!cadAmount.matches("")) {
                        double cadAmountDouble = Double.parseDouble(cadAmount);
                        double rmbAmountDouble = cadAmountDouble * 5.10;
                        double usdAmountDouble = cadAmountDouble * 1.35;
                        rmbInput.setText(Double.toString(rmbAmountDouble)+" RMB");
                        usdInput.setText(Double.toString(usdAmountDouble)+" USD");
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
                        double cadAmountDouble = rmbAmountDouble /5.10;
                        double usdAmountDouble = rmbAmountDouble /6.89;
                        cadInput.setText(Double.toString(cadAmountDouble)+" CAD");
                        usdInput.setText(Double.toString(usdAmountDouble)+" USD");
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
                        double rmbAmountDouble = usdAmountDouble / 6.89;
                        double cadAmountDouble = usdAmountDouble / 1.35;
                        rmbInput.setText(Double.toString(rmbAmountDouble)+" RMB");
                        cadInput.setText(Double.toString(cadAmountDouble)+" CAD");
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
}
