package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.manager.AppSharePrefManager;
import com.inetgoes.fangdd.model.UserInfo;
import com.inetgoes.fangdd.util.BitmapUtil;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.view.CustomTitleBar;
import com.inetgoes.fangdd.view.MyCircleImageView;
import com.inetgoes.fangdd.view.SlideSwitch;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetUserInfoActivity extends Activity {
    public static final String SET_NAME = "set_name";
    public static final String SET_EMAIL = "set_email";
    public static final String SET_PHONE = "set_phone";
    private UserInfo userInfo;

    private Activity activity;
    private View item_layout;
    private MyCircleImageView set_icon;
    private RelativeLayout linearLayout;
    TextView tvName, tvPhoen, tvEmail, tvAuth;
    private String hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar(this, "个人中心");
        setContentView(R.layout.activity_set_user_info);
        activity = SetUserInfoActivity.this;


        initView();
        this.registerForContextMenu(linearLayout);

        if (TextUtils.isEmpty(AppSharePrefManager.getInstance(this).getLastest_login_username())) {
            //网络获取用户信息
            getUserInfo();
        } else {
            setDataShow();
        }

    }

    public static void getTitleBar(final Activity activity, String title) {
        activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        activity.setContentView(R.layout.layout_titlebar);
        activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar);

        ImageView bar_back = (ImageView) activity.findViewById(R.id.bar_back);
        bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }
        });

        activity.findViewById(R.id.bar_right).setVisibility(View.INVISIBLE);

        ((TextView) activity.findViewById(R.id.bar_title)).setText(title);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (TextUtils.isEmpty(AppSharePrefManager.getInstance(this).getLastest_login_username())) {
            //网络获取用户信息
            getUserInfo();
        } else {
            setDataShow();
        }
    }

    public void getUserInfo() {
        int userid = AppSharePrefManager.getInstance(this).getLastest_login_id();
        if (userid == 0)
            return;
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(SetUserInfoActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        userInfo = JacksonMapper.getObjectMapper().readValue(result, UserInfo.class);
                        SetUserInfoActivity.setLocalInfo(SetUserInfoActivity.this, userInfo);
                        setDataShow();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).execute(Constants.getUserInfoUrl + "?userid=" + userid);
    }

    /**
     * 保存信息到本地用户中
     */
    public static void setLocalInfo(Context context, UserInfo userInfo) {
        AppSharePrefManager sharePrefManager = AppSharePrefManager.getInstance(context);
        sharePrefManager.setLastest_people_desc(userInfo.getPersonaldesc());
        sharePrefManager.setLastest_login_username(userInfo.getName());//
        sharePrefManager.setLastest_login_touxiang_imageurl(userInfo.getUserimage());
        sharePrefManager.setLastest_sex(userInfo.getSex());
        sharePrefManager.setLastest_address(userInfo.getPlacedesc());
        sharePrefManager.setLastest_qq(userInfo.getQq());
        sharePrefManager.setLastest_email(userInfo.getEmail());//
        sharePrefManager.setUser_register_time(userInfo.getRegisterdate());
        sharePrefManager.setLastest_login_phone_num(userInfo.getCellphone());//
    }

    private void setDataShow() {
        AppSharePrefManager sManager = AppSharePrefManager.getInstance(this);
        tvName.setText(sManager.getLastest_login_username());
        tvPhoen.setText(sManager.getLastest_login_phone_num());
        tvEmail.setText(sManager.getLastest_email());
    }

    private void initView() {
        item_layout = findViewById(R.id.set_layout_icon);//头像
        if (item_layout != null) {
            set_icon = (MyCircleImageView) findViewById(R.id.set_icon);
            String iconString = AppSharePrefManager.getInstance(this).getLastest_login_touxiang_imagebase64();
            if (!TextUtils.isEmpty(iconString)) {
                set_icon.setImageBitmap(BitmapUtil.base64ToBitmap(iconString));
            }
            linearLayout = (RelativeLayout) findViewById(R.id.set_layout_icon);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更换头像
                    openContextMenu(linearLayout);
                }
            });
        }

        item_layout = findViewById(R.id.set_layout_name);//用户名
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("用户名");
            tvName = (TextView) item_layout.findViewById(R.id.set_hitm);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hint = tvName.getText().toString().trim();
                    Intent intent = new Intent(activity, SetUserInfo2Activity.class);
                    intent.putExtra(SetUserInfo2Activity.TITLE_BAR, SET_NAME);
                    intent.putExtra(SetUserInfo2Activity.EditHint, hint.equals("未填写") ? "" : hint);
                    startActivity(intent);
                }
            });
        }

        item_layout = findViewById(R.id.set_layout_phone);//手机号
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("手机号");
            tvPhoen = (TextView) item_layout.findViewById(R.id.set_hitm);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    hint = tvPhoen.getText().toString().trim();
