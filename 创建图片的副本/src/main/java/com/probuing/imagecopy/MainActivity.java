package com.probuing.imagecopy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView copyImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyImg = ((ImageView) findViewById(R.id.imgCopy));
        Bitmap srcBitMap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, null);
        //创建原图的空的副本,与原图一摸一样，配置、宽高都一样
        Bitmap copyBitMap = Bitmap.createBitmap(srcBitMap.getWidth(), srcBitMap.getHeight(), srcBitMap.getConfig());
        //TODO 将原图中的内容画到新的副本中
        //1. 创建画笔
        Paint paint = new Paint();
        //2.创建画布,传入要画的对象
        Canvas canvas = new Canvas(copyBitMap);
        //3.开始画 原图，矩阵，画笔
        canvas.drawBitmap(srcBitMap,new Matrix(),paint);
        copyImg.setImageBitmap(copyBitMap);
    }
}
