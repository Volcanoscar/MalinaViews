package com.shchurov.malinaviews.library;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Set;

public class TagsView extends ViewGroup {

    private static final int EDIT_TEXT_MIN_WIDTH_DP = 100;
    private static final int MAX_TAG_LENGTH = 25;

    private EditText editText;
    private LinkedHashMap<String, TextView> tagViewMap = new LinkedHashMap<String, TextView>();
    private int editTextMinWidth;
    private int horizontalSpacing;
    private int verticalSpacing;
    private OnTagClickListener onTagClickListener;
    private int tagsResourceId;
    private boolean clearingEditText;

    public TagsView(Context context) {
        super(context);
        init();
    }

    public TagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        editTextMinWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EDIT_TEXT_MIN_WIDTH_DP,
                getResources().getDisplayMetrics());
        editText = (EditText) LayoutInflater.from(getContext()).inflate(R.layout.tags_view_edit_text, this, false);
        editText.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(MAX_TAG_LENGTH)});
        addView(editText, 0);
    }

    public void setEditTextVisible(boolean visible) {
        editText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (clearingEditText) {
                clearingEditText = false;
                return null;
            }
            // doesn't work on some devices (nexus4, nexus7, ...), method not called
            if (end == 0 && dend == 0) {
                removeLastTag();
            }
            for (int i = 0; i < source.length(); i++) {
                char c = source.charAt(i);
                if (!Character.isLetterOrDigit(c) && c != '_') {
                    if (c == ' ') {
                        addTag(dest.subSequence(0, dstart + i).toString());
                        clearingEditText = true;
                        editText.setText("");
                    }
                    return "";
                }
            }
            return null;
        }
    };

    public void setOnTagClickListener(OnTagClickListener listener) {
        this.onTagClickListener = listener;
    }

    public void setTagsResourceId(int resId) {
        tagsResourceId = resId;
    }

    public void setInputTextColor(int color) {
        editText.setTextColor(color);
    }

    public void setInputHintColor(int color) {
        editText.setHintTextColor(color);
    }

    public void setSpacing(int horizontal, int vertical) {
        horizontalSpacing = horizontal;
        verticalSpacing = vertical;
    }

    public void addTag(String tag) {
        if (tagViewMap.get(tag) != null || tag.length() == 0)
            return;
        TextView tv = (tagsResourceId == 0) ? new TextView(getContext()) : (TextView) LayoutInflater.from(getContext()).inflate(tagsResourceId, this, false);
        tv.setText(tag);
        tv.setOnClickListener(onClickListener);
        if (tv.getTextSize() < editText.getTextSize()) {
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX ,tv.getTextSize());
        }
        tagViewMap.put(tag, tv);
        addView(tv, getChildCount() - 1);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onTagClickListener != null) {
                onTagClickListener.onTagClick(((TextView) v).getText().toString());
            }
        }
    };

    public void removeTag(String tag) {
        TextView tv = tagViewMap.get(tag);
        if (tv == null)
            return;
        removeView(tv);
        tagViewMap.remove(tag);
    }

    public void setHint(String hint) {
        editText.setHint(hint);
    }

    public Set<String> getTags() {
        return tagViewMap.keySet();
    }

    private void removeLastTag() {
        int childCount = getChildCount();
        if (childCount == 1)
            return;
        TextView tv = (TextView) getChildAt(childCount - 2);
        tagViewMap.remove(tv.getText().toString());
        removeViewAt(childCount - 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalArgumentException("widthMeasureSpec mode can't be UNSPECIFIED");
        }
        int availableHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int childHeightMeasureSpec = (heightMode == MeasureSpec.UNSPECIFIED) ? MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED) :
                MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.AT_MOST);
        int rightLimit = width - getPaddingRight();
        int childCount = getChildCount();
        int x = getPaddingLeft();
        int y = getPaddingTop();
        int rowHeight = 0;
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE)
                continue;
            int childRequestedWidth = child.getLayoutParams().width;
            int childWidthMeasureSpec = (childRequestedWidth > 0) ? MeasureSpec.makeMeasureSpec(childRequestedWidth, MeasureSpec.EXACTLY) :
                    MeasureSpec.makeMeasureSpec(width - getPaddingLeft() - getPaddingRight(), MeasureSpec.AT_MOST);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (x + childWidth > rightLimit) {
                x = getPaddingLeft();
                y += rowHeight + verticalSpacing;
                rowHeight = 0;
            }
            rowHeight = Math.max(childHeight, rowHeight);
            x += childWidth + horizontalSpacing;
        }
        if (editText.getVisibility() != View.GONE) {
            if (x + editTextMinWidth > rightLimit) {
                editText.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), childHeightMeasureSpec);
                y += rowHeight + verticalSpacing;
                rowHeight = editText.getMeasuredHeight();
            } else {
                int editTextHeightMeasureSpec = (childCount == 1) ? childHeightMeasureSpec : MeasureSpec.makeMeasureSpec(rowHeight, MeasureSpec.EXACTLY);
                editText.measure(MeasureSpec.makeMeasureSpec(rightLimit - x, MeasureSpec.EXACTLY), editTextHeightMeasureSpec);
                if (childCount == 1) {
                    rowHeight = editText.getMeasuredHeight();
                }
            }
        }
        int height = resolveSize(y + verticalSpacing + rowHeight + getPaddingBottom(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int rightLimit = right - left - getPaddingRight();
        int childCount = getChildCount();
        int x = getPaddingLeft();
        int y = getPaddingTop();
        int rowHeight = 0;
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE)
                continue;
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (x + childWidth > rightLimit) {
                x = getPaddingLeft();
                y += rowHeight + verticalSpacing;
                rowHeight = 0;
            }
            child.layout(x, y, x + childWidth, y + childHeight);
            rowHeight = Math.max(childHeight, rowHeight);
            x += childWidth + horizontalSpacing;
        }
        if (editText.getVisibility() == View.GONE)
            return;
        if (x + editTextMinWidth > rightLimit) {
            y += rowHeight + verticalSpacing;
            editText.layout(getPaddingLeft(), y, rightLimit, y + editText.getMeasuredHeight());
        } else {
            if (childCount == 1) {
                rowHeight = editText.getMeasuredHeight();
            }
            editText.layout(x, y, rightLimit, y + rowHeight);
        }
    }

    public interface OnTagClickListener {

        void onTagClick(String tag);

    }

}
