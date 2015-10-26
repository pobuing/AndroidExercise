package com.probuing.loadbigimage;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 按钮点击事件
     * TODO 点击显示大图片
     *
     * @param v
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void click(View v) {
        img = ((ImageView) findViewById(R.id.img_iv));
        //只解析图片的宽高 Option类的常量设置
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        /**
         * 当设置options的常量inJustDecodeBounds为true的时候，解析器返回的Bitmap对象为空
         * 只会解析到图片的信息
         */
        BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/dog.jpg",op);
        //TODO 获取图片的宽高
        int imageWidth = op.outWidth;
        int imageHeight = op.outHeight;
        //TODO 获取屏幕的宽高
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth;
        int screenHight;
        //判断SDK版本，使用不同的api
        if(Build.VERSION.SDK_INT>8)
        {
            Point point = new Point();
            display.getSize(point);
            screenHight = point.y;
            screenWidth = point.x;
        }else{
            screenWidth = display.getWidth();
            screenHight = display.getHeight();
        }
        //设置缩放比例
        int scale = 1;
        //判断缩放比例，取大的
        int scaleWidth = imageWidth/screenWidth;
        int scaleHeight = imageHeight/screenHight;
        if(scaleWidth>=scaleHeight&&scaleWidth>=1){
            scale = scaleWidth;
        }else if(scaleWidth<scaleHeight&&scaleHeight>=1)
        {
            scale = scaleHeight;
        }
        //设置图片设置参数的缩放比例
        op.inSampleSize = scale;
        //关闭只解析宽高
        op.inJustDecodeBounds = false;
        //解析图片
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/dog.jpg", op);
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        } else {
            Toast.makeText(MainActivity.this, "图片不存在", Toast.LENGTH_SHORT).show();
        }
    }
}
