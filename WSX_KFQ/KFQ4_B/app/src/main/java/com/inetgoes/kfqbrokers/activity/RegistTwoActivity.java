package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.IM_Util.XmppUtil;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.BrokerInfo;
import com.inetgoes.kfqbrokers.utils.AppUtil;
import com.inetgoes.kfqbrokers.utils.BitmapUtil;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.utils.HttpUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册页二
 */
public class RegistTwoActivity extends Activity implements View.OnClickListener {


    private EditText edBrokername;
    private EditText edId;
    private ImageView ivIdimage;
    private FrameLayout ivIdimagebtu;
    private EditText edPassword;
    private EditText edTopassword;
    private EditText edEmail;
    private EditText edCity;
    private EditText edWork;
    private EditText edRecommendcode;
    private ImageView ivVerticalphoto;
    private ImageView ivExaphoto;
    private Button brokerRegistBt;
    private ImageView iv_photodelete;
    private FrameLayout iv_AddPhoto;

    private String phString;
    private String brokerName;
    private String id;


    private static String idImageBase64;
    private static String verImageBase64;

    private String password;
    private String toPassword;
    private String email;
    private String city;
    private String work;
    private String recommendCode;  //推荐码
    private ImageView iv_addpic;
    private ImageView iv_addphoto;

    private Dialog dialog_wait;
    private String title; //如果为空就是注册  不为空就是更新信息
    private String submitDataUrl;  //提交接口

