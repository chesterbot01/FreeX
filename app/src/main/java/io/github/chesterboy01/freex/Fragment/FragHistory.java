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
import io.github.chesterboy01.freex.UserPass;
import io.github.chesterboy01.freex.entity.User;


/*Action: searchTransactionHistory
        成功：JSONArray对象
        失败：searchTFail字符串*/
public class FragHistory extends Fragment {
//部署ultra-pull 下拉刷新

    public UserPass mListener;
    User conUser;
    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(v == null){
            v = inflater.inflate(R.layout.frag_history,container,false);
            conUser = mListener.getUser();
            ListView list = (ListView) v.findViewById(R.id.lvContact);
            ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
            for(int i=0;i<30;i++)
            {
                HashMap<String, Object> map = new HashMap<String, Object>();
                float inamount =  amountOfCucurrencyGenerator();
                float outamount = amountOfCucurrencyGenerator();
                map.put("intype", typeOfCucurrencyGenerator(i));
                map.put("inamount", inamount);
                map.put("outtype", typeOfCucurrencyGenerator(i+1));
                map.put("outamount", outamount);
                //减少rate的显示位数，使用float类型
                map.put("rate", (float)inamount/outamount);
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
        }
        else{
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeView(v);
            }
        }
        return v;
    }


    public void onButtonPressed(Uri uri) {
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
    public float amountOfCucurrencyGenerator() {
        Random random = new Random();
        int amount = random.nextInt(10000) % (10000 - 10 + 1) + 10;
        float result = (float)(amount/1.0);
        return result;
    }
}
