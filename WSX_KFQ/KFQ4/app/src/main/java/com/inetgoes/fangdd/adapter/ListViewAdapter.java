package com.inetgoes.fangdd.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.model.HouseinfoHuxing;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by czz on 2015/12/29.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<HouseinfoHuxing> list;
    private LayoutInflater inflater;

    public ListViewAdapter(Context context, List<HouseinfoHuxing> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolper vh = null;
        if(convertView == null){
            vh = new ViewHolper();
            convertView = this.inflater.inflate(R.layout.item_selecthuxing,parent,false);

            vh.item_huxing_icon = (ImageView) convertView.findViewById(R.id.item_huxing_icon);
            vh.item_huxing_title = (TextView) convertView.findViewById(R.id.item_huxing_title);
            vh.item_huxing_content = (TextView) convertView.findViewById(R.id.item_huxing_content);
            vh.item_huxing_price = (TextView) convertView.findViewById(R.id.item_huxing_price);

            convertView.setTag(vh);
        }else {
            vh = (ViewHolper) convertView.getTag();
        }

        HouseinfoHuxing hx = list.get(position);
        if(!TextUtils.isEmpty(hx.getHuxing_imageurl())){
            ImageLoader.getInstance().displayImage(hx.getHuxing_imageurl(),vh.item_huxing_icon, FangApplication.options_R,FangApplication.animateFirstListener);
        }
        vh.item_huxing_title.setText(hx.getHuxing_type());
        vh.item_huxing_content.setText(hx.getHuxing_desc());

        //vh.item_huxing_price

        return convertView;
    }

    private class ViewHolper {
        private ImageView item_huxing_icon;
        private TextView item_huxing_title, item_huxing_content, item_huxing_price;
    }
}
