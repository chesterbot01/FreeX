package io.github.chesterboy01.freex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.view.View;

import java.util.List;
import java.util.Map;

import io.github.chesterboy01.freex.R;

/**
 * Created by Administrator on 11/6/2016.
 */

public class CustomSpinnerAdapter extends SimpleAdapter {

    LayoutInflater mInflater;
    private List<? extends Map<String, ?>> dataRecieved;

    public CustomSpinnerAdapter(Context context, List<? extends Map<String, ?>> data,
                                int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        dataRecieved =data;
        mInflater= LayoutInflater.from(context);
    }

    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row,
                    null);
        }
        //  HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        ((TextView) convertView.findViewById(R.id.weekofday))
                .setText((String) dataRecieved.get(position).get("Name"));
        ((ImageView) convertView.findViewById(R.id.spinner_icon))
                .setBackgroundResource((Integer) dataRecieved.get(position).get("Icon"));
        return convertView;
    }
}