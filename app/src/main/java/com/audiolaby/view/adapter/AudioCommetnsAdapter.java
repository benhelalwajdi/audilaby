package com.audiolaby.view.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.Comment;
import com.audiolaby.view.dialog.CommentsDialog_;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AudioCommetnsAdapter extends Adapter<AudioCommetnsAdapter.SimpleViewHolder> {
    private final Context context;
    private boolean dummy;
    private List<Comment> comments;
    private AudioArticle audioArticle;


    public static class SimpleViewHolder extends ViewHolder {
        CircleImageView image;
        TextView name;
        TextView content;
        TextView date;
        ProgressBar progress;
        View card;


        public SimpleViewHolder(View view) {
            super(view);
            card = view;
            image = (CircleImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            content = (TextView) view.findViewById(R.id.content);
            date = (TextView) view.findViewById(R.id.date);
            progress = (ProgressBar) view.findViewById(R.id.progress);

        }
    }

    public AudioCommetnsAdapter(Context context, boolean dummy) {
        this.context = context;
        this.dummy = dummy;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_post_comment, parent, false));
    }

    public void onBindViewHolder(SimpleViewHolder holder, int position) {

            final int pos = position;
            final Comment rowItem = (Comment) this.comments.get(position);
            final SimpleViewHolder viewHolder = (SimpleViewHolder) holder;

            if (rowItem.getUser().getImage() != null && !rowItem.getUser().getImage().isEmpty())
                Picasso.with(context).load(rowItem.getUser().getImage()).error(R.drawable.ic_anonymou)
                        .placeholder(R.drawable.empty).into(viewHolder.image);

            viewHolder.name.setText(rowItem.getUser().getFullName());
            String contentString = rowItem.getComment().replace("\n", System.getProperty("line.separator"));
            viewHolder.content.setText("" + Html.fromHtml(contentString));
            if (rowItem.getSending()) {
                viewHolder.progress.setVisibility(View.VISIBLE);
                viewHolder.date.setVisibility(View.GONE);
            } else {
               // viewHolder.date.setText(DateUtils.getRelativeTimeSpanString((new Date(rowItem.getDate() * 1000L)).getTime(), System.currentTimeMillis(), 0));
                viewHolder.progress.setVisibility(View.GONE);
                viewHolder.date.setVisibility(View.VISIBLE);
            }
            viewHolder.card.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommentsDialog_.builder()
                            .post(audioArticle)
                            .build()
                            .show(((AppCompatActivity) context).getSupportFragmentManager(), "comments");
                }
            });
    }

    public int getItemCount() {
        if (this.comments != null) {
            return this.comments.size();
        }
        return 0;
    }

    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public AudioArticle getAudioArticle() {
        return audioArticle;
    }

    public void setAudioArticle(AudioArticle audioArticle) {
        this.audioArticle = audioArticle;
    }
}



