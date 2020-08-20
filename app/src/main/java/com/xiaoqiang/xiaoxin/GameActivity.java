package com.xiaoqiang.xiaoxin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static com.xiaoqiang.xiaoxin.MainActivity.HttpUrl;
import static com.xiaoqiang.xiaoxin.MainActivity.TestHttpUrl;

public class GameActivity extends AppCompatActivity {


    private ArcheryView mArcheryView, mArcheryView2;
    private AsyncTask<String, Void, String> execute;
    private DialogRemind mDialogRemind;

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
                showDialogSubmit(hit);
            }


        });
        mArcheryView2.setOnArcheryListener(new ArcheryView.OnArcheryListener() {
            @Override
            public void onReset() {

            }

            @Override
            public void onArcheryComplete(boolean hit) {
                showDialogSubmit2(hit);
            }
        });

    }

    @Override
    protected void onDestroy() {
        clearTask();
        super.onDestroy();
    }

    public void clearTask() {
        if (execute != null) {
            if (!execute.isCancelled()) {
                execute.cancel(true);
            }
            execute = null;
        }
    }

    private void showDialogSubmit2(boolean hit) {
        if (!hit) {
            Snackbar.make(mArcheryView, "体力不行，肾虚!!!", Snackbar.LENGTH_LONG).show();
            return;
        }

        new DialogSubmit(this, "您确定要“干”干苍井空吗？", new DialogSubmit.Callback() {
            @Override
            public void onClickOk() {
                clearTask();
                execute = new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        Data data = HttpUtils.get(strings[0]);
                        return data.toString();
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        showLoadDialog();
                    }

                    @Override
                    protected void onPostExecute(String data) {
                        super.onPostExecute(data);
                        mDialogRemind.setData(data);

                    }
                }.execute(HttpUrl);
            }

            @Override
            public void onOver() {

            }
        }).show();
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

    private void showDialogSubmit(boolean hit) {
        if (!hit) {
            Snackbar.make(mArcheryView, "体力不行，虚!!!", Snackbar.LENGTH_LONG).show();
            return;
        }

        new DialogSubmit(this, "您确定要“抚摸”波多野结衣吗？", new DialogSubmit.Callback() {
            @Override
            public void onClickOk() {
                clearTask();
                execute = new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        Data data = HttpUtils.get(strings[0]);
                        return data.toString();
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        showLoadDialog();
                    }

                    @Override
                    protected void onPostExecute(String data) {
                        super.onPostExecute(data);
                        mDialogRemind.setData(data);

                    }
                }.execute(TestHttpUrl);
            }

            @Override
            public void onOver() {

            }
        }).show();
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

    private void showLoadDialog() {
        canleDialog();
        mDialogRemind = new DialogRemind(this);
        mDialogRemind.show();
    }

    private void canleDialog() {
        if (mDialogRemind != null) {
            mDialogRemind.cancel();
            mDialogRemind = null;
        }
    }
}