package com.audiolaby.view.fragment;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.audiolaby.R;
import com.audiolaby.controller.CatalogController;
import com.audiolaby.controller.enumeration.SectionType;
import com.audiolaby.controller.response.CoversResponse;
import com.audiolaby.controller.response.SectionListResponse;
import com.audiolaby.otto.events.RefreshPostEvent;
import com.audiolaby.otto.events.UserLoggedInEvent;
import com.audiolaby.persistence.model.Cover;
import com.audiolaby.persistence.model.Section;
import com.audiolaby.util.Utils;
import com.audiolaby.view.activity.MainActivity;
import com.audiolaby.view.activity.MoreActivity;
import com.audiolaby.view.activity.SignInActivity;
import com.audiolaby.view.adapter.BrowseAdapter;
import com.audiolaby.view.adapter.SectionAdapter;
import com.google.android.gms.auth.api.Auth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.audiolaby.Constants.POST_FIRST;

@EFragment(R.layout.fragment_browse)
public class BrowseFragment extends BaseFragment {
    @Bean
    BrowseAdapter adapter;
    @Bean
    CatalogController catalogController;
    @ViewById(R.id.list)
    RecyclerView list;
    @ViewById(R.id.refresh)
    SwipeRefreshLayout refresh;
    Boolean isRefresh = false;
    Boolean first = true;


    Handler handler = new Handler();

    Boolean isLoaded = false;

    @AfterViews
    void afterViewsInjection() {
        this.adapter.setActivity((MainActivity) getActivity());
        this.adapter.setLayout(((MainActivity) getActivity()).getLayout());
        this.adapter.setMenuItemClickListener(new onMenuItemClickListener());
        this.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.list.setAdapter(this.adapter);
        refresh.setOnRefreshListener(new refreshLListener());

        loadCovers();
        loadSections();
    }


    @Background
    void loadSections() {
        if (!isFragmentBeyingDiscarded()) {
//            if (!isRefresh)
//                showProgress();
            isRefresh = false;
            SectionListResponse response = (SectionListResponse) this.catalogController.sections();
            String handlerResponse = responseHandler(response);
            switch (handlerResponse) {
                case "again":
                    loadSections();
                    break;
                case "handler":
                    if (!response.getSections().isEmpty())
                        updateUi(response.getSections());
                    else
                        Log.i("", "");
                    break;
                default:
                    showError(handlerResponse);
            }
        }
    }

    @UiThread
    void updateUi(List<Section> sectionList) {
        refresh.setRefreshing(false);
        this.adapter.setDummy(false);
        for (Iterator<Section> section = sectionList.iterator(); section.hasNext(); ) {
            Section section0 = section.next();
            if (!section0.getActive()) {
                section.remove();
            }

        }

        this.adapter.setSectionList(sectionList);
        this.adapter.notifyDataSetChanged();
    }


    @Background
    void loadCovers() {
        if (!isFragmentBeyingDiscarded()) {
            CoversResponse response = (CoversResponse) this.catalogController.covers();
            String handlerResponse = responseHandler(response);
            switch (handlerResponse) {
                case "again":
                    loadSections();
                    break;
                case "handler":
                    updateCovers(response);
                    break;
                default:
                    showError(handlerResponse);
            }
        }
    }

    @UiThread
    void updateCovers(CoversResponse response) {
        if (first) {
            first = false;

            Cover cover = new Cover();
            cover.setDonwloads(response.getN_downloads());
            cover.setViews(response.getN_views());
            cover.setStat(true);
            response.getCovers().add(0, cover);

            isLoaded=true;

            this.adapter.setCoverList(response.getCovers());
            this.adapter.notifyDataSetChanged();
        } else {

            if (this.adapter.getCoverList().get(0).getDonwloads() != response.getN_downloads()
                    ||
                    this.adapter.getCoverList().get(0).getViews() != response.getN_views()) {

                this.adapter.getCoverList().get(0).setDonwloads(response.getN_downloads());
                this.adapter.getCoverList().get(0).setViews(response.getN_views());
                this.adapter.notifyItemChanged(0);
            }

        }


        handler.postDelayed(new Runnable() {
            public void run() {
                loadCovers();

            }
        }, 60000);
    }

    class onMenuItemClickListener implements SectionAdapter.OnMenuItemClickListener {
        onMenuItemClickListener() {
        }

        public boolean onMenuItemClick(MenuItem item, Object object, Section section, Integer sectionIndex) {
            return onMenuItemClick(item.getItemId(), object, section, sectionIndex);
        }

        public boolean onMenuItemClick(int itemId, Object object, Section section, Integer sectionIndex) {
//            switch (itemId) {
//                case R.id.add_wishlist /*2131755230*/:
//                    BrowseFragment.this.addToWishList((Audiobook) object, section, sectionIndex);
//                    return true;
//                case R.id.remove_wishlist /*2131755231*/:
//                    BrowseFragment.this.removeFromWishList((Audiobook) object, section, sectionIndex);
//                    return true;
//                case R.id.download_to_device /*2131755398*/:
//                case R.id.mark_as_finished /*2131755619*/:
//                    BrowseFragment.this.markAs((Audiobook) object, true);
//                    return true;
//                case R.id.mark_as_unfinished /*2131755620*/:
//                    BrowseFragment.this.markAs((Audiobook) object, false);
//                    return true;
//                case R.id.purchase_with_credit /*2131755626*/:
//                    BrowseFragment.this.purchaseAudiobook((Audiobook) object, section.getSectionType(), sectionIndex);
//                    return true;
//            }
            return false;
        }
    }

    class refreshLListener implements SwipeRefreshLayout.OnRefreshListener {
        refreshLListener() {
        }

        @Override
        public void onRefresh() {
            isRefresh = true;
            first = true;
            loadCovers();
            loadSections();
        }

    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onRefreshPost(RefreshPostEvent event) {
        adapter.refresh();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @UiThread
    void showError(String message) {
        Snackbar.make(this.list, (CharSequence) message, Snackbar.LENGTH_LONG).show();
    }
}
