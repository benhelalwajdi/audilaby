package com.audiolaby.view.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.persistence.model.Cover;
import com.audiolaby.view.player.utils.timely.TimelyView;
import com.squareup.picasso.Picasso;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class CoverAdapter extends Adapter<CoverAdapter.SimpleViewHolder> {
    private final Context context;
    private Activity activity;
    private boolean dummy;
    private List<Cover> coverList;


    public static class SimpleViewHolder extends ViewHolder {
        public final View card;
        public final ImageView image;
        public final LinearLayout info, counterLayout;
        public final TextView title, description1, views, downloads;
        public final Button description2;
        public ArrayList<TimelyView> disgitsL;
        public ArrayList<TimelyView> disgitsD;


        public SimpleViewHolder(View view) {
            super(view);
            this.card = view;
            this.image = (ImageView) view.findViewById(R.id.image);
            this.info = (LinearLayout) view.findViewById(R.id.info);
            this.title = (TextView) view.findViewById(R.id.title);
            this.description1 = (TextView) view.findViewById(R.id.description1);
            this.description2 = (Button) view.findViewById(R.id.description2);


            this.views = (TextView) view.findViewById(R.id.views);
            this.downloads = (TextView) view.findViewById(R.id.downloads);


            this.disgitsL = new ArrayList<TimelyView>();
            this.disgitsL.add((TimelyView) view.findViewById(R.id.digit_l10m));
            this.disgitsL.add((TimelyView) view.findViewById(R.id.digit_l1m));
            this.disgitsL.add((TimelyView) view.findViewById(R.id.digit_l100k));
            this.disgitsL.add((TimelyView) view.findViewById(R.id.digit_l10k));
            this.disgitsL.add((TimelyView) view.findViewById(R.id.digit_l1k));
            this.disgitsL.add((TimelyView) view.findViewById(R.id.digit_l100));
            this.disgitsL.add((TimelyView) view.findViewById(R.id.digit_l10));
            this.disgitsL.add((TimelyView) view.findViewById(R.id.digit_l1));

            this.disgitsD = new ArrayList<TimelyView>();
            this.disgitsD.add((TimelyView) view.findViewById(R.id.digit_d10m));
            this.disgitsD.add((TimelyView) view.findViewById(R.id.digit_d1m));
            this.disgitsD.add((TimelyView) view.findViewById(R.id.digit_d100k));
            this.disgitsD.add((TimelyView) view.findViewById(R.id.digit_d10k));
            this.disgitsD.add((TimelyView) view.findViewById(R.id.digit_d1k));
            this.disgitsD.add((TimelyView) view.findViewById(R.id.digit_d100));
            this.disgitsD.add((TimelyView) view.findViewById(R.id.digit_d10));
            this.disgitsD.add((TimelyView) view.findViewById(R.id.digit_d1));

            this.counterLayout = (LinearLayout) view.findViewById(R.id.counter_layout);
        }
    }

    public CoverAdapter(Context context, Activity activity, boolean dummy) {
        this.context = context;
        this.activity = activity;
        this.dummy = dummy;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_cover, parent, false));
    }

    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        if (!this.dummy) {
            final Cover cover = (Cover) this.coverList.get(position);

            if (cover.getStat() != null && cover.getStat()) {
                holder.image.setImageResource(R.drawable.stat_bg);
                holder.counterLayout.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.GONE);
                holder.info.setVisibility(View.GONE);

                holder.views.setVisibility(GONE);
                holder.downloads.setVisibility(GONE);
//                holder.downloads.setText(numberFormat(cover.getDonwloads()));
//                holder.views.setText(numberFormat(cover.getViews()));

                generateDigit(holder.disgitsL, cover.getViews());
                generateDigit(holder.disgitsD, cover.getDonwloads());
            } else {

                holder.counterLayout.setVisibility(View.GONE);
                try {
                    Picasso.with(this.context).load(cover.getImage()).error(R.drawable.empty).placeholder(R.drawable.empty).into(holder.image);

                } catch (Exception e) {
                }
                if (cover.getTitle() != null && !cover.getTitle().isEmpty()) {
                    holder.title.setText(cover.getTitle());
                    holder.title.setVisibility(View.VISIBLE);
                    holder.info.setVisibility(View.VISIBLE);
                }
                if (cover.getDescription() != null && !cover.getDescription().isEmpty()) {
                    holder.description1.setText(cover.getDescription());
                    holder.description1.setVisibility(View.VISIBLE);
                    holder.info.setVisibility(View.VISIBLE);
                }

                if (cover.getUrl() != null && !cover.getUrl().isEmpty()) {
                    if (cover.getDescription2() != null && !cover.getDescription2().isEmpty()) {
                        holder.description2.setText(cover.getDescription2());
                        holder.description2.setVisibility(View.VISIBLE);
                        holder.info.setVisibility(View.VISIBLE);
                        holder.description2.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cover.getUrl()));
                                context.startActivity(browserIntent);
                            }
                        });
                    } else {

                            holder.card.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cover.getUrl()));
                                    context.startActivity(browserIntent);
                                }
                            });

                    }
                }
            }


        }
    }


    public void generateDigit(ArrayList<TimelyView> disgits, int value) {

        //        String valueS =Format(value);
        String valueS = "" + value;

        for (int i = 0; i < disgits.size(); i++) {

            if (i >= valueS.length())
                disgits.get(i).setVisibility(GONE);
            else
                animateDigit(disgits.get(i), valueS.charAt(i) - '0');
        }


    }


    public void animateDigit(final TimelyView tv, final int value) {


        if (value == 0)
            changeDigit(tv, 0);

        else {
            new CountDownTimer((value + 1) * 350, 350) {

                public void onTick(long millisUntilFinished) {

                    int current = (int) ((int) millisUntilFinished / 350);
                    int end = (value + 1) - current;
                    changeDigit(tv, end - 1, end);
                }

                public void onFinish() {
                }
            }.start();
        }

    }


    public void changeDigit(TimelyView tv, int start, int end) {
        try {
            ObjectAnimator obja = tv.animate(start, end);
            obja.setDuration(300);
            obja.start();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }

    public void changeDigit(TimelyView tv, int end) {
        ObjectAnimator obja = tv.animate(end);
        obja.setDuration(300);
        obja.start();
    }


    public String numberFormat(Integer number) {
        String[] suffix = new String[]{" k", " m", " b", " t"};
        int size = (number.intValue() != 0) ? (int) Math.log10(number) : 0;
        if (size >= 3) {
            while (size % 3 != 0) {
                size = size - 1;
            }
        }
        double notation = Math.pow(10, size);
        String result = (size >= 3) ? +(Math.round((number / notation) * 100) / 100.0d) + suffix[(size / 3) - 1] : +number + "";
        return result;
    }

    public int getItemCount() {
        if (this.coverList != null) {
            return this.coverList.size();
        }
        return 0;
    }

    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    public List<Cover> getCoverList() {
        return coverList;
    }

    public void setCoverList(List<Cover> coverList) {
        this.coverList = coverList;
    }
}
