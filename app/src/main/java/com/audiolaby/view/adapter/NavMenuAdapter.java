package com.audiolaby.view.adapter;

import android.app.Service;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.audiolaby.R;


public class NavMenuAdapter extends BaseAdapter {
    private static final int DOWNLOAD_POSITION = 11;
    private Context context;
    private int[] imgRes;
    private LayoutInflater inflater;
    private OnCheckedChangeListener offlineListener;
    private int selected;
    private int[] titleRes;


    public NavMenuAdapter(Context context, int selected) {
       // this.titleRes = new int[]{ R.string.discover,R.string.my_library, R.string.wishList, R.string.empty, R.string.settings, R.string.donate, R.string.about, R.string.logout};
       // this.imgRes = new int[]{ R.string.icon_discover,R.string.icon_library, R.string.icon_wish,  R.string.empty, R.string.icon_settings,R.string.icon_love,R.string.icon_info, R.string.icon_logout};
        this.titleRes = new int[]{ R.string.discover,R.string.my_library, R.string.wishList, R.string.empty, R.string.settings, R.string.contact, R.string.logout};
        this.imgRes = new int[]{ R.string.icon_discover,R.string.icon_library, R.string.icon_wish,  R.string.empty, R.string.icon_settings, R.string.icon_send, R.string.icon_logout};
        this.selected = selected;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.titleRes.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getItemViewType(int position) {
        return position == 3 ? 0 : 1;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getSelected() {
        return this.selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        int type = getItemViewType(position);
        if (view == null) {
            if (type == 0) {
                view = this.inflater.inflate(R.layout.menu_line_nav_drawer, null);
                view.setEnabled(false);
            } else {
                view = this.inflater.inflate(R.layout.menu_nav_drawer, null);
            }
        }
        if (type != 0) {
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView icon = (TextView) view.findViewById(R.id.icon);

            title.setText(this.titleRes[position]);
            icon.setText(this.imgRes[position]);

            View underline = view.findViewById(R.id.underline);
            SwitchCompat toggle = (SwitchCompat) view.findViewById(R.id.toggle);

            if (position == DOWNLOAD_POSITION) {
                toggle.setVisibility(View.VISIBLE);
                underline.setVisibility(View.GONE);
                toggle.setOnCheckedChangeListener(this.offlineListener);
            }  else {
                toggle.setVisibility(View.GONE);
                if (position == this.selected) {
                    title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    icon.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    //underline.setVisibility(View.VISIBLE);
                } else {
                    title.setTextColor(context.getResources().getColor(R.color.text_black));
                    icon.setTextColor(context.getResources().getColor(R.color.text_black));
                   // underline.setVisibility(View.GONE);
                }
            }
        }
        return view;
    }

    public void setOfflineListener(OnCheckedChangeListener offlineListener) {
        this.offlineListener = offlineListener;
    }

}
