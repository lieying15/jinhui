package com.thlh.viewlib.easyrecyclerview.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by zhy on 16/6/23.
 */
public class LoadMoreWrapper<T> extends EasyRecyclerViewAdapter
{
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private EasyRecyclerViewAdapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;
    private boolean isShowView = true;

    public LoadMoreWrapper(EasyRecyclerViewAdapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean hasLoadMore()
    {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0;
    }


    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount()) &&isShowView;
    }


    @Override
    public int[] getItemLayouts() {
        return mInnerAdapter.getItemLayouts();
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, int position) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(viewHolder, position);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            EasyRecyclerViewHolder holder;
            if (mLoadMoreView != null) {
                holder = EasyRecyclerViewHolder.createViewHolder(mLoadMoreView);
            } else {
                holder = EasyRecyclerViewHolder.createViewHolder(parent.getContext(), parent, mLoadMoreLayoutId);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }


//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView)
//    {
//        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback()
//        {
//            @Override
//            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position)
//            {
//                if (isShowLoadMore(position))
//                {
//                    return layoutManager.getSpanCount();
//                }
//                if (oldLookup != null)
//                {
//                    return oldLookup.getSpanSize(position);
//                }
//                return 1;
//            }
//        });
//    }


//    @Override
//    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
//    {
//        mInnerAdapter.onViewAttachedToWindow(holder);
//
//        if (isShowLoadMore(holder.getLayoutPosition()))
//        {
//            setFullSpan(holder);
//        }
//    }
//
//    private void setFullSpan(RecyclerView.ViewHolder holder)
//    {
//        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//
//        if (lp != null
//                && lp instanceof StaggeredGridLayoutManager.LayoutParams)
//        {
//            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//
//            p.setFullSpan(true);
//        }
//    }

    @Override
    public int getItemCount()
    {
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }


    public interface OnLoadMoreListener
    {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener)
    {
        if (loadMoreListener != null)
        {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(View loadMoreView)
    {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(int layoutId)
    {
        mLoadMoreLayoutId = layoutId;
        return this;
    }

    public void setShowView(boolean isShowView){
        this.isShowView = isShowView;
    }
}