    private int userId;//经纪人id
    private BrokerInfo brokerInfo = new BrokerInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            CustomTitleBar.getTitleBar(this, title, true, false);
        } else {
            CustomTitleBar.getTitleBar(this, "房产专家加盟", true, false);
        }
        setContentView(R.layout.activity_regist_two);

        initView();

        if (!TextUtils.isEmpty(title)) {
            showDatas();
        }
    }

    private void initView() {

        edBrokername = (EditText) findViewById(R.id.ed_brokername);//名字
        edId = (EditText) findViewById(R.id.ed_id);//身份证
        ivIdimage = (ImageView) findViewById(R.id.iv_idimage);//身份证照片
        ivIdimagebtu = (FrameLayout) findViewById(R.id.fl_idimagebtu);//身份证照片添加按钮
        iv_addpic = (ImageView) findViewById(R.id.iv_addidimage);
        edPassword = (EditText) findViewById(R.id.ed_password);//密码
        edTopassword = (EditText) findViewById(R.id.ed_topassword);//重输密码
        edEmail = (EditText) findViewById(R.id.ed_email);//email
        edCity = (EditText) findViewById(R.id.ed_city);//城市
        edWork = (EditText) findViewById(R.id.ed_work);//工作
        edRecommendcode = (EditText) findViewById(R.id.ed_recommendcode);//推荐码
        ivVerticalphoto = (ImageView) findViewById(R.id.iv_verticalphoto);//竖照image
        iv_AddPhoto = (FrameLayout) findViewById(R.id.fl_addphoto);//竖照添加按钮
        iv_addphoto = (ImageView) findViewById(R.id.iv_annphoto);
        ivExaphoto = (ImageView) findViewById(R.id.iv_exaphoto);//示例照
        iv_photodelete = (ImageView) findViewById(R.id.iv_photodelete);//竖照删除按钮
        brokerRegistBt = (Button) findViewById(R.id.broker_regist_bt);//.提交按钮

        //上传照片按钮
        registerForContextMenu(ivIdimagebtu);
        registerForContextMenu(iv_AddPhoto);


        ivIdimagebtu.setOnClickListener(this);
        brokerRegistBt.setOnClickListener(this);
        iv_AddPhoto.setOnClickListener(this);
        iv_photodelete.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fl_idimagebtu:

//                iv_addpic.setVisibility(View.GONE);
                //添加身份证照片
                openContextMenu(ivIdimagebtu);
                break;
            case R.id.fl_addphoto:
//                iv_addphoto.setVisibility(View.GONE);
                //添加竖照
                openContextMenu(iv_AddPhoto);
                break;

            case R.id.iv_photodelete:
                //删除竖照
                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                iv_addphoto.setVisibility(View.VISIBLE);

                verImageBase64 = null;//图片加密字符串置为空
                ivVerticalphoto.setImageResource(R.drawable.default_boy);
                break;

            case R.id.broker_regist_bt:
                //提交按钮
                submitData();
                break;
        }
    }

    /**
     * 展示数据
     */
    private void showDatas() {
        AppSharePrefManager sharePrefManager = AppSharePrefManager.getInstance(this);

        edBrokername.setHint(sharePrefManager.getLastest_login_username());
        edId.setHint(sharePrefManager.getLastest_shenfenzhengno());
        ivIdimage.setImageBitmap(BitmapUtil.base64ToBitmap(sharePrefManager.getLastest_shenfenzheng_imagebase64()));
        edEmail.setHint(sharePrefManager.getLastest_email());
        edCity.setHint(sharePrefManager.getLastest_placedesc());
        edWork.setHint(sharePrefManager.getLastest_companyname());
        ivVerticalphoto.setImageBitmap(BitmapUtil.base64ToBitmap(sharePrefManager.getLastest_shuzhao_imagebase64()));
    }


    /**
     * 提交注册数据
     */
    private void submitData() {

        //broker手机号
        phString = this.getIntent().getStringExtra(Constants.brokerphone);

        //broker名字
        brokerName = edBrokername.getText().toString().trim();

        //经纪人身份证号
        id = edId.getText().toString().trim();

        //密码
        password = edPassword.getText().toString().trim();

        //确认密码
        toPassword = edTopassword.getText().toString().trim();

        email = edEmail.getText().toString().trim();

        city = edCity.getText().toString().trim();

        //工作单位
        work = edWork.getText().toString().trim();

        //推荐码
        recommendCode = edRecommendcode.getText().toString().trim();

        //提交用户注册信息
        Map<String, Object> map = new HashMap<>();


        //身份证
        if (!TextUtils.isEmpty(idImageBase64)) {
            map.put("shenfenzhengimg_filecontent", idImageBase64);
            map.put("shenfenzhengimg_fileext", "jpg");
        }

        //大头照


        //竖照
        if (!TextUtils.isEmpty(verImageBase64)) {
            map.put("userimage_ver_filecontent", verImageBase64);
            map.put("userimage_ver_fileext", "jpg");
        }

        //横照


        //判断是否是更新个人信息
        if (!TextUtils.isEmpty(title)) {  //更新
            //用户id
            userId = AppSharePrefManager.getInstance(this).getLastest_login_id();
            map.put(Constants.brokerUserId, userId);

            submitDataUrl = Constants.updateUrl;

        } else {  //注册

            if (TextUtils.isEmpty(brokerName)) {
                Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(id)) {
                Toast.makeText(this, "身份证不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(id)) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(toPassword)) {
                DialogUtil.showDealFailed(this, "设置密码与确认密码不一致", null);
                return;
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Email不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "城市不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            map.put(Constants.brokerphone, phString);//电话
            map.put("authentype", "身份证");
            submitDataUrl = Constants.registerUrl;
        }

        //存入BrokerInfo
        saveBrokerInfo();


        map.put(Constants.brokername, brokerName);
        map.put("shenfenzhengno", id);
        map.put("password", password);
        map.put("email", email);
        map.put("placedesc", city);
        map.put("companyname", work);

        if (!HttpUtil.isNetworkAble(this)) {
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        //展示注册中的dialog
        dialog_wait = DialogUtil.showWait(this);

        new HttpAsy(new PostExecute() {


            @Override
            public void onPostExecute(String result) {

                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(RegistTwoActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                    if (null != dialog_wait && dialog_wait.isShowing()) {
                        dialog_wait.dismiss();
                    }
                    return;
                }

                Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                Log.e("RegisTwoActivity", result + "");
                boolean status = (boolean) resMap.get("status");

                if (TextUtils.isEmpty(title)) {//注册
                    userId = Integer.parseInt(String.valueOf(resMap.get("userid")));
                    brokerInfo.setIdd(userId);
                } else {
                    //把用户信息写入本地
                    AppUtil.setLocalInfo(RegistTwoActivity.this, brokerInfo);
                }


                /**单独存图片*/

                //身份证图片64编码String
                if (!TextUtils.isEmpty(idImageBase64)) {
                    AppSharePrefManager.getInstance(RegistTwoActivity.this).setLastest_shenfenzheng_imagebase64(idImageBase64);
                }
                //竖照64String
                if (!TextUtils.isEmpty(verImageBase64)) {
                    AppSharePrefManager.getInstance(RegistTwoActivity.this).setLastest_shuzhao_imagebase64(verImageBase64);
                }


                if (status) {
                    //设置注册时间
                    AppSharePrefManager.getInstance(RegistTwoActivity.this).setLastest_regist_time();
                    //新建用户成功
                    finish();
                    Intent i = new Intent(RegistTwoActivity.this, RegistSuccessActivity.class);
                    if (!TextUtils.isEmpty(title)) {//重新审核
                        i.putExtra("title", "anew");
                    }
                    startActivity(i);

                } else {
                    //已有该用户
                    Toast.makeText(RegistTwoActivity.this, "用户已存在，请直接登陆", Toast.LENGTH_SHORT).show();
                }
                //聊天账号注册
                new RegOpenfire().execute(Integer.toString(userId));

            }
        }).execute(submitDataUrl, map);
    }


    /**
     * 存入BrokerInfo
     */
    private void saveBrokerInfo() {


        if (!TextUtils.isEmpty(phString)) {
            brokerInfo.setCellphone(phString);
        }

        if (!TextUtils.isEmpty(brokerName)) {
            brokerInfo.setName(brokerName);
        }

        if (!TextUtils.isEmpty(id)) {
            brokerInfo.setShenfenzhengno(id);
        }

        if (!TextUtils.isEmpty(email)) {
            brokerInfo.setEmail(email);
        }

        if (!TextUtils.isEmpty(city)) {
            brokerInfo.setPlacedesc(city);
        }

        if (!TextUtils.isEmpty(work)) {
            brokerInfo.setCompanyname(work);
        }
    }


    private class RegOpenfire extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            int reg = XmppUtil.getInstance().regist(params[0], params[0]);
            if (reg == 1 || reg == 2) {
                Log.e("czhongzhi", "openfire 注册成功或已存在");
                return true;
            } else {
                Log.e("czhongzhi", "openfire 注册失败");
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Log.e("RegisTwoActivity", "openfire 注册成功或已存在");
            } else {
                Log.e("RegisTwoActivity", "openfire 注册失败");
            }
            if (null != dialog_wait && dialog_wait.isShowing()) {
                dialog_wait.dismiss();
            }
        }
    }


    private Uri PhotoUri;
    //private String uploadFile = "";  //图像剪切保存路径

    private static final int PIC_FROM_CAMERA = Menu.FIRST << 0;//拍照
    private static final int PIC_FROM_LOCALPHOTO = Menu.FIRST << 1;//本地


    private int actionType;//动作类型

    private static final int IDIMAGEREQUESTCODE = 1 << 0; //身份证照片
    private static final int VERiMAGEREQUESTCODE = 1 << 1;//竖照

    private int picType;//照片类型 :身份证 , 竖照


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        switch (v.getId()) {

            case R.id.fl_idimagebtu:

                picType = IDIMAGEREQUESTCODE;
                //添加身份证照片
                menu.add(picType, PIC_FROM_CAMERA, 0, "身份证-拍照");
                menu.add(picType, PIC_FROM_LOCALPHOTO, 0, "身份证-本地");
                break;
            case R.id.fl_addphoto:

                picType = VERiMAGEREQUESTCODE;
                //添加竖照
                menu.add(picType, PIC_FROM_CAMERA, 0, "竖照-拍照");
                menu.add(picType, PIC_FROM_LOCALPHOTO, 0, "竖照-本地");
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

            case IDIMAGEREQUESTCODE: {
                intent.putExtra("aspectX", 143);
                intent.putExtra("aspectY", 100);
                intent.putExtra("outputX", 686);
                intent.putExtra("outputY", 480);
                break;
            }
            case VERiMAGEREQUESTCODE: {

                intent.putExtra("aspectX", 2);
                intent.putExtra("aspectY", 3);
                intent.putExtra("outputX", 640);
                intent.putExtra("outputY", 960);
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

                            case IDIMAGEREQUESTCODE:

                                iv_addpic.setVisibility(View.GONE);

                                ivIdimage.setImageBitmap(bitmap);

                                //身份证加密后字符串
                                idImageBase64 = BitmapUtil.bitmapToBase64(bitmap);
                                break;
                            case VERiMAGEREQUESTCODE:  //竖照显示

                                iv_photodelete.setVisibility(View.VISIBLE);
                                iv_addphoto.setVisibility(View.GONE);

                                ivVerticalphoto.setImageBitmap(bitmap);

                                //竖图片加密后字符串
                                verImageBase64 = BitmapUtil.bitmapToBase64(bitmap);
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

    @Override
    protected void onDestroy() {

        if (dialog_wait != null) {
            dialog_wait.dismiss();
        }
        super.onDestroy();

    }
}