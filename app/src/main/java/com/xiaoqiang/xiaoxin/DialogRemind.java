package com.xiaoqiang.xiaoxin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
public class DialogRemind extends Dialog {
    private TextView mTextViewContent,
            mTextViewBtn;


    public DialogRemind(Context context) {
        super(context, R.style.app_dialog_login_overdue);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
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
        setContentView(R.layout.app_dialog_remind);
        initView();
        setListener();
    }

    private void initView() {
        mTextViewContent = findViewById(R.id.app_dialog_remind_tvcontent);
        mTextViewContent.setBackgroundColor(Color.parseColor("#00ffffff"));
        mTextViewContent.setText("");
        mTextViewBtn = findViewById(R.id.app_dialog_remind_tvqueding);
        mTextViewBtn.setText("加载中...");
    }

    private void setListener() {
        mTextViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "请等待...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setData(String data) {
        try {
            mTextViewContent.setBackgroundColor(Color.parseColor("#ffffff"));
            mTextViewContent.setText(data);
            mTextViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                }
            });
            mTextViewBtn.setText("关闭");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            cancel();
        }
    }

}
