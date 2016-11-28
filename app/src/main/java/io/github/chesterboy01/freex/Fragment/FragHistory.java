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

import static io.github.chesterboy01.freex.R.id.rate;


/*Action: searchTransactionHistory
        成功：JSONArray对象
        失败：searchTFail字符串*/
public class FragHistory extends Fragment {
//部署ultra-pull 下拉刷新

    public UserPass mListener;
    User conUser;
    View v;
    ArrayList<HashMap<String, Object>> mylist;
    ListView list;

    boolean fetchHistory_flag;
    boolean fetchHistory_result;

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
            list = (ListView) v.findViewById(R.id.lvContact);
            mylist = new ArrayList<HashMap<String,Object>>();
            //new FetchAllTransactionHistory().execute(conUser);
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
                String strrate = Double.valueOf(inamount/outamount).toString();
                int point = strrate.lastIndexOf(".");
                if (point >= strrate.length()-3)
                    map.put("rate", strrate.substring(0,strrate.length()-1));
                else
                    map.put("rate", strrate.substring(0,point+4));
                //map.put("rate", (float)inamount/outamount);
                mylist.add(map);
            }

            SimpleAdapter mSchedule = new SimpleAdapter(getContext(), //没什么解释
                    mylist,//数据来源
                    R.layout.history_list_item,//ListItem的XML实现

                    //动态数组与ListItem对应的子项
                    new String[] {"intype", "inamount", "outtype", "outamount", "rate"},

                    //ListItem的XML文件里面的两个TextView ID
                    new int[] {R.id.intype,R.id.inamount, R.id.outtype, R.id.outamount, rate});
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
            return "CNY";
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

    /*public class FetchAllTransactionHistory extends AsyncTask<User, Void, Boolean> {
        JSONArray jsonArray;
        protected void onPreExecute() {

        }
        @SuppressWarnings("deprecation")
        protected Boolean doInBackground(User... params) {
            //params[0]就是我要传进来的user对象
            try {
                String URL = "http://192.168.95.1:8080/FreeX_Server/searchTransactionHistory.action";
                String result = null;
                HttpPost request = new HttpPost(URL);;
                try{
                    JSONObject json = new JSONObject();
                    json.put("username",params[0].getUsername());
                    json.put("password",params[0].getPassword());
                    json.put("email",params[0].getEmail());
                    StringEntity se = new StringEntity(json.toString(),"utf-8");
                    request.setEntity(se);
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

                if (result.equals("searchTFail"))
                    fetchHistory_result = false;
                else{
                    jsonArray = new JSONArray(result);
                    JSONObject firstItem = (JSONObject) jsonArray.get(0);
                    if (firstItem.getInt("thuid") > 0) {
                        fetchHistory_result = true;
                    }
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject tmp = (JSONObject) jsonArray.get(i);

                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("intype", tmp.getInt("cidin"));
                        map.put("inamount", tmp.getString("thamount"));
                        map.put("outtype", tmp.getInt("cidout"));

                        String ratestring = tmp.getString("rate");
                        double in_amount = Double.valueOf(tmp.getString("thamount"));
                        double rate_double = Double.valueOf(ratestring);
                        float outAmount_float = (float) (in_amount*rate_double);
                        //减少rate的显示位数，使用float类型
                        map.put("outamount", (new Float(outAmount_float)).toString());
                        map.put("rate", ratestring);
                        mylist.add(map);
                    }
                    SimpleAdapter mSchedule = new SimpleAdapter(getContext(), //没什么解释
                            mylist,//数据来源
                            R.layout.history_list_item,//ListItem的XML实现

                            //动态数组与ListItem对应的子项
                            new String[] {"intype", "inamount", "outtype", "outamount", "rate"},

                            //ListItem的XML文件里面的两个TextView ID
                            new int[] {R.id.intype,R.id.inamount, R.id.outtype, R.id.outamount, rate});
                    //添加并且显示
                    //must be called from the UI thread
                    //list.setAdapter(mSchedule);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            fetchHistory_flag = true;
            return fetchHistory_result;
        }
        protected void onPostExecute(Boolean... params) {
        }
        void setBalance(JSONObject jo, Balance bal) throws JSONException {
            bal.setBamount(jo.getString("bamount"));
            bal.setBid(jo.getInt("bid"));
            bal.setBuid(jo.getInt("buid"));
            bal.setBcid(jo.getInt("bcid"));
        }
    }*/
}
