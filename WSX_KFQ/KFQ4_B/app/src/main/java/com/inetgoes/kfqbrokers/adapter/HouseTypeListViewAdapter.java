package com.inetgoes.kfqbrokers.adapter;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.OnHousePhotoListener;
import com.inetgoes.kfqbrokers.model.HouseTypeGridViewAdapterinfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by czz on 2015/12/30.
 */
public class HouseTypeListViewAdapter extends BaseAdapter{

    private static final int TYPE_IMG = 0;//item类型 图片
    private static final int TYPE_ADD = 1;//item类型 添加按钮

    public static int selectindex = -1;  //当前选择的item

    private Context context;
    private List<HouseTypeGridViewAdapterinfo> houseTypeList;//房屋类型info集合
    private OnHousePhotoListener onHousePhotoListener;//添加itemView的接口

    private HouseTypeGridViewAdapterinfo itemInfo;//图片信息bean
    private String itmeUri;//图片地址
    private String itmeShow;//图片说明


    public HouseTypeListViewAdapter(Context context, List<HouseTypeGridViewAdapterinfo> houseTypeList) {

        this.houseTypeList = houseTypeList;
        this.context = context;
        this.onHousePhotoListener = (OnHousePhotoListener) context;
    }

    @Override
    public int getCount() {
        return houseTypeList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return houseTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        return position == (houseTypeList.size()) ? TYPE_ADD : TYPE_IMG;

    }

    public int getSelectindex(){
        return selectindex;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        switch (getItemViewType(position)) {

            case TYPE_IMG:
                itemInfo = houseTypeList.get(position);
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_uploadhuxing, null);
                    holder.upload_huxing_icon = (ImageView) convertView.findViewById(R.id.upload_huxing_icon);
                    holder.upload_huxing_select = (RelativeLayout) convertView.findViewById(R.id.upload_huxing_select);
                    holder.upload_huxing_del = (ImageView) convertView.findViewById(R.id.upload_huxing_del);

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                //展示图片
                if(!TextUtils.isEmpty(itemInfo.getItmeUri())){
                    holder.upload_huxing_icon.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(itemInfo.getItmeUri(), holder.upload_huxing_icon, FangApplication.options, FangApplication.animateFirstListener);
                    holder.upload_huxing_select.setVisibility(View.GONE);
                }else{
                    holder.upload_huxing_icon.setVisibility(View.GONE);
                    holder.upload_huxing_select.setVisibility(View.VISIBLE);
                }


                //删除图片
                holder.upload_huxing_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        houseTypeList.remove(position);
                        HouseTypeListViewAdapter.this.notifyDataSetChanged();
                    }
                });

                holder.upload_huxing_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectindex = position;
                        onHousePhotoListener.addHouseTypePhoto();
                    }
                });

                //添加图片说明
//                holder.tv_PhotoShow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onHousePhotoListener.addHouseTypeShow(v, position);
//                    }
//                });
                break;

            case TYPE_ADD:
                //添加图片
                convertView = View.inflate(context, R.layout.item_uploadhuxing_add, null);

                convertView.findViewById(R.id.upload_huxing_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHousePhotoListener.addHouseHuxing();
                    }
                });

                break;
        }
        return convertView;
    }

    private class ViewHolder {
//        private ImageView iv_HousePhoto;
//        private ImageView iv_HousePhotoDel;
//        private TextView tv_PhotoShow;

        private ImageView upload_huxing_icon;
        private RelativeLayout upload_huxing_select;

        private ImageView upload_huxing_del;

        private EditText upload_huxing_shi;
        private EditText upload_huxing_ting;
        private EditText upload_huxing_wei;
        private EditText upload_huxing_mian;
        private EditText upload_huxing_wang;

    }
}
