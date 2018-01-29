package com.example.mama.zxing60;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0xf1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Button btn;
    private ImageView iv_scanner,iv_scanner2,iv_scanner3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZXingLibrary.initDisplayOpinion(this);//初始化
        initView();
    }

    private void initView() {
        iv_scanner = (ImageView) findViewById(R.id.iv_scanner);
        iv_scanner2 = (ImageView) findViewById(R.id.iv_scanner2);
        iv_scanner3 = (ImageView) findViewById(R.id.iv_scanner3);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ){
                    requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
                    return;
                }
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        iv_scanner.setImageBitmap(QRCode.createQRCodeWithLogo5("http://www.baidu.com",500,drawableToBitmap(getResources().getDrawable(R.mipmap.ic_launcher))));
        iv_scanner2.setImageBitmap(QRCode.createQRCodeWithLogo6("http://www.baidu.com",500,drawableToBitmap(getResources().getDrawable(R.mipmap.ic_launcher))));
        iv_scanner3.setImageBitmap(QRCode.createQRCode("http://www.baidu.com",500));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==CAMERA_REQUEST_CODE){
            if(grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            } else {
                Toast.makeText(this, "权限已拒绝，请在权限设置中开启", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
