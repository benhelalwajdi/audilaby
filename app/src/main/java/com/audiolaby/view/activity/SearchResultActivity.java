package com.audiolaby.view.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.audiolaby.R;
import com.audiolaby.controller.AudioController;
import com.audiolaby.controller.enumeration.SortField;
import com.audiolaby.controller.model.Pagination;
import com.audiolaby.controller.response.PostsListResponse;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.User;
import com.audiolaby.view.adapter.AudioPostAdapter;
import com.audiolaby.view.adapter.decoration.EndlessRecyclerOnScrollListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

import static com.audiolaby.Constants.POST_FIRST;
import static com.audiolaby.Constants.POST_MORE_SIZE;

@EActivity(R.layout.activity_more)
public class SearchResultActivity extends BasePlayerActivity {


    @Bean
    AudioController audioController;

    @Extra
    String query;
    @Extra
    String sort;
    @Extra
    String type;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.refresh)
    SwipeRefreshLayout refresh;
    @ViewById(R.id.list)
    RecyclerView list;
    @ViewById(R.id.title)
    TextView title_;

    Boolean isSorted = false;
    Boolean isRefresh = false;

    User user;
    AudioPostAdapter adapter;
    EndlessScrollListener endlessScrollListener;

    @AfterViews
    void afterViewsInjection() {

        isSorted = true;
        sort = SortField.RELEASE_DATE_ASC.name();

        setSupportActionBar(this.toolbar);
        super.afterViewsInjection();
        String titleString = this.query != null ? this.query : StringUtils.EMPTY;
        title_.setText(String.format(getString(R.string.search_result), titleString));
        this.user = this.libraryDAO.getUser();
        this.adapter = new AudioPostAdapter(SearchResultActivity.this, this.layout, false, isSorted, new ArrayList());
        this.adapter.setLibraryDAO(this.libraryDAO);
        //  this.adapter.setMenuItemClickListener(new C07772());
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        if (isSorted) {
            this.adapter.setSortList(Arrays.asList(getResources().getStringArray(R.array.sort_discover)));
            this.adapter.setFilterAndSortListener(new filterAndSortListener());
            //this.adapter.setFilterList(Arrays.asList(getResources().getStringArray(R.array.sort_discover)));
            manager.setSpanSizeLookup(new spanSizeLookup(2));
        }
        new EndlessScrollListener(manager);
        this.list.setLayoutManager(manager);
        this.list.setAdapter(new SlideInBottomAnimatorAdapter(this.adapter, this.list));
        this.list.scrollToPosition(1);
        refresh.setOnRefreshListener(new refreshLListener());
        endlessScrollListener = new EndlessScrollListener(manager);
        this.list.addOnScrollListener(endlessScrollListener);


        loadPosts(POST_FIRST);
    }


    @Background(id = "longtask")
    void loadPosts(final int page) {
        if (!isRefresh)
            showProgress();
        isRefresh = false;
        Pagination pagination = new Pagination(page, POST_MORE_SIZE, sort, query);
        PostsListResponse response = (PostsListResponse) this.audioController.posts(type, pagination);
        String handlerResponse = responseHandler(response);
        dimissProgress();
        switch (handlerResponse) {
            case "again":
                loadPosts(page);
                break;
            case "handler":
                if (response.getPosts() != null && !response.getPosts().isEmpty())
                    updateList(page, response.getPosts());
                break;
            default:
                showError(handlerResponse);
        }
    }

    @UiThread
    <T> void updateList(int page, List<AudioArticle> postList) {
        refresh.setRefreshing(false);


        this.adapter.setDummy(false);

        if (page == POST_FIRST) {
            endlessScrollListener.reset();
            this.adapter.setAudioArticles(postList);
            this.adapter.notifyDataSetChanged();
            this.list.scrollToPosition(1);
        } else {
            List<AudioArticle> posts = this.adapter.getAudioArticles();
            posts.addAll(postList);
            this.adapter.setAudioArticles(posts);
            this.adapter.notifyDataSetChanged();
        }
    }


    class EndlessScrollListener extends EndlessRecyclerOnScrollListener {
        EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
            super(linearLayoutManager);
        }

        public void onLoadMore(int currentPage) {
            loadPosts(currentPage);
        }
    }

    class refreshLListener implements SwipeRefreshLayout.OnRefreshListener {
        refreshLListener() {
        }

        @Override
        public void onRefresh() {
            isRefresh = true;
            loadPosts(POST_FIRST);
        }

    }

    class spanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        final int columns;

        spanSizeLookup(int i) {
            this.columns = i;
        }

        public int getSpanSize(int position) {
            if (position == 0) {
                return this.columns;
            }
            return 1;
        }
    }

    class filterAndSortListener implements AudioPostAdapter.FilterAndSortListener {
        filterAndSortListener() {
        }

        @Override
        public boolean onFilterSelected(Object obj, int i) {
            return false;
        }

        public boolean onSortSelected(Object object, int index) {
            switch (index) {
                case 0:
                    sort = SortField.RELEASE_DATE_ASC.name();
                    break;
                case 1:
                    sort = SortField.RELEASE_DATE_DESC.name();
                    break;
                case 2:
                    sort = SortField.ALPHABETICALLY.name();
                    break;
                case 3:
                    sort = SortField.HIGHEST_RATED.name();
                    break;
                case 4:
                    sort = SortField.MOST_POPULAR.name();
                    break;
            }

            loadPosts(POST_FIRST);
            return true;
        }
    }


}
