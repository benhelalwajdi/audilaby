package com.audiolaby.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.controller.enumeration.TypeField;
import com.audiolaby.persistence.model.Category;
import com.audiolaby.view.activity.MoreActivity_;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesCoverAdapter extends Adapter<CategoriesCoverAdapter.SimpleViewHolder> {
    private final Context context;
    private boolean dummy;
    private List<Category> categoryList;



    class onClickListener implements OnClickListener {

        Category category;
        onClickListener(Category category) {
            this.category=category;
        }

        public void onClick(View v) {
            MoreActivity_.intent(CategoriesCoverAdapter.this.context)
                    .title(category.getName())
                    .type(TypeField.category.name())
                    .id(category.getCategory_id())
                    .start();
        }
    }

    public static class SimpleViewHolder extends ViewHolder {
        public final View card;
        public final ImageView image;
        TextView title,count;

        public SimpleViewHolder(View view) {
            super(view);
            this.card = view;
            this.image = (ImageView) view.findViewById(R.id.image);
            this.title = (TextView) view.findViewById(R.id.title);
            this.count = (TextView) view.findViewById(R.id.count);
        }
    }

    public CategoriesCoverAdapter(Context context, boolean dummy) {
        this.context = context;
        this.dummy = dummy;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_categories_cover, parent, false));
    }

    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        if (!this.dummy) {
            Category category = (Category) this.categoryList.get(position);
            Picasso.with(this.context).load(category.getPicture()).error(R.drawable.empty).placeholder(R.drawable.empty).into(holder.image);
            holder.card.setOnClickListener(new onClickListener(category));

            holder.title.setText(category.getName());
            if(category.getSize()<=10)
                holder.count.setText(String.format(this.context.getString(R.string.books_count1), "" + category.getSize()));
            else
                holder.count.setText(String.format(this.context.getString(R.string.books_count2), "" + category.getSize()));
        }
    }

    public int getItemCount() {
        if (this.categoryList != null) {
            return this.categoryList.size();
        }
        return 0;
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
}
