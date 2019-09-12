package com.audiolaby.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.controller.AudioController;
import com.audiolaby.controller.CatalogController;
import com.audiolaby.controller.enumeration.ResponseStatus;
import com.audiolaby.controller.enumeration.SectionMap;
import com.audiolaby.controller.model.Pagination;
import com.audiolaby.controller.request.SectionItemsRequest;
import com.audiolaby.controller.response.PostsListResponse;
import com.audiolaby.controller.response.SectionItemResponse;
import com.audiolaby.controller.response.SectionListResponse;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.Category;
import com.audiolaby.persistence.model.Cover;
import com.audiolaby.persistence.model.Section;
import com.audiolaby.view.activity.BaseActivity;
import com.audiolaby.view.activity.MainActivity;
import com.audiolaby.view.activity.MoreActivity_;
import com.audiolaby.view.fragment.BrowseFragment;
import com.audiolaby.view.layout.SmoothLinearLayoutManager;
import com.facebook.login.widget.ToolTipPopup;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;
import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

import static com.audiolaby.Constants.POST_FIRST;
import static com.audiolaby.Constants.POST_SECTION_SIZE;

@EBean
public class BrowseAdapter extends Adapter<ViewHolder> implements SectionAdapter.SectionInterface {
    @RootContext
    Context context;
    private boolean dummy;
    private List<Section> sectionList;
    private List<Cover> coverList;
    private BottomSheetLayout layout;
    private BaseActivity activity;
    private SectionAdapter.OnMenuItemClickListener menuItemClickListener;
    private List<SectionAdapter> sectionAdapters;

    @Bean
    AudioController audioController;

    @Bean
    LibraryDAO libraryDAO;

    @Override
    public void load(ViewHolder viewHolder, int position, Section section) {
        loadCarouselData(viewHolder, position, section);
    }


    public class SectionViewHolder extends ViewHolder {
        SectionAdapter adapter;
        public final RecyclerView elements;

        public SectionViewHolder(Context context, View view, List<Section> sections, boolean dummy) {
            super(view);
            this.adapter = new SectionAdapter(context, BrowseAdapter.this);
            this.elements = (RecyclerView) view.findViewById(R.id.elements);
            this.elements.setLayoutManager(new LinearLayoutManager(context, 1, false));
            this.elements.setAdapter(new SlideInBottomAnimatorAdapter(this.adapter, this.elements));
            this.adapter.setActivity(BrowseAdapter.this.activity);
            this.adapter.setLayout(BrowseAdapter.this.layout);
            this.adapter.setLibraryDAO(libraryDAO);
            sectionAdapters.add(adapter);
        }
    }


    public class CoverViewHolder extends ViewHolder {
        private CoverAdapter adapter;
        private Context context;
        public final RecyclerView elements;
        private int position;
        LinearLayout main;

        public CoverViewHolder(Context context, View view) {
            super(view);
            this.position = 0;
            this.context = context;
            this.main = (LinearLayout) view.findViewById(R.id.main);
            this.elements = (RecyclerView) view.findViewById(R.id.elements);
            this.elements.setNestedScrollingEnabled(false);
            this.adapter = new CoverAdapter(context, activity, true);
            this.elements.setLayoutManager(new SmoothLinearLayoutManager(this.context, 0, false));
            this.elements.setAdapter(new AlphaAnimatorAdapter(this.adapter, this.elements));
            new Timer().schedule(new C10801(), ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME, ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME);
        }

        class C10801 extends TimerTask {
            C10801() {
            }

            public void run() {
                if (CoverViewHolder.this.elements.getAdapter().getItemCount() > 0) {
                    if (CoverViewHolder.this.position + 1 >= CoverViewHolder.this.adapter.getItemCount()) {
                        CoverViewHolder.this.position = -1;
                    }
                    CoverViewHolder.this.elements.smoothScrollToPosition(getPos(CoverViewHolder.this));
                }
            }
        }

        int getPos(CoverViewHolder coverViewHolder) {
            int i = coverViewHolder.position + 1;
            coverViewHolder.position = i;
            return i;
        }
    }

