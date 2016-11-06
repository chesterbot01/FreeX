package io.github.chesterboy01.freex.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import io.github.chesterboy01.freex.R;


public class FragHistory extends Fragment {
//部署ultra-pull 下拉刷新

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_history,container,false);
        ListView list = (ListView) v.findViewById(R.id.lvContact);
        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<30;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            int inamount =  amountOfCucurrencyGenerator();
            int outamount = amountOfCucurrencyGenerator();
            map.put("intype", typeOfCucurrencyGenerator(i));
            map.put("inamount", inamount);
            map.put("outtype", typeOfCucurrencyGenerator(i+1));
            map.put("outamount", outamount);
            map.put("rate", inamount/outamount);
            mylist.add(map);
        }

        SimpleAdapter mSchedule = new SimpleAdapter(getContext(), //没什么解释
                mylist,//数据来源
                R.layout.history_list_item,//ListItem的XML实现

                //动态数组与ListItem对应的子项
                new String[] {"intype", "inamount", "outtype", "outamount", "rate"},

                //ListItem的XML文件里面的两个TextView ID
                new int[] {R.id.intype,R.id.inamount, R.id.outtype, R.id.outamount, R.id.rate});
        //添加并且显示
        list.setAdapter(mSchedule);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    //生成货币的种类
    public String typeOfCucurrencyGenerator(int i){

        if (i % 3 == 0)
            return "CAD";
        else if (i % 3 == 1)
            return "RMB";
        else if (i % 3 == 2)
            return "USD";
        else
            return "FUCK";
    }
    //随机生成货币的数量
    public int amountOfCucurrencyGenerator() {
        Random random = new Random();
        int amount = random.nextInt(10000) % (10000 - 10 + 1) + 10;
        return amount;
    }
}
