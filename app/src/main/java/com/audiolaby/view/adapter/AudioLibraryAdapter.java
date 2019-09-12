package com.audiolaby.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.util.MusicVisualizer;
import com.audiolaby.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AudioLibraryAdapter extends Adapter<ViewHolder> {

    private Context mContext;
    private List<AudioArticle> audioArticles;
    private String selected;
    private OnClickListener clickListener;


    public AudioLibraryAdapter(Context mContext, List<AudioArticle> items) {
        this.audioArticles = new ArrayList();
        this.mContext = mContext;
        this.audioArticles = items;
        clickListener = (OnClickListener) mContext;
        selected = "";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_library, parent, false);
        return new SimpleViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final AudioArticle audioArticle = audioArticles.get(position);
        if (holder instanceof SimpleViewHolder) {
            final SimpleViewHolder viewHolder = (SimpleViewHolder) holder;

            if (audioArticle.getPost_id().equals(selected)) {
                viewHolder.visualizer.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                viewHolder.visualizer.setVisibility(View.VISIBLE);
            } else

                viewHolder.visualizer.setVisibility(View.INVISIBLE);


            viewHolder.title.setText(audioArticle.getTitle());
            viewHolder.voiceover.setText(audioArticle.getVoiceOver().getName());
            if (audioArticle.getIs_parted())
                viewHolder.time.setText(String.format(mContext.getString(R.string.parts_number), "" + audioArticle.getPostParts().size()));
            else
                viewHolder.time.setText(String.format(mContext.getString(R.string.runtime), "" + Utils.formatSecondsToHoursMinutesAndSeconds(audioArticle.getRuntime())));
            viewHolder.category.setText(audioArticle.getCategory().getName());

            try {
                Picasso.with(this.mContext).load(audioArticle.getCover()).error(R.drawable.empty).placeholder(R.drawable.empty).into(viewHolder.picture);
            } catch (Exception e) {
            }

            viewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(audioArticle, position);
                }
            });

            viewHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickListener.onLongClick(audioArticle, position);
                    return true;
                }
            });

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
        return audioArticles.size();
    }

    public class SimpleViewHolder extends ViewHolder {
        TextView title, voiceover, time, category;
        ImageView picture;
        MusicVisualizer visualizer;
        public View card;

        public SimpleViewHolder(View view) {
            super(view);
            this.card = view;
            title = (TextView) view.findViewById(R.id.title);
            voiceover = (TextView) view.findViewById(R.id.voiceover);
            time = (TextView) view.findViewById(R.id.time);
            category = (TextView) view.findViewById(R.id.category);
            visualizer = (MusicVisualizer) view.findViewById(R.id.visualizer);
            picture = (ImageView) view.findViewById(R.id.picture);
        }

    }


    public List<AudioArticle> getAudioArticles() {
        return audioArticles;
    }

    public void setAudioArticles(List<AudioArticle> audioArticles) {
        this.audioArticles = audioArticles;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }


    public interface OnClickListener {
        void onClick(AudioArticle audioArticle, int position);

        void onLongClick(AudioArticle audioArticle, int position);
    }
}