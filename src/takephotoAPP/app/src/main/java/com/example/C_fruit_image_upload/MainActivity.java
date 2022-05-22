package com.example.C_fruit_image_upload;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements C_PhotoFragment.OnFragmentInteractionListener {

    int PERMISSION_ALL = 1;
    boolean flagPermissions = false;

    private WebView webView;
    private String accessUrl = "http://www.eng.nagasaki-u.ac.jp/it_recurrent/";

    public global_variable gv;
    public SettingActivity setact;

    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    TextView title ;

    LinearLayout main_Layout;
    LinearLayout Photo_Layout;
    LinearLayout webView_Layout;

    Button P;
    Button C;
    ImageButton S;
    Button B;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermissions();

        Context context = getApplicationContext();

        gv = new global_variable();
        gv.url = "0";

        //ipアドレス設定
        setact = new SettingActivity();
        String str = setact.readFile(context);
        if (str != null) {
            gv.ip_address = str;
        } else {
            gv.ip_address = "192.168.0.166";
        }

        main_Layout = findViewById(R.id.main_Layout);
        webView_Layout = findViewById(R.id.webView_Layout);

        main_Layout.setVisibility(View.VISIBLE);
        webView_Layout.setVisibility(View.GONE);


        P = findViewById(R.id.P_photo_button);
        C = findViewById(R.id.C_photo_button);
        S = findViewById(R.id.setting_button);
        B = findViewById(R.id.back_btn);


        webView = findViewById(R.id.web_view);
    }


    @OnClick(R.id.P_photo_button)
    void onClickScanButton() {


        P.setVisibility(View.GONE);
        C.setVisibility(View.GONE);
        S.setVisibility(View.GONE);
        // check permissions
        if (!flagPermissions) {
            checkPermissions();
            return;
        }

    }

    @OnClick(R.id.C_photo_button)
    void onClickButton() {

        P.setVisibility(View.GONE);
        C.setVisibility(View.GONE);
        S.setVisibility(View.GONE);

        if (!flagPermissions) {
            checkPermissions();
            return;
        }
        //start text fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.res_photo_layout, new C_PhotoFragment())
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.setting_button)
    void onClickScanSetting_button() {
        Intent intent = new Intent(getApplication(), SettingActivity.class);
        startActivity(intent);

    }
    @OnClick(R.id.back_btn)
    void onClickScanBack_btn() {
        main_Layout.setVisibility(View.VISIBLE);
        webView_Layout.setVisibility(View.GONE);

    }

    void OpenWebview() {

        //String url = gv.getTestString();
        String url= "http://" + gv.ip_address + "/MERON/result/index.html";
        if (url.equals("0")){
            Log.i("url", "初期値のままのため遷移しない。");
        }
        else
        {
            createWebview(url);
        }
    }

    void VisibleButton(){
        //P.setVisibility(View.VISIBLE);
        C.setVisibility(View.VISIBLE);
        S.setVisibility(View.VISIBLE);
        //title.setVisibility(View.VISIBLE);
    }

    void checkPermissions() {
        if (!hasPermissions(this, PERMISSIONS)) {
            requestPermissions(PERMISSIONS,
                    PERMISSION_ALL);
            flagPermissions = false;
        }
        flagPermissions = true;

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onFragmentInteraction(Bitmap bitmap) {
        if (bitmap != null) {
            ImageFragment imageFragment = new ImageFragment();
            imageFragment.imageSetupFragment(bitmap);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.res_photo_layout, imageFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    public void createWebview(String url)
    {
        main_Layout.setVisibility(View.GONE);
        webView_Layout.setVisibility(View.VISIBLE);


        // JavaScriptを有効化(JavaScript インジェクションに注意)
        webView.getSettings().setJavaScriptEnabled(true);

        // Web Storage を有効化
        webView.getSettings().setDomStorageEnabled(true);

        // Hardware Acceleration ON
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        webView.loadUrl(url);



    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();

                    } else {
                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        startActivity(intent);
//                        finish();

                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }




}