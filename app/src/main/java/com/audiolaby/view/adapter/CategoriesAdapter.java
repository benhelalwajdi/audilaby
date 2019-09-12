package com.audiolaby.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.audiolaby.R;
import com.audiolaby.controller.enumeration.TypeField;
import com.audiolaby.persistence.model.Category;
import com.audiolaby.view.activity.MoreActivity_;
import com.audiolaby.view.adapter.decoration.SpaceItemDecoration;
import com.audiolaby.view.layout.SmoothLinearLayoutManager;
import com.facebook.login.widget.ToolTipPopup;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;

public class CategoriesAdapter extends Adapter<ViewHolder> {
    private final Context context;
    private boolean dummy;
    private List<Category> categoryList;
    private List<Category> categoryCover;

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CategoriesShowcaseViewHolder) {

            CategoriesShowcaseViewHolder viewHolder = (CategoriesShowcaseViewHolder) holder;
            if (this.categoryCover != null && !this.categoryCover.isEmpty()) {
                viewHolder.adapter.setDummy(false);
                viewHolder.adapter.setCategoryList(categoryCover);
                viewHolder.adapter.notifyDataSetChanged();
            }

        } else if (holder instanceof CategoriesViewHolder) {

            CategoriesViewHolder viewHolder2 = (CategoriesViewHolder) holder;
            viewHolder2.adapter.setCategoryList(this.categoryList);
            viewHolder2.elements.addItemDecoration(new SpaceItemDecoration(10));
            viewHolder2.adapter.notifyDataSetChanged();
        }
    }


    public static class CategoriesShowcaseViewHolder extends ViewHolder {
        private CategoriesCoverAdapter adapter;
        private Context context;
        public final RecyclerView elements;
        private int mPosition;

        class timerTask extends TimerTask {
            timerTask() {
            }

            public void run() {
                if (CategoriesShowcaseViewHolder.this.mPosition + 1 >=
                        CategoriesShowcaseViewHolder.this.adapter.getItemCount()) {
                    CategoriesShowcaseViewHolder.this.mPosition = -1;
                }
                CategoriesShowcaseViewHolder.this.elements.smoothScrollToPosition(getPos(CategoriesShowcaseViewHolder.this));
            }
        }

        static int getPos(CategoriesShowcaseViewHolder categoriesShowcaseViewHolder) {
            int i = categoriesShowcaseViewHolder.mPosition + 1;
            categoriesShowcaseViewHolder.mPosition = i;
            return i;
        }

        public CategoriesShowcaseViewHolder(Context context, View view) {
            super(view);
            this.context = context;
            this.elements = (RecyclerView) view.findViewById(R.id.elements);
            this.elements.setNestedScrollingEnabled(false);
            this.adapter = new CategoriesCoverAdapter(context, true);
            this.elements.setLayoutManager(new SmoothLinearLayoutManager(this.context, 0, false));
            this.elements.setAdapter(new AlphaAnimatorAdapter(this.adapter, this.elements));
            new Timer().schedule(new timerTask(), ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME, ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME);
        }
    }

    public static class CategoriesViewHolder extends ViewHolder {
        private CategoryListAdapter adapter;
        public final RecyclerView elements;

        public CategoriesViewHolder(Context context, View view, List<Category> categoryList, boolean dummy) {
            super(view);
            this.elements = (RecyclerView) view.findViewById(R.id.elements);
            this.adapter = new CategoryListAdapter(context);
            this.elements.setLayoutManager(new LinearLayoutManager(context, 1, false));
            this.elements.setAdapter(new AlphaAnimatorAdapter(this.adapter, this.elements));
        }
    }

    public CategoriesAdapter(Context context, boolean dummy) {
        this.context = context;
        this.dummy = dummy;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new CategoriesShowcaseViewHolder(this.context, LayoutInflater.from(this.context).inflate(R.layout.item_banner_parent, parent, false));
            case 1:
                return new CategoriesViewHolder(this.context, LayoutInflater.from(this.context).inflate(R.layout.item_categories_list, parent, false), this.categoryList, this.dummy);
            default:
                return null;
        }
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    public int getItemCount() {
        return 2;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }


    public List<Category> getCategoryCover() {
        return categoryCover;
    }

    public void setCategoryCover(List<Category> categoryCover) {
        this.categoryCover = categoryCover;
    }
}
