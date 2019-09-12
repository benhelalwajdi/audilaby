package com.audiolaby.view.adapter.decoration;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

public abstract class EndlessRecyclerOnScrollListener extends OnScrollListener {
    private int currentPage;
    private int firstVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    private boolean loading;
    private int previousTotal;
    private int totalItemCount;
    private int visibleItemCount;
    private int visibleThreshold;

    public abstract void onLoadMore(int i);

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.previousTotal = 0;
        this.loading = true;
        this.visibleThreshold = 5;
        this.currentPage = 1;
        this.linearLayoutManager = linearLayoutManager;
    }

    public void reset()
    {
        this.previousTotal = 0;
        this.loading = true;
        this.visibleThreshold = 5;
        this.currentPage = 1;
        this.linearLayoutManager = linearLayoutManager;
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        this.visibleItemCount = recyclerView.getChildCount();
        this.totalItemCount = this.linearLayoutManager.getItemCount();
        this.firstVisibleItem = this.linearLayoutManager.findFirstVisibleItemPosition();
        if (this.loading && this.totalItemCount > this.previousTotal) {
            this.loading = false;
            this.previousTotal = this.totalItemCount;
        }
        if (!this.loading && this.totalItemCount - this.visibleItemCount <= this.firstVisibleItem + this.visibleThreshold) {
            this.currentPage++;
            onLoadMore(this.currentPage);
            this.loading = true;
        }
    }
}
