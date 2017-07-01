package com.inetgoes.kfqbrokers.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.OnHousePhotoListener;
import com.inetgoes.kfqbrokers.model.HouseTypeGridViewAdapterinfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by android on 2015/11/20.
 * 房源户型图片的上传gridViewAdapter
 * 有两个类型的item  上面都有点击
 */
public class HouseTypeGridViewAdapter extends BaseAdapter {

    private static final int TYPE_IMG = 0;//item类型 图片
    private static final int TYPE_ADD = 1;//item类型 添加按钮

    private Context context;
    private List<HouseTypeGridViewAdapterinfo> houseTypeList;//房屋类型info集合
    private OnHousePhotoListener onHousePhotoListener;//添加itemView的接口

    private HouseTypeGridViewAdapterinfo itemInfo;//图片信息bean
    private String itmeUri;//图片地址
    private String itmeShow;//图片说明

    public HouseTypeGridViewAdapter(Context context, List<HouseTypeGridViewAdapterinfo> houseTypeList) {

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

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        switch (getItemViewType(position)) {

            case TYPE_IMG:
                itemInfo = houseTypeList.get(position);
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_uploadhousesource_imagetype, null);
                    holder.iv_HousePhoto = (ImageView) convertView.findViewById(R.id.iv_house_photo);
                    holder.iv_HousePhotoDel = (ImageView) convertView.findViewById(R.id.iv_house_photo_delete);
                    holder.tv_PhotoShow = (TextView) convertView.findViewById(R.id.tv_projectname);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                //展示图片
                ImageLoader.getInstance().displayImage(itemInfo.getItmeUri(), holder.iv_HousePhoto, FangApplication.options, FangApplication.animateFirstListener);
                //图片说明
                itmeShow = itemInfo.getItmeShow();
                if (TextUtils.isEmpty(itmeShow)) {
                    holder.tv_PhotoShow.setText("请添加说明");

                } else {
                    holder.tv_PhotoShow.setText(itmeShow);
                }

                //删除图片
                holder.iv_HousePhotoDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        houseTypeList.remove(position);
                        HouseTypeGridViewAdapter.this.notifyDataSetChanged();
                    }
                });

                //添加图片说明
                holder.tv_PhotoShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHousePhotoListener.addHouseTypeShow(v, position);
                    }
                });
                break;

            case TYPE_ADD:
                //添加图片
                convertView = View.inflate(context, R.layout.item_uploadhousesource_addtype, null);

                convertView.findViewById(R.id.ll_horphoto_upbtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHousePhotoListener.addHouseTypePhoto();
                    }
                });

                break;
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv_HousePhoto;
        private ImageView iv_HousePhotoDel;
        private TextView tv_PhotoShow;
    }

}
