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


public class FragTrade extends Fragment implements View.OnClickListener {

//自动要disable一个已选的币种

   // private static Integer[] imageIconDatabase =
          //  {R.drawable.cadsymbol, R.drawable.rmbsymbol, R.drawable.usdsymbol};

    private String[] imageNameDatabase
            = new String[] {"CAD", "RMB", "USD"};

    //List<Map<String, Object>> spinnerList = new ArrayList<>();
   /* private void initializeImageList() {
        // TODO Auto-generated method stub
        for (int i = 0; i < imageNameDatabase.length; i++) {
            Map<String, Object>  map = new HashMap<String, Object>();

            map.put("Name", imageNameDatabase[i]);
            map.put("Icon", imageIconDatabase[i]);
            spinnerList.add(map);
        }
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource((Integer) spinnerList.get(0).get("Icon"));
        spinnerList.get(0).get("Name");
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.frag_trade,container,false);
       // initializeImageList();
        Button button_deposit = (Button) v.findViewById(R.id.button_deposit);
        Button button_withdrawl = (Button) v.findViewById(R.id.button_withdrawl);
        Button button_buy = (Button) v.findViewById(R.id.button_buy);
        Button button_sell = (Button) v.findViewById(R.id.button_sell);



        button_deposit.setOnClickListener(this);
        button_withdrawl.setOnClickListener(this);
        button_buy.setOnClickListener(this);
        button_sell.setOnClickListener(this);


       /* Spinner s = (Spinner) v.findViewById(R.id.spinner_intype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, imageNameDatabase);
        s.setAdapter(adapter);*/

        /*Spinner spin = (Spinner) v.findViewById(R.id.spinner_intype);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(),
                spinnerList, R.layout.row, new String[] { "Name",
                "Icon" }, new int[] { R.id.weekofday,
                R.id.spinner_icon });
        spin.setAdapter(adapter);*/
        //选完第一个后，enable第二个spinner，最好有textview显示选了哪个币种
        return v;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_deposit:// 朋友圈
                //Utils.start_Activity(getActivity(), AlbumActivity.class);

                break;
            case R.id.button_withdrawl:// 扫一扫
                //Utils.start_Activity(getActivity(), CaptureActivity.class);

                break;
            case R.id.button_buy:
               /* Utils.start_Activity(getActivity(), PublicActivity.class,
                        new BasicNameValuePair(Constants.NAME, getString(R.string.shake)));*/
                break;
            case R.id.button_sell:
                /*Utils.start_Activity(getActivity(), PublicActivity.class,
                        new BasicNameValuePair(Constants.NAME, getString(R.string.people_nearby)));*/
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
