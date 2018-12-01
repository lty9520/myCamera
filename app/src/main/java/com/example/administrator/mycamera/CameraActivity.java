package com.example.administrator.mycamera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends Activity {

    private Button btn_photo;
    private Button btn_back;

    private final int TAKE_PHOTO = 1;//拍照操作
    private final int CROP_PHOTO = 2;//切图操作


    /*
     *  拍照所得到的图像的保存路径
     */
    private Uri imageUri;

    /*
     *  当前用户拍照或者从相册选择的照片的文件名
     */
    private String fileName;


    private class MainOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_photo:
                    applyWritePermission();
                    break;
                case R.id.btn_back:
                    Intent intent_new = new Intent(CameraActivity.this, MainActivity.class);
                    intent_new.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_new);
                    break;
                default:
                    break;
            }
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        btn_photo = findViewById(R.id.btn_photo);
        btn_back = findViewById(R.id.btn_back);
        MainOnClickListener mainOnClickListener = new MainOnClickListener();
        btn_photo.setOnClickListener(mainOnClickListener);
        btn_back.setOnClickListener(mainOnClickListener);

    }

    public void applyWritePermission() {

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                takePhoto();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.CAMERA
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 1);
            }
        } else {
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 当用户点击按钮时，打开摄像头进行拍照
     */
    public void takePhoto(){
        /*
         *  用时间戳的方式来命名图片文件，这样可以避免文件名称重复
         */
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        this.fileName = "test";

        /*
         *  创建一个File对象，用于存放拍照所得到的照片
         */
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path + File.separator + format.format(date) + File.separator,this.fileName+".jpg");

        /*
         *  以防万一，看一下这个文件是不是存在，如果存在的话，先删除掉
         */
        if(outputImage.exists()){
            outputImage.delete();
        }

        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         *  将File对象转换为Uri对象，以便拍照后保存
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            imageUri = FileProvider.getUriForFile(CameraActivity.this,"com.example.administrator.mycamera.fileprovider",outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }

        /*
         *  启动系统的照相Intent
         */
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //Android系统自带的照相intent
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri); //指定图片输出地址
        startActivityForResult(intent,this.TAKE_PHOTO); //以forResult模式启动照相intent
    }


    /**
     * 因为启动拍照intent的时候是使用的forResult模式，因此需要onActivityResult方法来接受回调参数
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "获取图片出现错误", Toast.LENGTH_SHORT).show();
        }
        else{
            switch(requestCode) {
                /*
                 *  case TAKE_PHOTO 代表从拍摄照片的intent返回之后
                 *  完成拍摄照片之后，立刻打开系统自带的裁剪图片的intent
                 */
                case TAKE_PHOTO:
                    this.cropPhoto(this.imageUri);
                    break;

                /*
                 *  case CROP_PHOTO 代表从裁剪照片的intent返回之后
                 *  完成裁剪照片后，就要将图片生成bitmap对象，然后显示在界面上面了
                 */
                case CROP_PHOTO:
                    try {
                        /*
                         *  将图片转换成Bitmap对象
                         */
                        Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(this.imageUri));
                        Toast.makeText(this, this.imageUri.toString(), Toast.LENGTH_SHORT).show();


                    } catch(FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;


                default:
                    break;
            }
        }
    }



    /**
     * 打开裁剪图片的系统界面
     */
    private void cropPhoto(Uri imageUri){
        /*
         *  准备打开系统自带的裁剪图片的intent
         */
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("scale", true);

        /*
         *  设置裁剪区域的宽高比例
         */
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        /*
         *  设置裁剪区域的宽度和高度
         */
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);

        /*
         *  指定裁剪完成以后的图片所保存的位置
         */
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri);
        Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();

        /*
         *  以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
         */
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(this.imageUri);
        this.sendBroadcast(intentBc);

        /*
         *  以forResult模式启动系统自带的裁剪图片的intent
         */
        startActivityForResult(intent, CROP_PHOTO); //设置裁剪参数显示图片至ImageView
    }


}
