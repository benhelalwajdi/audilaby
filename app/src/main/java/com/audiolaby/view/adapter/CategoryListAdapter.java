package com.audiolaby.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.controller.enumeration.CategoryType;
import com.audiolaby.controller.enumeration.SectionMap;
import com.audiolaby.controller.enumeration.SectionType;
import com.audiolaby.controller.enumeration.TypeField;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.Category;
import com.audiolaby.persistence.model.Section;
import com.audiolaby.view.activity.BaseActivity;
import com.audiolaby.view.activity.MoreActivity_;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;

public class CategoryListAdapter extends Adapter<ViewHolder> {
    Context context;
    private boolean dummy;
    private List<Category> categoryList;
    private LibraryDAO libraryDAO;



    public class RegularViewHolder extends ViewHolder {
        private  View card;
        public final TextView name;
        View view;
        LinearLayout layout;

        public RegularViewHolder( View view) {
            super(view);
            card=view;
            this.name = (TextView) view.findViewById(R.id.name);
            this.view = (View) view.findViewById(R.id.view);
            this.layout = (LinearLayout) view.findViewById(R.id.layout);
        }
    }



    public static class Ads2ViewHolder extends ViewHolder {


        private  View card;
        public Ads2ViewHolder(View view) {
            super(view);
            card=view;
        }
    }



    public CategoryListAdapter(Context context) {
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new RegularViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_categories, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Category category = (Category) this.categoryList.get(position);
        if (holder instanceof RegularViewHolder) {

            RegularViewHolder viewHolder = (RegularViewHolder) holder;

            if(category.getCategory_id().equals("1") || category.getCategory_id().equals("2"))
            {
                viewHolder.view.setBackgroundResource(R.color.black);
                viewHolder.name.setTextColor(Color.parseColor("#ffffff"));
                viewHolder.layout.setBackgroundResource(R.drawable.category_background_orange);
            }
            else
            {
                viewHolder.view.setBackgroundResource(R.color.colorPrimary);
                viewHolder.name.setTextColor(Color.parseColor("#000000"));
                viewHolder.layout.setBackgroundResource(R.drawable.category_background);

            }
            viewHolder.name.setText(category.getName());
            viewHolder.card.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category category = (Category) categoryList.get(position);

                    MoreActivity_.intent(context)
                            .title(category.getName())
                            .type(TypeField.category.name())
                            .id(category.getCategory_id())
                            .start();
                }
            });

        }



    }


    public int getItemViewType(int position) {

            return 0;
    }


    public int getItemCount() {
        if (this.categoryList != null) {
            return this.categoryList.size();
        }
        return 0;
    }

    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public LibraryDAO getLibraryDAO() {
        return libraryDAO;
    }

    public void setLibraryDAO(LibraryDAO libraryDAO) {
        this.libraryDAO = libraryDAO;
    }



    public void setVisibility(boolean isVisible,View itemView){
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
        if (isVisible){
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
        }else{
            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        itemView.setLayoutParams(param);
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
