package com.audiolaby.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import com.audiolaby.Constants;
import com.audiolaby.R;
import com.audiolaby.persistence.model.Author;
import com.audiolaby.persistence.model.Category;
import com.audiolaby.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class NameSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List elements;
    private boolean useWhiteTextColor;

    public NameSpinnerAdapter(Context context, List<String> elements) {
        this(context, elements, false);
    }

    public NameSpinnerAdapter(Context context, List<String> elements, boolean useWhiteTextColor) {
        this.context = context;
        this.elements = elements;
        this.useWhiteTextColor = useWhiteTextColor;
    }

    public int getCount() {
        return this.elements != null ? this.elements.size() : 0;
    }

    public Object getItem(int position) {
        return this.elements.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return buildView(position, convertView, parent, 0, this.useWhiteTextColor ? this.context.getResources().getColor(android.R.color.white) : this.context.getResources().getColor(R.color.black_85));
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return buildView(position, convertView, parent, R.drawable.spinner_background, this.context.getResources().getColor(R.color.black_85));
    }

    private View buildView(int position, View convertView, ViewGroup parent, int backgroundRes, int textColor) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_spinner, null);
        if (backgroundRes != 0) {
            view.setBackgroundResource(backgroundRes);
        }
        TextView text = (TextView) view.findViewById(R.id.name);
        text.setTextColor(textColor);
        String name = StringUtils.EMPTY;
        Object model = this.elements.get(position);
        if (model instanceof Author) {
            name = ((Author) model).getName();
        } else if (model instanceof Category) {
            name = ((Category) model).getName();
        } else if (model instanceof String) {
            name = (String) model;
        }
        text.setText(name);
        text.setPadding(30, 0, 30, 0);
        return view;
    }
}