//                    Intent intent = new Intent(activity,SetUserInfo2Activity.class);
//                    intent.putExtra(SetUserInfo2Activity.TITLE_BAR,SET_PHONE);
//                    intent.putExtra(SetUserInfo2Activity.EditHint, hint.equals("未填写") ? "" : hint);
//                    startActivity(intent);
                }
            });
        }

        item_layout = findViewById(R.id.set_layout_mailbox);//电子邮箱
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("电子邮箱");
            tvEmail = (TextView) item_layout.findViewById(R.id.set_hitm);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hint = tvEmail.getText().toString().trim();
                    Intent intent = new Intent(activity, SetUserInfo2Activity.class);
                    intent.putExtra(SetUserInfo2Activity.TITLE_BAR, SET_EMAIL);
                    intent.putExtra(SetUserInfo2Activity.EditHint, hint.equals("未填写") ? "" : hint);
                    startActivity(intent);
                }
            });
        }

        item_layout = findViewById(R.id.set_layout_auth);//经纪人认证
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("房产专家认证");
            ((TextView) item_layout.findViewById(R.id.set_hitm)).setTextColor(getResources().getColor(R.color.divider_font_red));
            tvAuth = (TextView) item_layout.findViewById(R.id.set_hitm);
            ((TextView) item_layout.findViewById(R.id.set_hitm)).setText("未认证");
        }

        item_layout = findViewById(R.id.set_layout_phonec);//修改绑定手机号
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("修改绑定手机号");
            item_layout.findViewById(R.id.set_hitm).setVisibility(View.INVISIBLE);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }
    }

    private static final int ITEM1 = Menu.FIRST;
    private static final int ITEM2 = Menu.FIRST + 1;

    //拍照获取头像
    private static final int PIC_FROM_CAMERA = 1;
    //本地获取
    private static final int PIC_FROM_LOCALPHOTO = 0;

    private Uri PhotoUri;
    private String newName = "icon.png";
    private String uploadFile = "";  //图像剪切保存路径

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, ITEM1, 0, "拍照");
        menu.add(0, ITEM2, 0, "本地");
    }

    //菜单单击响应
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //点击了拍照
            case ITEM1:
                doHandlerPhoto(PIC_FROM_CAMERA);
                break;
            //点击了本地
            case ITEM2:
                doHandlerPhoto(PIC_FROM_LOCALPHOTO);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 根据不同方式选择图片设置ImageView
     *
     * @param type 0-本地相册选择，非0为拍照
     */
    private void doHandlerPhoto(int type) {
        try {
            //保存剪裁后的图片文件
            File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/upload");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            File picFile = new File(pictureFileDir, "icon.png");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }

            PhotoUri = Uri.fromFile(picFile);
            uploadFile = picFile.getAbsolutePath();
            Log.w("MyLog", "选择类型：" + type);
            if (type == PIC_FROM_LOCALPHOTO) {
                //类型本地
                Intent intent = getCropImageIntent();
                startActivityForResult(intent, PIC_FROM_LOCALPHOTO);
            } else {
                //类型拍照
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUri);
                startActivityForResult(cameraIntent, PIC_FROM_CAMERA);

            }
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
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

    private void cropImageUriByTakePhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(PhotoUri, "image/*");
        setIntentParams(intent);
        startActivityForResult(intent, PIC_FROM_LOCALPHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //判断取消的情况
        if (resultCode == RESULT_CANCELED)
            return;
        Bitmap bitmap = null;
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
                        bitmap = decodeUriAsBitmap(PhotoUri);
                        set_icon.setImageBitmap(bitmap);
                        String filecontent = BitmapUtil.bitmapToBase64(bitmap);
                        setUserIcon(filecontent);
                        AppSharePrefManager.getInstance(SetUserInfoActivity.this).setLastest_login_touxiang_imagebase64(filecontent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 更新用户头像
     */
    private void setUserIcon(String filecontent) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", AppSharePrefManager.getInstance(this).getLastest_login_id());
        map.put("fileext", "jpg");
        map.put("filecontent", filecontent);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                if (state.equals("true")) {//修改成功
                    Toast.makeText(SetUserInfoActivity.this, "头像修改成功", Toast.LENGTH_SHORT).show();
                } else {//修改失败
                    Toast.makeText(SetUserInfoActivity.this, "头像修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(Constants.setUserInfoUrl, map);
    }

}
