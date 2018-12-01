package com.example.administrator.mycamera;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {



    private Button btn_managePrj;
    private Button btn_newPrj;
    private Button btn_exit;


    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }


    private class MainOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_managePrj:
                    Intent intent_mana = new Intent(MainActivity.this, ManageActivity.class);
                    intent_mana.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_mana);
                case R.id.btn_newPrj:
                    Intent intent_new = new Intent(MainActivity.this, CameraActivity.class);
                    intent_new.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent_new);
                    break;
                case R.id.btn_exit:
                    exit();
                    break;
                default:
                    break;
            }
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        btn_managePrj = findViewById(R.id.btn_managePrj);
        btn_newPrj = findViewById(R.id.btn_newPrj);
        btn_exit = findViewById(R.id.btn_exit);
        MainOnClickListener mainOnClickListener = new MainOnClickListener();
        btn_managePrj.setOnClickListener(mainOnClickListener);
        btn_newPrj.setOnClickListener(mainOnClickListener);
        btn_exit.setOnClickListener(mainOnClickListener);

    }


}
