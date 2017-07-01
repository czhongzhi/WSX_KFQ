package com.inetgoes.fangdd.adapter;

import android.content.Context;
import android.text.TextUtils;
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
 * Created by android on 2015/11/20.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context context;

    private List<HouseinfoHuxing> huxingarr;

    public GridViewAdapter(Context context, List<HouseinfoHuxing> huxingarr) {

        this.huxingarr = huxingarr;
        this.context = context;
    }

    @Override
    public int getCount() {
        return huxingarr.size();
    }

    @Override
    public Object getItem(int position) {
        return huxingarr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 房源的holder
     */
    class ViewHolder {
        ImageView fangyuan_ItemImage;
        TextView fangyuan_ItemText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();

            convertView = View.inflate(context, R.layout.item_building_type, null);

            viewHolder.fangyuan_ItemImage = (ImageView) convertView.findViewById(R.id.ItemImage);
            viewHolder.fangyuan_ItemText = (TextView) convertView
                    .findViewById(R.id.ItemText);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        HouseinfoHuxing hx = huxingarr.get(position);

        if (hx != null) {
            String image_Huxing = hx.getHuxing_imageurl();
            if (!TextUtils.isEmpty(image_Huxing)) {
                ImageLoader.getInstance().displayImage(image_Huxing, viewHolder.fangyuan_ItemImage, FangApplication.options, FangApplication.animateFirstListener);
            }

            String fangyuan_Info = hx.getHuxing_size() + "/" + hx.getHuxing_desc();
            viewHolder.fangyuan_ItemText.setText(fangyuan_Info);

        }

        return convertView;

    }
}
