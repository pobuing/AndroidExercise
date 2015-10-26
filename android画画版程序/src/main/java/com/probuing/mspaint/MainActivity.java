package com.probuing.mspaint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView iv;
    private Bitmap copyBitmap;
    private Paint paint;
    private Canvas canvas;
    private int startX;
    private int startY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = ((ImageView) findViewById(R.id.iv));
        //原图
        Bitmap srcbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        //创建原图副本
        copyBitmap = Bitmap.createBitmap(srcbitmap.getWidth(), srcbitmap.getHeight(), srcbitmap.getConfig());
        //创建画笔
        paint = new Paint();
        //创建画布
        canvas = new Canvas(copyBitmap);
        //画图
        canvas.drawBitmap(srcbitmap, new Matrix(), paint);
        //设置图片
        iv.setImageBitmap(copyBitmap);
        //触摸监听
        iv.setOnTouchListener(this);
    }

    /**
     * 按钮点击事件
     */
    public void redBtn(View v) {
        paint.setColor(Color.RED);
    }

    public void brushBtn(View v) {
        //设置画笔宽度
        paint.setStrokeWidth(10);
    }

    public void greenBtn(View v) {
        paint.setColor(Color.GREEN);

    }

    public void blueBtn(View v) {
        paint.setColor(Color.BLUE);

    }

    //保存图片
    public void saveBtn(View v) {
        //路径
        String path = Environment.getExternalStorageDirectory() + "/dazuo.jpg";
        //创建文件类对象
        File file = new File(path);
        //文件输出流
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            //压缩图片
            copyBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            //判断版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                sendBroadcast(intent);

            } else {
                //通知SD卡就绪
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
                sendBroadcast(intent);
            }
            //设置Data
            //发送广播
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //记录开始坐标
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                //画线
                canvas.drawLine(startX, startY, x, y, paint);
                iv.setImageBitmap(copyBitmap);
                //改变起始坐标
                startX = x;
                startY = y;
                break;
        }
        //触摸事件交给imageview处理
        return true;
    }
}
