package com.yanqi.verificationcodeinputview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.TextView;

/**
 * Created by yanqi on 2018/2/13.
 */

public class InputTextView extends TextView {
    private String TAG = "InputTextView";

    public void setmOnKeyEventListener(OnKeyEventListener mOnKeyEventListener) {
        this.mOnKeyEventListener = mOnKeyEventListener;
    }

    private OnKeyEventListener mOnKeyEventListener;

    public InputTextView(Context context) {
        this(context, null);
    }

    public InputTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new ProxyInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    public class ProxyInputConnection extends InputConnectionWrapper {

        /**
         * Initializes a wrapper.
         * <p>
         * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
         * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
         * has {@code null} in {@code target}.</p>
         *
         * @param target  the {@link InputConnection} to be proxied.
         * @param mutable set {@code true} to protect this object from being reconfigured to target
         *                another {@link InputConnection}.  Note that this is ignored while the target is {@code null}.
         */
        public ProxyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    Log.i(TAG, "delete");
                    mOnKeyEventListener.onDel();
                } else {
                    mOnKeyEventListener.onAdd((event.getKeyCode() - 7) + "");
                }
            }
            return false;
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            mOnKeyEventListener.onAdd(text + "");
            return false;
        }
    }

    public interface OnKeyEventListener {
        void onAdd(String content);

        void onDel();
    }

}
