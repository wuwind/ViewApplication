package com.wuwind.corelibrary.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView加载更多的类,支持LinearLayoutManager和GridLayoutManager
 * 使用:
 * RecyclerView.addOnScrollListener(new OnRecyclerLoadMoreListener(){
 * public void onLoadMore(){...}
 * });
 */
public abstract class OnRecyclerLoadMoreListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager mLayoutManager;
    private int mItemCount, mLastCompletely, mLastVisisable;

    private View view;
    private boolean isShown;

    /**
     * load more
     */
    public abstract void onLoadMore();

    public OnRecyclerLoadMoreListener(View view) {
        this.view = view;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            mItemCount = mLayoutManager.getItemCount();  //总条数
            mLastVisisable = mLayoutManager.findLastVisibleItemPosition();
        } else {
            return;
        }
        if (!isShown && mItemCount == mLastVisisable + 1) {
            onLoadMore();
            showView();
            isShown = true;
        } else if(isShown && mItemCount - 1 > mLastVisisable){
            dismissView();
            isShown = false;
        }
    }

    private void showView() {
        if (view != null && !view.isShown()) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void dismissView() {
        if (view != null && view.isShown()) {
            view.setVisibility(View.GONE);
        }
    }
}  
