package com.yanqi.verificationcodeinputview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanqi on 2018/2/14.
 */

public class InputCodeView extends LinearLayout implements View.OnClickListener, InputTextView.OnKeyEventListener {
    private Context mContext;
    private int MAX_COUNT = 4;//验证码数量
    private int inputType;

    private int padding = 14;
    private int leftPadding;
    private int rightPadding;
    private int topPadding;
    private int bottomPadding;
    private int margin = 14;
    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;
    private int childWidth = 120;
    private int childHeigh = ViewGroup.LayoutParams.WRAP_CONTENT;
    private boolean average = true;
    private Drawable focusDrawable;
    private Drawable normalDrawable;
    private int textSize = 20;
    private ColorStateList textColor;

    private OnKeyEvent mOnKeyEvent;

    public void setmOnKeyEvent(OnKeyEvent mOnKeyEvent) {
        this.mOnKeyEvent = mOnKeyEvent;
    }

    private int count = 0;

    private List<InputTextView> inputTextViews = new ArrayList<>();

    private List<String> codeData = new ArrayList<>();

    public InputCodeView(Context context) {
        this(context, null);
    }

    public InputCodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.InputCode);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.InputCode_number) {
                MAX_COUNT = a.getInt(attr, 4);

            } else if (attr == R.styleable.InputCode_inputType) {
                inputType = a.getInt(attr, EditorInfo.TYPE_NULL);

            } else if (attr == R.styleable.InputCode_focusDrawable) {
                focusDrawable = a.getDrawable(attr);

            } else if (attr == R.styleable.InputCode_normalDrawable) {
                normalDrawable = a.getDrawable(attr);

            } else if (attr == R.styleable.InputCode_padding) {
                padding = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_paddingLeft) {
                leftPadding = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_paddingRight) {
                rightPadding = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_paddingTop) {
                topPadding = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_paddingBottom) {
                bottomPadding = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_margin) {
                margin = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_marginLeft) {
                leftMargin = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_marginRight) {
                rightMargin = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_marginTop) {
                topMargin = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_marginBottom) {
                bottomMargin = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_textColor) {
                textColor = a.getColorStateList(attr);

            } else if (attr == R.styleable.InputCode_textSize) {
                textSize = a.getDimensionPixelSize(attr, textSize);

            } else if (attr == R.styleable.InputCode_childWidth) {
                childWidth = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_childHeight) {
                childHeigh = a.getDimensionPixelSize(attr, -1);

            } else if (attr == R.styleable.InputCode_average) {
                average = a.getBoolean(attr, true);

            }
        }
        initItemCode();

    }

    private void initItemCode() {
        for (int i = 0; i < MAX_COUNT; i++) {
            final InputTextView inputTextView = new InputTextView(mContext);
            inputTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (padding != 0) {
                inputTextView.setPadding(padding, padding, padding, padding);
            } else {
                inputTextView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
            }
            inputTextView.setTextSize(textSize);
            inputTextView.setTextColor(textColor != null ? textColor : ColorStateList.valueOf(0xFF000000));
            if (focusDrawable != null && normalDrawable != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    inputTextView.setBackground(setSelector(focusDrawable, normalDrawable));
                } else {
                    inputTextView.setBackgroundDrawable(setSelector(focusDrawable, normalDrawable));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    inputTextView.setBackground(setSelector(mContext.getDrawable(R.drawable.focus_bg)
                            , mContext.getDrawable(R.drawable.normal_bg)));
                } else {
                    inputTextView.setBackground(setSelector(mContext.getResources().getDrawable(R.drawable.focus_bg)
                            , mContext.getResources().getDrawable(R.drawable.normal_bg)));
                }
            }
            inputTextView.setGravity(Gravity.CENTER);
            LayoutParams layoutParams;
            layoutParams = new LayoutParams(childWidth, childHeigh, average ? 1 : 0);
            if (margin != 0) {
                layoutParams.setMargins(margin, margin, margin, margin);
            } else {
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
            }
            inputTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            inputTextView.setLayoutParams(layoutParams);
            if (average) {
                inputTextView.setMinWidth(500);//解决输入文字移位
            }
            inputTextView.setmOnKeyEventListener(this);

            final int finalI = i;
            addView(inputTextView, finalI);
            inputTextViews.add(inputTextView);
        }
    }

    /**
     * 构建选择背景Drawable
     *
     * @param normalDrawable 默认样式
     * @param focusDrawable  foucus样式
     * @return
     */
    private StateListDrawable setSelector(Drawable focusDrawable, Drawable normalDrawable) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_focused}, focusDrawable);
        bg.addState(new int[]{}, normalDrawable);
        return bg;
    }

    /**
     * 删除事件
     */
    private void del() {
        if (count > 0) {
            count--;
            inputTextViews.get(count).setText("");
            inputTextViews.get(count).requestFocus();
            if (mOnKeyEvent != null) {
                mOnKeyEvent.onCodeChange(count, "");
            }
        } else {
            count = 0;
        }
    }

    /**
     * 添加事件
     *
     * @param content 添加的内容
     */
    private void add(String content) {
        if (count < MAX_COUNT) {
            inputTextViews.get(count).setText(content);
            if (mOnKeyEvent != null) {
                mOnKeyEvent.onCodeChange(count, content);
            }
            count++;
            if (count == MAX_COUNT) {
                inputTextViews.get(MAX_COUNT - 1).requestFocus();
                if (mOnKeyEvent != null) {
                    mOnKeyEvent.onFinishCode(getCode());
                }
            } else {
                inputTextViews.get(count).requestFocus();
            }
        } else {
            count = MAX_COUNT;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int temp = count;
        if (count == MAX_COUNT) {
            temp = count - 1;
        }
        inputTextViews.get(temp).requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.viewClicked(inputTextViews.get(temp));
        }
        imm.showSoftInput(inputTextViews.get(temp), 0);
        return true;
    }

    @Override
    public void onAdd(String content) {
        add(content);
    }

    @Override
    public void onDel() {
        del();
    }

    /**
     * 获取当前输入的验证码
     *
     * @return 验证码结果，以list形式返回
     */
    public List<String> getCode() {
        codeData.clear();
        for (TextView textView : inputTextViews) {
            codeData.add(textView.getText().toString());
        }
        return codeData;
    }

    /**
     * 获取当前将要输入的位置
     *
     * @return 位置
     */
    public int getPosition() {
        return count;
    }

    public interface OnKeyEvent {
        /**
         * 发生变化的code事件监听
         *
         * @param position
         * @param content
         */
        void onCodeChange(int position, String content);

        /**
         * 完成code事件监听
         *
         * @param codeData
         */
        void onFinishCode(List<String> codeData);

    }
}
