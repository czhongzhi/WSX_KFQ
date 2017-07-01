package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.adapter.HousePhotoAdapter;
import com.inetgoes.kfqbrokers.adapter.HouseTypeGridViewAdapter;
import com.inetgoes.kfqbrokers.adapter.HouseTypeListViewAdapter;
import com.inetgoes.kfqbrokers.asynctast.OnHousePhotoListener;
import com.inetgoes.kfqbrokers.model.HouseInfoBasedataResp;
import com.inetgoes.kfqbrokers.model.HouseTypeGridViewAdapterinfo;
import com.inetgoes.kfqbrokers.utils.AloneDeal;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.MyGridView;
import com.inetgoes.kfqbrokers.view.MyListView;
import com.lling.photopicker.PhotoPickerActivity;
import com.lling.photopicker.utils.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 上传房源类2
 */
public class UploadHouseSourceTwoActivity extends Activity implements View.OnClickListener, OnHousePhotoListener, ViewPager.OnPageChangeListener {


    private EditText edProjectName;
    private ViewPager vpHousePhotoList;
    private LinearLayout llHousePhotoBtn;
    private EditText edHousePrice;
    private TextView tvProvince;
    private TextView tvCity;
    private TextView tvArea;
    private EditText edAddress;
    private MyGridView houseTypeGridView;

    private MyListView houseTypeListView;

    private Button housesourceTwoNextbtn;
    private List<View> imageViews = new ArrayList();
    private List<HouseTypeGridViewAdapterinfo> houseTypePhotoinfoList = new ArrayList<>();

    private HouseTypeGridViewAdapter houseTypeGridViewAdapter;
    private HouseTypeListViewAdapter houseTypeListViewAdapter;


    private HousePhotoAdapter adapter;
    private LinearLayout pointContainer;
    private ImageView housePhotoDelete;
    private HouseInfoBasedataResp houseInfo;
    private ArrayList<Drawable> loufangImages = new ArrayList();
    private PopupWindow popupWindow;

    private static final String[] privinces = {"广东省"};
    private static final String[] citys = {"深圳市", "东莞市"};
    private static final String[] sz_areas = {"福田区", "龙华新区", "罗湖区",
            "南山区", "龙岗区", "盐田区", "坪山新区", "大鹏新区", "宝安区", "光明新区"};
    private static final String[] dg_areas = {"莞城街道", "南城街道", "东城街道", "万江街道"};


