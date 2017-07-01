package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.utils.BitmapUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.MyCircleImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SetBrokerInfoMainActivity extends Activity implements View.OnClickListener {


    private ImageView ivHorphoto;
    private LinearLayout llHorphotoUpbtn;
    private View setBrokerIcon;
    private RelativeLayout rlResetpasswordBtn;
    private RelativeLayout rlResetinfoBtn;
    private TextView tvServicePhone;
    private MyCircleImageView myIcon;
    private String brokerIcon;

    private TextView tvBrokerName;
    private TextView tvBrokerPhone;
    private TextView tvEmail;
    private TextView tvCity;
    private TextView tvWork;
    private String brokerName;
    private String eMail;
    private String city;
    private String work;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "个人资料", true, false);
        setContentView(R.layout.activity_set_brokerinfo_main);


        initView();
    }

    private void initView() {

        ivHorphoto = (ImageView) findViewById(R.id.iv_horphoto);//形象照片
        llHorphotoUpbtn = (LinearLayout) findViewById(R.id.ll_horphoto_upbtn);//更新形象照片按钮
        String horImgString = AppSharePrefManager.getInstance(this).getLastest_hengzhao_imagebase64();
        if(!TextUtils.isEmpty(horImgString)){
            llHorphotoUpbtn.setVisibility(View.GONE);
            ivHorphoto.setImageBitmap(BitmapUtil.base64ToBitmap(horImgString));
        }


        setBrokerIcon = (View) findViewById(R.id.set_broker_icon);//头像
        myIcon = (MyCircleImageView) setBrokerIcon.findViewById(R.id.set_icon);//头像展示View

        rlResetpasswordBtn = (RelativeLayout) findViewById(R.id.rl_resetpassword_btn);//重设密码
        rlResetinfoBtn = (RelativeLayout) findViewById(R.id.rl_resetinfo_btn);//重设资料

        tvServicePhone = (TextView) findViewById(R.id.tv_service_phone);//客服电话


        tvBrokerName = (TextView) findViewById(R.id.tv_brokerName);//名字
        tvBrokerPhone = (TextView) findViewById(R.id.tv_broker_phone);//电话
        tvEmail = (TextView) findViewById(R.id.tv_email);//email
        tvCity = (TextView) findViewById(R.id.tv_city);//城市
        tvWork = (TextView) findViewById(R.id.tv_work);//工作单位


        showDatas();

        //上传照片按钮
        registerForContextMenu(llHorphotoUpbtn);
        registerForContextMenu(setBrokerIcon);


        ivHorphoto.setOnClickListener(this);
        llHorphotoUpbtn.setOnClickListener(this);
        setBrokerIcon.setOnClickListener(this);
        rlResetpasswordBtn.setOnClickListener(this);
        rlResetinfoBtn.setOnClickListener(this);
        tvServicePhone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            case R.id.iv_horphoto:
            case R.id.ll_horphoto_upbtn:
                //Toast.makeText(this, "添加形象照片", Toast.LENGTH_SHORT).show();
                //iv_addpic.setVisibility(View.GONE);
                //添加形象照片
                openContextMenu(llHorphotoUpbtn);
                break;
            case R.id.set_broker_icon:
                // iv_addphoto.setVisibility(View.GONE);
                //Toast.makeText(this, "添加大头像", Toast.LENGTH_SHORT).show();
                //添加大头像
                openContextMenu(setBrokerIcon);
                break;

            case R.id.rl_resetpassword_btn:
                //重设密码
                //finish();
                intent = new Intent(this, RePasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_resetinfo_btn:
                //重设资料
                intent = new Intent(this, RegistTwoActivity.class);
                intent.putExtra("title", "重设审核资料");
                startActivity(intent);
                break;
        }

    }


    /**
     * 展示数据
     */
    private void showDatas() {
        AppSharePrefManager sharePrefManager = AppSharePrefManager.getInstance(this);

        //经纪人名字
        brokerName = sharePrefManager.getLastest_login_username();
        if (!TextUtils.isEmpty(brokerName)) {
            tvBrokerName.setText(brokerName);
        }

        //电话
        phone = sharePrefManager.getLastest_login_phone_num();
        if (!TextUtils.isEmpty(phone)) {
            tvBrokerPhone.setText(phone);
        }
        //大头照
        brokerIcon = sharePrefManager.getLastest_login_touxiang_imagebase64();
        if (!TextUtils.isEmpty(brokerIcon)) {
            myIcon.setImageBitmap(BitmapUtil.base64ToBitmap(brokerIcon));
        }
        //邮箱
        eMail = sharePrefManager.getLastest_email();
        if (!TextUtils.isEmpty(brokerIcon)) {
            tvEmail.setText(eMail);
        }
        //城市
        city = sharePrefManager.getLastest_placedesc();
        if (!TextUtils.isEmpty(city)) {
            tvCity.setText(city);
        }


        work = sharePrefManager.getLastest_companyname();
        if (!TextUtils.isEmpty(work)) {
            tvWork.setText(work);
        }

    }


    private Uri PhotoUri;
    //private String uploadFile = "";  //图像剪切保存路径

    private static final int PIC_FROM_CAMERA = Menu.FIRST << 0;//拍照
    private static final int PIC_FROM_LOCALPHOTO = Menu.FIRST << 1;//本地

    //private static final int PIC_FROM_CAMERA = 1 << 0;   //拍照获取头像
    //private static final int PIC_FROM_LOCALPHOTO = 1 << 1;  //本地获取

    private int actionType;//动作类型

    private static final int HORPHOTO = 1 << 0; //形象照片
    private static final int ICON = 1 << 1;//大头照

    private int picType;//照片类型 :身份证 , 竖照


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {

        switch (v.getId()) {

            case R.id.ll_horphoto_upbtn:

                picType = HORPHOTO;
                //添加身份证照片
                menu.add(picType, PIC_FROM_CAMERA, 0, "形象照-拍照");
                menu.add(picType, PIC_FROM_LOCALPHOTO, 0, "形象照-本地");
                break;
            case R.id.set_broker_icon:

                picType = ICON;
                //添加竖照
                menu.add(picType, PIC_FROM_CAMERA, 0, "大头照-拍照");
                menu.add(picType, PIC_FROM_LOCALPHOTO, 0, "大头照-本地");
                break;
        }
    }


    //菜单单击响应
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        actionType = item.getItemId();
        doHandlerPhoto(actionType);
        return super.onContextItemSelected(item);
    }

    /**
     * 根据不同方式选择图片设置ImageView
     *
     * @param actionType
     */

    private void doHandlerPhoto(int actionType) {
        try {
            //保存剪裁后的图片文件
            File pictureFileDir = new File(getExternalCacheDir(), "pic");
            //File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/upload");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            File picFile = new File(pictureFileDir, "brokerpic.png");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }

            PhotoUri = Uri.fromFile(picFile);
            Intent intent = null;

            switch (actionType) {

                case PIC_FROM_LOCALPHOTO:
                    //类型本地
                    intent = getCropImageIntent();
                    break;

                case PIC_FROM_CAMERA:
                    //类型拍照
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUri);
                    break;
            }

            startActivityForResult(intent, actionType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用图片剪辑程序
     */
    private Intent getCropImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        setIntentParams(intent);
        return intent;
    }

    /**
     * 设置公用参数
     */
    private void setIntentParams(Intent intent) {

        intent.putExtra("crop", "true");
        switch (picType) {

            case HORPHOTO: {
                intent.putExtra("aspectX", 75);
                intent.putExtra("aspectY", 48);
                intent.putExtra("outputX", 750);
                intent.putExtra("outputY", 480);
                break;
            }
            case ICON: {

                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 150);
                intent.putExtra("outputY", 150);
                break;
            }

        }
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //判断取消的情况
        if (resultCode == RESULT_CANCELED)
            return;
        Bitmap bitmap = null;

        //Log.e("groupid", "data.getFlags()" + data.getFlags());

        switch (requestCode) {

            case PIC_FROM_CAMERA://拍照
                try {
                    cropImageUriByTakePhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PIC_FROM_LOCALPHOTO://本地获取
                try {
                    if (PhotoUri != null) {

                        Log.e("czhongzhi", "PhotoUri -- " + PhotoUri);

                        //加载图片
                        bitmap = decodeUriAsBitmap(PhotoUri);

                        switch (picType) {

                            case HORPHOTO://形象照片展示
                                ivHorphoto.setImageBitmap(bitmap);
                                llHorphotoUpbtn.setVisibility(View.GONE);

                                String h_ImageBase64 = BitmapUtil.bitmapToBase64(bitmap);
                                setUserIcon(h_ImageBase64,"userimage_hor_filecontent","userimage_hor_fileext");
                                AppSharePrefManager.getInstance(SetBrokerInfoMainActivity.this).setLastest_hengzhao_imagebase64(h_ImageBase64);

                                break;
                            case ICON:  //大头照显示

                                myIcon.setImageBitmap(bitmap);

                                String filecontent = BitmapUtil.bitmapToBase64(bitmap);
                                setUserIcon(filecontent,"userimage_filecontent","userimage_fileext");
                                AppSharePrefManager.getInstance(SetBrokerInfoMainActivity.this).setLastest_login_touxiang_imagebase64(filecontent);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void cropImageUriByTakePhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(PhotoUri, "image/*");
        setIntentParams(intent);
        startActivityForResult(intent, PIC_FROM_LOCALPHOTO);
    }


    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    /**
     * 更新用户图片
     */
    private void setUserIcon(String filecontent,String imgt,String imgg) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", AppSharePrefManager.getInstance(this).getLastest_login_id());
        map.put(imgg, "jpg");
        map.put(imgt, filecontent);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("setUserIcon is " + result);
                if(TextUtils.isEmpty(result))
                    return;
                String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                if (state.equals("true")) {//修改成功
                    Toast.makeText(SetBrokerInfoMainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                } else {//修改失败
                    Toast.makeText(SetBrokerInfoMainActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(Constants.updateUrl, map);
    }


}
