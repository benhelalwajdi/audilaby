package com.audiolaby.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.persistence.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentAdapter extends Adapter<ViewHolder> {

    private Context mContext;
    private List<Comment> items;


    public CommentAdapter(Context mContext, List<Comment> items) {
        this.items = new ArrayList();
        this.mContext = mContext;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_comments_item, parent, false);
        return new SimpleViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Comment rowItem = items.get(position);

        if (holder instanceof SimpleViewHolder) {


            final SimpleViewHolder viewHolder = (SimpleViewHolder) holder;

            if (rowItem.getUser().getImage() != null)
                if(rowItem.getUser().getImage()!=null && !rowItem.getUser().getImage().isEmpty())
                Picasso.with(this.mContext).load(rowItem.getUser().getImage()).error(R.drawable.empty)
                        .placeholder(R.drawable.empty).into(viewHolder.image);

            viewHolder.name.setText(rowItem.getUser().getFullName());
            String contentString = rowItem.getComment().replace("\n", System.getProperty("line.separator"));
            viewHolder.content.setText("" + Html.fromHtml(contentString));
            if (rowItem.getSending()) {
                viewHolder.progress.setVisibility(View.VISIBLE);
                viewHolder.date.setVisibility(View.GONE);
            } else {
//                viewHolder.date.setText(DateUtils.getRelativeTimeSpanString((new Date(rowItem.getDate() * 1000L)).getTime(), System.currentTimeMillis(), 0));
                viewHolder.progress.setVisibility(View.GONE);
                viewHolder.date.setVisibility(View.VISIBLE);
            }


        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class SimpleViewHolder extends ViewHolder {
        CircleImageView image;
        TextView name;
        TextView content;
        TextView date;
        ProgressBar progress;


        public SimpleViewHolder(View view) {
            super(view);
            image = (CircleImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            content = (TextView) view.findViewById(R.id.content);
            date = (TextView) view.findViewById(R.id.date);
            progress = (ProgressBar) view.findViewById(R.id.progress);

        }
    }

    public List<Comment> getItems() {
        return items;
    }

    public void setItems(List<Comment> items) {
        this.items = items;
    }


}
