package io.github.chesterboy01.freex.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import io.github.chesterboy01.freex.R;



public class FragIndex extends Fragment {
    private ListView lview;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;


    //private OnFragmentInteractionListener mListener;

    public FragIndex() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    /*public static FragIndex newInstance(String param1, String param2) {
        FragIndex fragment = new FragIndex();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.frag_index,container,false);
       lview = (ListView) v.findViewById(R.id.listview);
        /* TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;*/
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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



    public void refresh() {
        //conversationList.clear();
        initViews();
    }

    private void initViews() {
       /* conversationList.addAll(loadConversationsWithRecentChat());
        if (conversationList != null && conversationList.size() > 0) {
            layout.findViewById(R.id.txt_nochat).setVisibility(View.GONE);
            adpter = new NewMsgAdpter(getActivity(), conversationList);
            // TODO 加载订阅号信息 ，增加一个Item
            // if (GloableParams.isHasPulicMsg) {
            EMConversation nee = new EMConversation("100000");
            conversationList.add(0, nee);
            String time = Utils.getValue(getActivity(), "Time");
            String content = Utils.getValue(getActivity(), "Content");
            time = "下午 02:45";
            content = "[腾讯娱乐] 赵薇炒股日赚74亿";
            PublicMsgInfo msgInfo = new PublicMsgInfo();
            msgInfo.setContent(content);
            msgInfo.setMsg_ID("12");
            msgInfo.setTime(time);
            adpter.setPublicMsg(msgInfo);
            // }
            lvContact.setAdapter(adpter);
        } else {
            layout.findViewById(R.id.txt_nochat).setVisibility(View.VISIBLE);
        }
    }*/

    }
}
