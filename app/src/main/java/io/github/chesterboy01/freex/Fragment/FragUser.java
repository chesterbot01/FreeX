package io.github.chesterboy01.freex.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.chesterboy01.freex.R;
import io.github.chesterboy01.freex.UserPass;
import io.github.chesterboy01.freex.entity.User;


public class FragUser extends Fragment {

    public UserPass mListener;
    User conUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        conUser = mListener.getUser();
        return textView;
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