    public BrowseAdapter() {
        sectionAdapters = new ArrayList<SectionAdapter>();
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new CoverViewHolder(this.context,
                        LayoutInflater.from(this.context).inflate(R.layout.item_banner_parent, parent, false));
            case 1:
                return new SectionViewHolder(this.context,
                        LayoutInflater.from(this.context).inflate(R.layout.item_section_list, parent, false), this.sectionList, this.dummy);
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

    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof CoverViewHolder) {
            CoverViewHolder viewHolder = (CoverViewHolder) holder;
            if (this.coverList != null && !this.coverList.isEmpty()) {
                viewHolder.main.setVisibility(View.VISIBLE);
                viewHolder.adapter.setDummy(false);
                viewHolder.adapter.setCoverList(coverList);
                viewHolder.adapter.notifyDataSetChanged();
            } else {
                viewHolder.main.setVisibility(View.GONE);
            }
        } else if (holder instanceof SectionViewHolder) {
            SectionViewHolder viewHolder = (SectionViewHolder) holder;
            viewHolder.adapter.setSectionList(this.sectionList);
            viewHolder.adapter.notifyDataSetChanged();
        }
    }


    @Background
    void loadCarouselData(ViewHolder viewHolder, int position, Section section) {
        Pagination pagination = new Pagination(POST_FIRST, POST_SECTION_SIZE, SectionMap.getSectionType(section.getSectionType())[1]);
        PostsListResponse response = (PostsListResponse) this.audioController.posts(SectionMap.getSectionType(section.getSectionType())[0], pagination);
        String handlerResponse = getActivity().responseHandler(response);
        switch (handlerResponse) {
            case "again":
                loadCarouselData(viewHolder, position, section);
                break;
            case "handler":
                if (viewHolder instanceof SectionAdapter.RegularViewHolder) {
                    updateRegularUi((SectionAdapter.RegularViewHolder) viewHolder, response);
                } else if (viewHolder instanceof SectionAdapter.FeaturedViewHolder) {
                    updateFeaturedUi((SectionAdapter.FeaturedViewHolder) viewHolder, section, response);
                }

                break;
        }
    }


    @UiThread
    void updateRegularUi(SectionAdapter.RegularViewHolder viewHolder, PostsListResponse response) {
        viewHolder.adapter.setDummy(false);
        viewHolder.adapter.setSectionItemList(response.getPosts());
        viewHolder.adapter.notifyDataSetChanged();
    }

    @UiThread
    void updateFeaturedUi(SectionAdapter.FeaturedViewHolder viewHolder, final Section section, PostsListResponse response) {


        final String sectionTitle = (String) SectionMap.getSectionTitle(section.getSectionType(), context);
        viewHolder.title.setText(sectionTitle);
        if (response.getPosts().size() <= 10)
            viewHolder.count.setText(String.format(this.context.getString(R.string.books_count1), "" + response.getPosts().size()));
        else
            viewHolder.count.setText(String.format(this.context.getString(R.string.books_count2), "" + response.getPosts().size()));

        try {
            Picasso.with(this.context).load(response.getPosts().get(0).getCover()).error(R.drawable.empty).placeholder(R.drawable.empty).noFade().into(viewHolder.cover1);
        } catch (Exception e) {
        }
        ;
        try {
            Picasso.with(this.context).load(response.getPosts().get(1).getCover()).error(R.drawable.empty).placeholder(R.drawable.empty).noFade().into(viewHolder.cover2);
        } catch (Exception e) {
        }
        ;
        try {
            Picasso.with(this.context).load(response.getPosts().get(2).getCover()).error(R.drawable.empty).placeholder(R.drawable.empty).noFade().into(viewHolder.cover3);
        } catch (Exception e) {
        }
        ;

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreActivity_.intent(context)
                        .title(sectionTitle)
                        .type(SectionMap.getSectionType(section.getSectionType())[0])
                        .sort(SectionMap.getSectionType(section.getSectionType())[1])
                        .id("")
                        .start();
            }
        });
    }

    public int getItemCount() {
        return 2;
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

    public List<Cover> getCoverList() {
        return coverList;
    }

    public void setCoverList(List<Cover> coverList) {
        this.coverList = coverList;
    }

    public BottomSheetLayout getLayout() {
        return layout;
    }

    public void setLayout(BottomSheetLayout layout) {
        this.layout = layout;
    }

    public BaseActivity getActivity() {
        return activity;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public SectionAdapter.OnMenuItemClickListener getMenuItemClickListener() {
        return menuItemClickListener;
    }

    public void setMenuItemClickListener(SectionAdapter.OnMenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
    }

    public void refresh() {
        for (SectionAdapter sectionAdapter : sectionAdapters)
            sectionAdapter.notifyDataSetChanged();
    }
}
