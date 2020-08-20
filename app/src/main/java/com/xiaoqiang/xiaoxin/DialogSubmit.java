package com.xiaoqiang.xiaoxin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data: on 2019/11/29 14:38
 */
public class DialogSubmit extends Dialog {
    private TextView mTextViewContent,
            mTextViewBtn, mTextViewOver;
    private String content;
    private Callback mCallback;

    public DialogSubmit(Context context, String content, Callback callback) {
        super(context, R.style.app_dialog_login_overdue);
        this.content = content;
        mCallback = callback;
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window != null && window.getAttributes() != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(attributes);
            window.setWindowAnimations(R.style.app_dialog_login_overdue);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_dialog_submit);
        initView();
        setListener();
    }

    private void initView() {
        mTextViewContent = findViewById(R.id.app_dialog_remind_tvcontent);
        mTextViewBtn = findViewById(R.id.app_dialog_remind_tvqueding);
        mTextViewOver = findViewById(R.id.app_dialog_remind_tvover);
        mTextViewContent.setText(content);
    }

    private void setListener() {
        mTextViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if (mCallback != null) {
                    mCallback.onClickOk();
                }
            }
        });
        mTextViewOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if (mCallback != null) {
                    mCallback.onOver();
                }
            }
        });
    }

    public interface Callback {
        /**
         * 点击 确定按钮
         */
        void onClickOk();

        /**
         * 点击取消
         */
        void onOver();
    }


}
