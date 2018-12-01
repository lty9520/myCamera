package com.example.administrator.mycamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class ManageActivity extends Activity {

    private Button btn_backToMain;
    private Button btn_backToPhoto;


    /*
     *  一组界面样式，分别是照片在TableRow中所占的宽度比重和照片上传状态的文字信息在TableRow中所占的宽度比重
     */
    private TableRow.LayoutParams photoParams;
    private TableRow.LayoutParams uploadStateMsgParam;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        initButton();
        this.initLayoutParams();

    }

    private void initButton() {

        btn_backToMain = findViewById(R.id.btn_backToMain);
        btn_backToPhoto = findViewById(R.id.btn_backToPhoto);
        MainOnClickListener mainOnClickListener = new MainOnClickListener();
        btn_backToMain.setOnClickListener(mainOnClickListener);
        btn_backToPhoto.setOnClickListener(mainOnClickListener);

    }

    /*
     * 创建按钮点击监听器类
     * */
    private class MainOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_backToPhoto:
                    Intent intent_btp = new Intent(ManageActivity.this, CameraActivity.class);
                    intent_btp.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_btp);
                case R.id.btn_backToMain:
                    Intent intent_btm = new Intent(ManageActivity.this, MainActivity.class);
                    intent_btm.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_btm);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 将拍照和裁剪后所得到的照片，罗列在界面上
     */
    public void addPhotoToActivity(Bitmap bitMap){
        /*
         *  首先获取到用来显示照片的容器
         *  该容器是一个TableLayout
         */
        TableLayout tableLayout = (TableLayout)this.findViewById(R.id.TABLE_LAYOUT_TAKE_PHOTO_LIST);

        /*
         *  创建一个TableRow对象
         *  每一行TableRow对象都用来存放一张照片，以及该照片的上传情况信息
         *  将这个TableRow放入TableLayout中
         */
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(8,8,8,8);//设置每一行的下间距
        tableLayout.addView(tableRow);

        /*
         *  创建一个ImageView对象
         *  将这个对象放入TableRow中
         *  并在这个对象上显示刚刚拍照所得到的照片
         */
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(this.photoParams);
        imageView.setImageBitmap(bitMap);
        tableRow.addView(imageView);

        /*
         *  创建一个TextView对象
         *  为这个对象设置一段“图片正在上传”的提示文字
         *  并将这个TextView对象放入TableRow中
         */
        //TextView textView = new TextView(this);
        //textView.setLayoutParams(this.uploadStateMsgParam);
        //textView.setGravity(Gravity.CENTER_VERTICAL);
        //textView.setText("正在上传照片...");
        //textView.setTextColor(ContextCompat.getColor(this, R.color.blue));
        //tableRow.addView(textView);

    }


    /**
     * 初始化一些界面组件的样式
     */
    private void initLayoutParams(){
        /*
         *  拍照所得到的图片被放置在界面上时，其在TableRow所占的宽度占比
         */
        this.photoParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                268,
                0.1f
        );

        /*
         *  照片上传状态的文字信息被放置在界面上时，其在TableRow所占的宽度占比
         */
        this.uploadStateMsgParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                268,
                0.9f
        );
    }

}
