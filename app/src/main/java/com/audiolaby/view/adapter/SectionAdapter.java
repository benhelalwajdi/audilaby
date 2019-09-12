package com.audiolaby.view.adapter;

import android.content.Context;
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
import com.audiolaby.controller.enumeration.SectionMap;
import com.audiolaby.controller.enumeration.SectionType;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.Section;
import com.audiolaby.view.activity.BaseActivity;
import com.audiolaby.view.activity.MoreActivity_;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import com.lapism.searchview.SearchView;

import java.util.ArrayList;
import java.util.List;

import commons.validator.routines.AbstractNumberValidator;
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;

public class SectionAdapter extends Adapter<ViewHolder> {
    Context context;
    private boolean dummy;
    private List<Section> sectionList;
    private BottomSheetLayout layout;
    private BaseActivity activity;
    private OnMenuItemClickListener menuItemClickListener;
    private List<AudioPostAdapter> postCardAdapters;
    private SectionInterface sectionIterface;
    private LibraryDAO libraryDAO;


    public class RegularViewHolder extends ViewHolder {
        AudioPostAdapter adapter;
        private  View card;
        private Context context;
        public final RecyclerView elements;
        public final Button more;
        public final TextView name;

        public RegularViewHolder(Context context, View view, BottomSheetLayout layout) {
            super(view);
            card=view;
            this.context = context;
            this.name = (TextView) view.findViewById(R.id.name);
            this.more = (Button) view.findViewById(R.id.more);
            this.elements = (RecyclerView) view.findViewById(R.id.elements);
            this.elements.setNestedScrollingEnabled(false);
            this.adapter = new AudioPostAdapter(context, layout, new ArrayList());
            this.adapter.setLibraryDAO(libraryDAO);
            this.elements.setLayoutManager(new LinearLayoutManager(this.context, 0, false));
            this.elements.setAdapter(new ScaleInAnimatorAdapter(this.adapter, this.elements));
        }
    }


    public static class FeaturedViewHolder extends ViewHolder {
        public final View container;
        private  View card;
        public final TextView count;
        public final ImageView cover1;
        public final ImageView cover2;
        public final ImageView cover3;
        public final TextView title;

        public FeaturedViewHolder(View view) {
            super(view);
            card=view;
            this.container = view;
            this.title = (TextView) view.findViewById(R.id.title);
            this.count = (TextView) view.findViewById(R.id.count);
            this.cover1 = (ImageView) view.findViewById(R.id.cover_1);
            this.cover2 = (ImageView) view.findViewById(R.id.cover_2);
            this.cover3 = (ImageView) view.findViewById(R.id.cover_3);
        }
    }




    public SectionAdapter(Context context, SectionInterface sectionIterface) {
        this.context = context;
        this.postCardAdapters = new ArrayList();
        this.sectionIterface = sectionIterface;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new FeaturedViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_featured, parent, false));

            case 1:
                return new RegularViewHolder(this.context, LayoutInflater.from(this.context).inflate(R.layout.item_section, parent, false), this.layout);

            default:
                return null;
        }

    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final Section section = (Section) this.sectionList.get(position);
        if (holder instanceof RegularViewHolder) {

            RegularViewHolder viewHolder = (RegularViewHolder) holder;
            String sectionTitle = (String) SectionMap.getSectionTitle(section.getSectionType(), context);

            viewHolder.name.setText(sectionTitle);
            sectionIterface.load(viewHolder, position, section);
            //loadCarouselData(viewHolder, position, section);
            viewHolder.more.setOnClickListener(new moreOnClickListener(section, sectionTitle));
            // viewHolder.adapter.setMenuItemClickListener(new onMenuItemClickListener(section, position));
            //viewHolder.adapter.setSectionType(section.getSectionType());
            viewHolder.adapter.setSectionIndex(position);
            this.postCardAdapters.add(viewHolder.adapter);
        } else if (holder instanceof FeaturedViewHolder) {
            FeaturedViewHolder viewHolder1 = (FeaturedViewHolder) holder;
            sectionIterface.load(viewHolder1, position, section);
            this.postCardAdapters.add(null);
        }


    }


    public int getItemViewType(int position) {
        if (this.sectionList.get(position).getSectionType() == SectionType.FEATURED)
            return 0;
       else
            return 1;
    }


    class moreOnClickListener implements OnClickListener {
        final String sectionTitle;
        final Section section;

        moreOnClickListener(Section section, String str) {
            this.section = section;
            this.sectionTitle = str;
        }

        public void onClick(View v) {

            MoreActivity_.intent(SectionAdapter.this.context)
                    .title(sectionTitle)
                    .type(SectionMap.getSectionType(section.getSectionType())[0])
                    .sort(SectionMap.getSectionType(section.getSectionType())[1])
                    .id("")
                    .start();
        }
    }


    interface SectionInterface {
        public void load(ViewHolder viewHolder, int position, Section section);
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(int i, Object obj, Section section, Integer num);

        boolean onMenuItemClick(MenuItem menuItem, Object obj, Section section, Integer num);
    }
//    class onMenuItemClickListener implements AudioPostAdapter.OnMenuItemClickListener {
//        final  int position;
//        final  Section section;
//
//        onMenuItemClickListener(Section section, int i) {
//            this.section = section;
//            this.position = i;
//        }
//
//        public boolean onMenuItemClick(MenuItem item, Object object) {
//            return SectionAdapter.this.menuItemClickListener.onMenuItemClick(item, object, this.section, Integer.valueOf(this.position));
//        }
//    }

    public int getItemCount() {
        if (this.sectionList != null) {
            return this.sectionList.size();
        }
        return 0;
    }

    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public BottomSheetLayout getLayout() {
        return layout;
    }

    public void setLayout(BottomSheetLayout layout) {
        this.layout = layout;
    }

    public OnMenuItemClickListener getMenuItemClickListener() {
        return menuItemClickListener;
    }

    public void setMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
    }

//    public List<AudioPostAdapter> getPostCardAdapters() {
//        return postCardAdapters;
//    }
//
//    public void setPostCardAdapters(List<AudioPostAdapter> postCardAdapters) {
//        this.postCardAdapters = postCardAdapters;
//    }

    public BaseActivity getActivity() {
        return activity;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
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
}
