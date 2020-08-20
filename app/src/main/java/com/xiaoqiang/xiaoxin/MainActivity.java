package com.xiaoqiang.xiaoxin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //    public static final String TestHttpUrl = "http://quzan.qushiwan.cn/quzan/setting/restartAudit";
//    public static final String HttpUrl = "https://app.quzanplay.com/quzan/setting/restartAudit";
    public static final String TestHttpUrl = "http://quzan.qushiwan.cn/quzan/version/getAndroidVersionInfo";
    public static final String HttpUrl = "https://app.quzanplay.com/quzan/version/getAndroidVersionInfo";
    private AsyncTask<String, Void, String> execute;
    private DialogRemind mDialogRemind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    /**
     * 测试版
     *
     * @param view
     */
    @SuppressLint("StaticFieldLeak")
    public void doClickTest(View view) {

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

    /**
     * 正式版
     *
     * @param view
     */
    @SuppressLint("StaticFieldLeak")
    public void doClickFormal(View view) {
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

    public void doClickGame(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }
}