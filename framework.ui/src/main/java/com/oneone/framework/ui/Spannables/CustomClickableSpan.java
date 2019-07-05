package com.oneone.framework.ui.Spannables;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

class CustomClickableSpan extends ClickableSpan {

  private CharSequence text;
  private Object tag;
  private Range range;
  private OnTextClickListener onTextClickListener;
  private OnTextLongClickListener onTextLongClickListener;

  private CustomClickableSpan(CharSequence text, Object tag, Range range) {
    this.text = text;
    this.tag = tag;
    this.range = range;
  }

  CustomClickableSpan(CharSequence text, Object tag, Range range, OnTextClickListener onTextClickListener) {
    this(text, tag, range);
    this.onTextClickListener = onTextClickListener;
  }

  CustomClickableSpan(CharSequence text, Object tag, Range range, OnTextLongClickListener onTextLongClickListener) {
    this(text, tag, range);
    this.onTextLongClickListener = onTextLongClickListener;
  }

  OnTextClickListener getOnTextClickListener() {
    return onTextClickListener;
  }

  OnTextLongClickListener getOnTextLongClickListener() {
    return onTextLongClickListener;
  }

  void setOnTextClickListener(OnTextClickListener onTextClickListener) {
    this.onTextClickListener = onTextClickListener;
  }

  void setOnTextLongClickListener(OnTextLongClickListener onTextLongClickListener) {
    this.onTextLongClickListener = onTextLongClickListener;
  }

  @Override
  public void onClick(View view) {
    if (onTextClickListener != null) {
      handleClickEvent(view, new Runnable() {
        @Override
        public void run() {
          onTextClickListener.onClicked(text, range, tag);
        }
      });
    }
  }

  @Override
  public void updateDrawState(TextPaint ds) {
  }

  void onLongClick(View view) {
    if (onTextLongClickListener != null) {
      handleClickEvent(view, new Runnable() {
        @Override
        public void run() {
          onTextLongClickListener.onLongClicked(text, range, tag);
        }
      });
    }
  }

  private void handleClickEvent(View view, Runnable runnable) {
    // prevent calling view's onClick listener after
    view.setEnabled(false);
    runnable.run();
    view.setEnabled(true);
  }
}