    private String t_Province = "广东省";
    private String t_City = "深圳市";
    private String t_Area = "福田区";
    private String projectName;
    private String pricedesc;
    private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "上传房源", true, false);
        setContentView(R.layout.activity_upload_housesource_two);

        initView();

        initData();
    }


    private void initView() {

        edProjectName = (EditText) findViewById(R.id.ed_project_name);//项目名称
        vpHousePhotoList = (ViewPager) findViewById(R.id.viewpager_house_photolist);//楼盘照片
        llHousePhotoBtn = (LinearLayout) findViewById(R.id.ll_house_photo_btn);//上传楼盘照片按钮
        pointContainer = (LinearLayout) findViewById(R.id.point_container);//小点的容器
        housePhotoDelete = (ImageView) findViewById(R.id.iv_house_photo_delete);//图片删除按钮
        edHousePrice = (EditText) findViewById(R.id.ed_house_price);//均价
        tvProvince = (TextView) findViewById(R.id.tv_province);//省份
        tvCity = (TextView) findViewById(R.id.tv_city);//城市
        tvArea = (TextView) findViewById(R.id.tv_area);//区/县
        edAddress = (EditText) findViewById(R.id.ed_address);//详细地址
        //houseTypeGridView = (MyGridView) findViewById(R.id.gridview_housesource_type);//户型gridview

        houseTypeListView = (MyListView) findViewById(R.id.listView_housesource_type);//户型listview

        housesourceTwoNextbtn = (Button) findViewById(R.id.housesource_two_nextbtn);//下一步按钮


        llHousePhotoBtn.setOnClickListener(this);
        housesourceTwoNextbtn.setOnClickListener(this);
        //vpHousePhotoList.setOnClickListener(this);
        housePhotoDelete.setOnClickListener(this);
        tvProvince.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvArea.setOnClickListener(this);


        houseInfo = new HouseInfoBasedataResp(); //房屋信息的bean
    }


    private void initData() {

        //房源主图ListView加载
        adapter = new HousePhotoAdapter(imageViews);
        vpHousePhotoList.setAdapter(adapter);
        housePhotoDelete.setVisibility(View.GONE);
        //页面选中时设置对应的点
        vpHousePhotoList.setOnPageChangeListener(this);

        //房源类型GridView加载
        //houseTypeGridViewAdapter = new HouseTypeGridViewAdapter(this, houseTypePhotoinfoList);
        //houseTypeGridView.setAdapter(houseTypeGridViewAdapter);

        houseTypeListViewAdapter = new HouseTypeListViewAdapter(this,houseTypePhotoinfoList);
        houseTypeListView.setAdapter(houseTypeListViewAdapter);
    }


    private static Boolean showCamera = false;//是否显示相机
    private static final int selectedMode = PhotoPickerActivity.MODE_MULTI; //多选模式
    private static final int maxNum = 8;//最多选取照片数
    private static final int PICK_PHOTO = 1;//房源主页ViewPager请求码
    private static final int HOUSE_TYPE = 0; //房源类型GridView请求码


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_house_photo_btn:

                //添加多个楼盘照片
                Intent intent = new Intent(this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
                startActivityForResult(intent, PICK_PHOTO);
                break;

            case R.id.tv_province:
                //省
                showPopup(tvProvince, privinces, 0);
                break;


            case R.id.tv_city:
                //市
                showPopup(tvCity, citys, 1);
                break;


            case R.id.tv_area:
                //区
                if ("深圳市".equals(t_City)) {
                    //tvArea.setText(sz_areas[0]);
                    showPopup(tvArea, sz_areas, 2);
                } else if ("东莞市".equals(t_City)) {
                    //tvArea.setText(dg_areas[0]);
                    showPopup(tvArea, dg_areas, 2);
                }
                break;

            case R.id.housesource_two_nextbtn:

                saveToBean();

                //下一步按钮
                Intent nextIntent = new Intent(this, UploadHouseSourceThreeActivity.class);
                nextIntent.putExtra("houseInfo", houseInfo);//传递bean;
                nextIntent.putExtra("imagelist", loufangImages);//图片集合
                startActivity(nextIntent);
                break;


            case R.id.iv_house_photo_delete://删除房源主照片

                DialogUtil.deleteHousePhoto(this, new AloneDeal() {
                    @Override
                    public void deal() {
                        //确定按钮
                        if (imageViews.size() != 0) {

                            //移除当前选中的页面id
                            int currentPhonoId = vpHousePhotoList.getCurrentItem();
                            imageViews.remove(currentPhonoId);
                            adapter.notifyDataSetChanged();

                            //移除当前的小点
                            pointContainer.removeView(pointContainer.getChildAt(currentPhonoId));
                            int count = pointContainer.getChildCount();
                            //重绘小点
                            currentPhonoId = vpHousePhotoList.getCurrentItem();
                            for (int i = 0; i < count; i++) {
                                View point = pointContainer.getChildAt(i);
                                point.setBackgroundResource(i == currentPhonoId ? R.drawable.point_selected
                                        : R.drawable.point_normal);
                            }
                            //显示添加按钮
                            llHousePhotoBtn.setVisibility(imageViews.size() == 0 ? View.VISIBLE : View.GONE);
                            //显示删除按钮
                            housePhotoDelete.setVisibility(imageViews.size() == 0 ? View.GONE : View.VISIBLE);

                        }
                    }
                }, null);
                break;
        }
    }

    /**
     * 保存页面信息为javaBean //TODO  向下传递双参数
     */
    private void saveToBean() {

        //项目名称
        projectName = edProjectName.getText().toString().trim();
        if (TextUtils.isEmpty(projectName)) {
            Toast.makeText(this, "项目名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        houseInfo.setLoupanname(projectName);

        //楼房鸟瞰图
        int ivi = imageViews.size();
        if (ivi == 0) {
            Toast.makeText(this, "楼房展示图没有", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < ivi; i++) {
            Drawable d = ((ImageView) imageViews.get(i)).getDrawable();
            loufangImages.add(d);
        }

        //均价
        pricedesc = edHousePrice.getText().toString().trim();
        if (TextUtils.isEmpty(pricedesc)) {
            Toast.makeText(this, "均价为空", Toast.LENGTH_SHORT).show();
            return;
        }
        houseInfo.setPricedesc(pricedesc);

        //项目地址
        address = tvProvince.getText().toString().trim() + tvCity.getText().toString().trim() + tvArea.getText().toString().trim() + edAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "项目地址为空", Toast.LENGTH_SHORT).show();
            return;
        }
        houseInfo.setAddress(address);

    }

    /**
     * 展示数据
     */
    private void showToView() {

        //todo 未写完
    }

    //取出照片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case HOUSE_TYPE: //楼盘户型图片
                try {
                    if (photoUri != null) {
                        String houseTypePhoto = photoUri.toString();

                        HouseTypeGridViewAdapterinfo info = houseTypePhotoinfoList.get(houseTypeListViewAdapter.getSelectindex());
                        info.setItmeUri(houseTypePhoto);
                        //刷新GridAdapter数据
                        houseTypeListViewAdapter.notifyDataSetInvalidated();
                        // AppSharePrefManager.getInstance(SetUserInfoActivity.this).setLastest_login_touxiang_imagebase64(filecontent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PICK_PHOTO:  //楼盘主图

                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                //imageViews.clear();
                for (int i = 0; i < result.size(); i++) {

                    ImageView iv = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_housetype_image, null);
                    String imageUrl = result.get(i);

                    if (!TextUtils.isEmpty(imageUrl)) {
                        ImageLoader.getInstance().display(imageUrl, iv, 0, 0);
                    }
                    imageViews.add(iv);

                    // 动态的去添加点
                    addPoints(i);
                }
                //刷新楼房照片数据
                adapter.notifyDataSetChanged();
                //隐藏添加按钮  显示删除按钮
                llHousePhotoBtn.setVisibility(View.GONE);
                housePhotoDelete.setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     * 动态的添加点到界面
     *
     * @param i 第几张图片
     */
    private void addPoints(int i) {

        View point = new View(this);
        point.setBackgroundResource(R.drawable.point_normal);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
        if (i != 0) {
            params.leftMargin = 10;
        } else {
            // 设置默认选中第0个
            point.setBackgroundResource(R.drawable.point_selected);
        }
        pointContainer.addView(point, params);
    }


    //添加房源类型图片
    @Override
    public void addHouseTypePhoto() {
        doHandlerPhoto();
    }

    //添加新的户型
    @Override
    public void addHouseHuxing(){
        houseTypePhotoinfoList.add(new HouseTypeGridViewAdapterinfo());
        houseTypeListViewAdapter.notifyDataSetChanged();
    }


    //添加房源说明
    @Override
    public void addHouseTypeShow(final View view, final int position) {
        //DialogUtil.addHouseTypeShow(this, view);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_housesource_type, null);
        final AlertDialog dialog = builder.setView(dialogView).show();
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        //dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);

        //确定按钮
        dialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder typeString = new StringBuilder();
                EditText etHouseArea = (EditText) dialogView.findViewById(R.id.et_house_area);
                typeString.append(etHouseArea.getText().toString().trim() + "平/");
                EditText etHouseRoom = (EditText) dialogView.findViewById(R.id.et_house_room);
                typeString.append(etHouseRoom.getText().toString().trim() + "室/");
                EditText etHouseHall = (EditText) dialogView.findViewById(R.id.et_house_hall);
                typeString.append(etHouseHall.getText().toString().trim() + "厅/");
                EditText etHouseToilet = (EditText) dialogView.findViewById(R.id.et_house_toilet);
                typeString.append(etHouseToilet.getText().toString().trim() + "卫");
                houseTypePhotoinfoList.get(position).setItmeShow(typeString.toString());
                houseTypeGridViewAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        //取消按钮
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


    private Uri photoUri;

    /**
     * 添加房屋类型图片
     */
    private void doHandlerPhoto() {
        try {
            //保存剪裁后的图片文件
            File pictureFileDir = new File(getExternalCacheDir(), "pic");
            //File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/upload");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }

            Date dt = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String fileName = sdf.format(dt);

            File picFile = new File(pictureFileDir, fileName + ".png");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }

            photoUri = Uri.fromFile(picFile);

            //类型本地
            Intent intent = new Intent(Intent.ACTION_PICK);
            //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1.3);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 210);
            intent.putExtra("outputY", 160);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, HOUSE_TYPE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private Bitmap decodeUriAsBitmap(Uri uri) {
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return bitmap;
//    }


    /**
     * 展示地址选项popupwin
     */
    private void showPopup(final View v, final String[] strs, final int pos) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_window, null);
        int width = v.getWidth();
        popupWindow = new PopupWindow(view, width, 300);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, android.R.id.text1, strs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                ((TextView) v).setText(strs[position]);
                switch (pos) {
                    case 0:
                        t_Province = strs[position];
                        break;
                    case 1:
                        t_City = strs[position];

                        if ("深圳市".equals(t_City)) {
                            //tvArea.setText(sz_areas[0]);
                            showPopup(tvArea, sz_areas, 2);
                        } else if ("东莞市".equals(t_City)) {
                            //tvArea.setText(dg_areas[0]);
                            showPopup(tvArea, dg_areas, 2);
                        }

                        break;
                    case 2:
                        t_Area = strs[position];
                        break;

                }
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAsDropDown(v);
    }


    /**
     * 页面选中时设置选中的点
     *
     * @param position 选中的页面id
     */
    @Override
    public void onPageSelected(int position) {

        int count = pointContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View point = pointContainer.getChildAt(i);
            point.setBackgroundResource(i == position ? R.drawable.point_selected
                    : R.drawable.point_normal);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

}

