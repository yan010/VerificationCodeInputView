package com.yanqi.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yanqi.verificationcodeinputview.VerificationInputCodeView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private VerificationInputCodeView mInputCodeView;
    private TextView mTextView;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputCodeView = findViewById(R.id.code);
        mTextView = findViewById(R.id.text);
        mResultTextView = findViewById(R.id.text_result);
        mInputCodeView.setmOnKeyEvent(new VerificationInputCodeView.OnKeyEvent() {
            @Override
            public void onCodeChange(int position, String content) {
                mResultTextView.setText("position:" + position + "content:" + content);
            }

            @Override
            public void onFinishCode(List<String> codeData) {
                String result = "";
                for (String temp : codeData) {
                    result = result + temp;
                }
                mResultTextView.setText("完成输入：" + result);

            }
        });
    }
}
