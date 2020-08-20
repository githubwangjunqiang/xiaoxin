package com.xiaoqiang.xiaoxin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.SeekBar;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {


    private ArcheryView mArcheryView, mArcheryView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mArcheryView = findViewById(R.id.arcview);
        mArcheryView2 = findViewById(R.id.arcview2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getBitmap(R.mipmap.boduoyejieyi, mArcheryView);
                getBitmap(R.mipmap.cangjingkong, mArcheryView2);

            }
        }).start();

        mArcheryView.setOnArcheryListener(new ArcheryView.OnArcheryListener() {
            @Override
            public void onReset() {

            }

            @Override
            public void onArcheryComplete(boolean hit) {
                Toast.makeText(GameActivity.this, hit ? "命中了目标" : "射箭失败", Toast.LENGTH_SHORT).show();
            }
        });
        mArcheryView2.setOnArcheryListener(new ArcheryView.OnArcheryListener() {
            @Override
            public void onReset() {

            }

            @Override
            public void onArcheryComplete(boolean hit) {
                Toast.makeText(GameActivity.this, hit ? "命中了目标" : "射箭失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getBitmap(int bitmapId, ArcheryView archeryView) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), bitmapId, options);
            int outWidth = options.outWidth;
            int outHeight = options.outHeight;

            float v = outWidth / dpTopx(100);
            float v1 = outHeight / dpTopx(100);
            v = Math.max(v, v1);
            options.inJustDecodeBounds = false;
            options.inSampleSize = (int) v;
            final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapId, options);
            if (isDestroyed() || isFinishing() || mArcheryView == null) {
                return;
            }
            archeryView.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dp px 转换
     *
     * @param size
     * @return
     */
    private float dpTopx(int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }
}