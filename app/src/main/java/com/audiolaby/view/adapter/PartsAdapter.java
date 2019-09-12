package com.audiolaby.view.adapter;

import android.app.Activity;
import android.app.Dialog;
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
import com.audiolaby.persistence.model.AudioArticleMin;
import com.audiolaby.persistence.model.Part;
import com.audiolaby.persistence.model.PartMin;
import com.audiolaby.util.MusicVisualizer;
import com.audiolaby.util.Utils;
import com.audiolaby.view.activity.BasePlayerActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PartsAdapter extends Adapter<ViewHolder> {

    private Context mContext;
    private List<PartMin> items;
    private String selected;
    private Activity activity;
    private Dialog dialog;
    private AudioArticleMin audioArticle;


    public PartsAdapter(Context mContext, Activity activity, List<PartMin> items, AudioArticleMin audioArticle, Dialog dialog) {
        this.items = new ArrayList();
        this.mContext = mContext;
        this.activity = activity;
        this.audioArticle = audioArticle;
        this.dialog = dialog;
        this.items = items;

        selected="";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_parts_item, parent, false);
        return new SimpleViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final PartMin rowItem = items.get(position);

        if (holder instanceof SimpleViewHolder) {


            final SimpleViewHolder viewHolder = (SimpleViewHolder) holder;

            String id  = audioArticle.getPost_id()+"-"+position;
            if(id.equals(selected))
            {
                viewHolder.visualizer.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                viewHolder.visualizer.setVisibility(View.VISIBLE);
            }

            else

                viewHolder.visualizer.setVisibility(View.INVISIBLE);


            viewHolder.post_title.setText(audioArticle.getTitle());
            viewHolder.voiceover.setText(audioArticle.getVoiceOver());
            viewHolder.time.setText(String.format(mContext.getString(R.string.runtime), "" + Utils.formatSecondsToHoursMinutesAndSeconds(rowItem.getDuration())));
            viewHolder.title.setText(rowItem.getTitle());

            try {
                Picasso.with(this.mContext).load(audioArticle.getCover()).error(R.drawable.empty).placeholder(R.drawable.empty).into(viewHolder.picture);
            } catch (Exception e) {
            }

            viewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     /* if (adapter.getSelected().isEmpty())
            ((BasePlayerActivity) getActivity()).putMusic(mediaMetadataCompats);
*/
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ((BasePlayerActivity) activity).onMediaItemSelected(audioArticle.getPost_id()+"-"+position);
                        }
                    }, 500);
                    setSelected(audioArticle.getPost_id()+"-"+position);
                    notifyDataSetChanged();
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
        return items.size();
    }


    public class SimpleViewHolder extends ViewHolder {
        TextView title,voiceover,time,post_title;
        ImageView picture;
        MusicVisualizer visualizer;
        public View card;

        public SimpleViewHolder(View view) {
            super(view);
            this.card = view;
            title = (TextView) view.findViewById(R.id.title);
            voiceover = (TextView) view.findViewById(R.id.voiceover);
            time = (TextView) view.findViewById(R.id.time);
            post_title = (TextView) view.findViewById(R.id.post_title);
            visualizer = (MusicVisualizer) view.findViewById(R.id.visualizer);
            picture = (ImageView) view.findViewById(R.id.picture);
        }

    }

    public List<PartMin> getItems() {
        return items;
    }

    public void setItems(List<PartMin> items) {
        this.items = items;
    }


    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public AudioArticleMin getAudioArticle() {
        return audioArticle;
    }

    public void setAudioArticle(AudioArticleMin audioArticle) {
        this.audioArticle = audioArticle;
    }

    public interface OnClickListener {
        void onClick(Part part, int position);
        void onLongClick(Part part, int position);
    }

}
