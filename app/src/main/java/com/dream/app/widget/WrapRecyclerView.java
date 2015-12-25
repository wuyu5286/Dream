package com.dream.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.dream.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/23.
 * ============================================================
 */
public class WrapRecyclerView extends RecyclerView {
    public static final int LAYOUT_MANAGER_TYPE_LINEAR = 0;
    public static final int LAYOUT_MANAGER_TYPE_GRID = 1;
    public static final int LAYOUT_MANAGER_TYPE_STAGGERED_GRID = 2;

    private static final int DEF_LAYOUT_MANAGER_TYPE = LAYOUT_MANAGER_TYPE_LINEAR;
    private static final int DEF_GRID_SPAN_COUNT = 2;
    private static final int DEF_LAYOUT_MANAGER_ORIENTATION = OrientationHelper.VERTICAL;
    private static final int DEF_DIVIDER_HEIGHT = 1;

    private List<View> mHeaderView = new ArrayList<>();
    private List<View> mFooterView = new ArrayList<>();
    private WrapRecyclerViewAdapter mWrapRecyclerViewAdapter;
    private Adapter mReqAdapter;
    private GridLayoutManager mCurGridLayoutManager;
    private WrapDefaultItemDecoration mWrapDefaultItemDecoration;

    private Drawable mVerticalDivider;
    private Drawable mHorizontalDivider;
    private int mVerticalDividerHeight;
    private int mHorizontalDividerHeight;
    private int mItemViewBothSidesMargin;
    private boolean isHeaderDividersEnabled = false;
    private boolean isFooterDividersEnabled = false;
    private boolean isDefaultItemDecoration = true;
    private boolean isKeepShowHeadOrFooter = false;
    private int mEmptyViewResId;
    private View mEmptyView;
    private OnItemClickListener mTempOnItemClickListener;
    private OnItemLongClickListener mTempOnItemLongClickListener;
    private int mLayoutManagerType;

    private boolean hasShowEmptyView = false;

    public WrapRecyclerView(Context context) {
        this(context, null);
    }

    public WrapRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WrapRecyclerView);
        Drawable divider = ta.getDrawable(R.styleable.WrapRecyclerView_frv_divider);
        int dividerHeight = (int) ta.getDimension(R.styleable.WrapRecyclerView_frv_dividerHeight, -1);
        mVerticalDivider = ta.getDrawable(R.styleable.WrapRecyclerView_frv_dividerVertical);
        mHorizontalDivider = ta.getDrawable(R.styleable.WrapRecyclerView_frv_dividerHorizontal);
        mVerticalDividerHeight = (int) ta.getDimension(R.styleable.WrapRecyclerView_frv_dividerVerticalHeight, -1);
        mHorizontalDividerHeight = (int) ta.getDimension(R.styleable.WrapRecyclerView_frv_dividerHorizontalHeight, -1);
        mItemViewBothSidesMargin = (int) ta.getDimension(R.styleable.WrapRecyclerView_frv_itemViewBothSidesMargin, 0);
        mEmptyViewResId = ta.getResourceId(R.styleable.WrapRecyclerView_frv_emptyView, -1);
        isKeepShowHeadOrFooter = ta.getBoolean(R.styleable.WrapRecyclerView_frv_isEmptyViewKeepShowHeadOrFooter, false);
        isHeaderDividersEnabled = ta.getBoolean(R.styleable.WrapRecyclerView_frv_headerDividersEnabled, false);
        isFooterDividersEnabled = ta.getBoolean(R.styleable.WrapRecyclerView_frv_footerDividersEnabled, false);
        if (ta.hasValue(R.styleable.WrapRecyclerView_frv_layoutManager)) {
            int layoutManagerType = ta.getInt(R.styleable.WrapRecyclerView_frv_layoutManager, DEF_LAYOUT_MANAGER_TYPE);
            int layoutManagerOrientation = ta.getInt(R.styleable.WrapRecyclerView_frv_layoutManagerOrientation, DEF_LAYOUT_MANAGER_ORIENTATION);
            boolean isReverseLayout = ta.getBoolean(R.styleable.WrapRecyclerView_frv_isReverseLayout, false);
            int gridSpanCount = ta.getInt(R.styleable.WrapRecyclerView_frv_spanCount, DEF_GRID_SPAN_COUNT);

            switch (layoutManagerType) {
                case LAYOUT_MANAGER_TYPE_LINEAR:
                    processGridDivider(divider, dividerHeight, false, layoutManagerOrientation);
                    setLayoutManager(new LinearLayoutManager(context, layoutManagerOrientation, isReverseLayout));
                    break;
                case LAYOUT_MANAGER_TYPE_GRID:
                    processGridDivider(divider, dividerHeight, false, layoutManagerOrientation);
                    setLayoutManager(new GridLayoutManager(context, gridSpanCount, layoutManagerOrientation, isReverseLayout));
                    break;
                case LAYOUT_MANAGER_TYPE_STAGGERED_GRID:
                    processGridDivider(divider, dividerHeight, false, layoutManagerOrientation);
                    setLayoutManager(new StaggeredGridLayoutManager(gridSpanCount, layoutManagerOrientation));
                    break;
            }
        }
        ta.recycle();
    }

    private void processGridDivider(Drawable divider, int dividerHeight, boolean isLinearLayoutManager, int layoutManagerOrientation) {
        if ((null == mVerticalDivider || null == mHorizontalDivider) && null != divider) {
            if (isLinearLayoutManager) {
                if (layoutManagerOrientation == OrientationHelper.VERTICAL && null == mHorizontalDivider) {
                    mHorizontalDivider = divider;
                } else if (layoutManagerOrientation == OrientationHelper.HORIZONTAL && null == mVerticalDivider) {
                    mVerticalDivider = divider;
                }
            } else {
                if (null == mVerticalDivider) {
                    mVerticalDivider = divider;
                }

                if (null == mHorizontalDivider) {
                    mHorizontalDivider = divider;
                }
            }
        }

        if (mVerticalDividerHeight > 0 && mHorizontalDividerHeight > 0) return;

        if (dividerHeight > 0) {
            if (isLinearLayoutManager) {
                if (layoutManagerOrientation == OrientationHelper.VERTICAL && mHorizontalDividerHeight <= 0) {
                    mHorizontalDividerHeight = dividerHeight;
                } else if (layoutManagerOrientation == OrientationHelper.HORIZONTAL && mVerticalDividerHeight <= 0) {
                    mVerticalDividerHeight = dividerHeight;
                }
            } else {
                if (mVerticalDividerHeight <= 0) {
                    mVerticalDividerHeight = dividerHeight;
                }

                if (mHorizontalDividerHeight <= 0) {
                    mHorizontalDividerHeight = dividerHeight;
                }
            }
        } else {
            if (isLinearLayoutManager) {
                if (layoutManagerOrientation == OrientationHelper.VERTICAL && mHorizontalDividerHeight <= 0) {
                    if (null != mHorizontalDivider) {
                        if (mHorizontalDivider.getIntrinsicHeight() > 0) {
                            mHorizontalDividerHeight = mHorizontalDivider.getIntrinsicHeight();
                        } else {
                            mHorizontalDividerHeight = DEF_DIVIDER_HEIGHT;
                        }
                    }
                } else if (layoutManagerOrientation == OrientationHelper.HORIZONTAL && mVerticalDividerHeight <= 0) {
                    if (null != mVerticalDivider) {
                        if (mVerticalDivider.getIntrinsicHeight() > 0) {
                            mVerticalDividerHeight = mVerticalDivider.getIntrinsicHeight();
                        } else {
                            mVerticalDividerHeight = DEF_DIVIDER_HEIGHT;
                        }
                    }
                }
            } else {
                if (mVerticalDividerHeight <= 0 && null != mVerticalDivider) {
                    if (mVerticalDivider.getIntrinsicHeight() > 0) {
                        mVerticalDividerHeight = mVerticalDivider.getIntrinsicHeight();
                    } else {
                        mVerticalDividerHeight = DEF_DIVIDER_HEIGHT;
                    }
                }

                if (mHorizontalDividerHeight <= 0 && null != mHorizontalDivider) {
                    if (mHorizontalDivider.getIntrinsicHeight() > 0) {
                        mHorizontalDividerHeight = mHorizontalDivider.getIntrinsicHeight();
                    } else {
                        mHorizontalDividerHeight = DEF_DIVIDER_HEIGHT;
                    }
                }
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        // Only once
        if (mEmptyViewResId != -1) {
            if (null != getParent()) {
                ViewGroup parentView = ((ViewGroup) getParent());
                View tempEmptyView1 = parentView.findViewById(mEmptyViewResId);

                if (null != tempEmptyView1) {
                    mEmptyView = tempEmptyView1;

                    if (isKeepShowHeadOrFooter) parentView.removeView(tempEmptyView1);
                } else {
                    ViewParent pParentView = parentView.getParent();
                    if (null != pParentView && pParentView instanceof ViewGroup) {
                        View tempEmptyView2 = ((ViewGroup) pParentView).findViewById(mEmptyViewResId);
                        if (null != tempEmptyView2) {
                            mEmptyView = tempEmptyView2;

                            if (isKeepShowHeadOrFooter)
                                ((ViewGroup) pParentView).removeView(tempEmptyView2);
                        }
                    }
                }
            }
            mEmptyViewResId = -1;
        } else if (isKeepShowHeadOrFooter && null != mEmptyView) {
            ((ViewGroup) mEmptyView.getParent()).removeView(mEmptyView);
        }

        if (null == adapter) {
            if (null != mReqAdapter) {
                if (!isKeepShowHeadOrFooter) {
                    mReqAdapter.unregisterAdapterDataObserver(mReqAdapterDataObserver);
                }
                mReqAdapter = null;
                mWrapRecyclerViewAdapter = null;

                processEmptyView();
            }

            return;
        }

        mReqAdapter = adapter;
        mWrapRecyclerViewAdapter = new WrapRecyclerViewAdapter(this, adapter, mHeaderView, mFooterView, mLayoutManagerType);

        mWrapRecyclerViewAdapter.setOnItemClickListener(mTempOnItemClickListener);
        mWrapRecyclerViewAdapter.setOnItemLongClickListener(mTempOnItemLongClickListener);

        mReqAdapter.registerAdapterDataObserver(mReqAdapterDataObserver);
        super.setAdapter(mWrapRecyclerViewAdapter);

        processEmptyView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (null != mReqAdapter) {
            mReqAdapter.unregisterAdapterDataObserver(mReqAdapterDataObserver);
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (null == layout) return;

        if (layout instanceof GridLayoutManager) {
            mCurGridLayoutManager = ((GridLayoutManager) layout);
            mCurGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position < getHeaderViewsCount() || position >= mReqAdapter.getItemCount() + getHeaderViewsCount()) {
                        // header or footer span
                        return mCurGridLayoutManager.getSpanCount();
                    } else {
                        // default item span
                        return 1;
                    }
                }
            });

            mLayoutManagerType = LAYOUT_MANAGER_TYPE_GRID;
            setDivider(mVerticalDivider, mHorizontalDivider);
        } else if (layout instanceof StaggeredGridLayoutManager) {
            mLayoutManagerType = LAYOUT_MANAGER_TYPE_STAGGERED_GRID;
            setDivider(mVerticalDivider, mHorizontalDivider);
        } else if (layout instanceof LinearLayoutManager) {
            mLayoutManagerType = LAYOUT_MANAGER_TYPE_LINEAR;
            if (((LinearLayoutManager) layout).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                setDividerVertical(mVerticalDivider);
            } else {
                setDividerHorizontal(mHorizontalDivider);
            }
        }
    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        if (null == decor) return;

        // remove default ItemDecoration
        if (null != mWrapDefaultItemDecoration) {
            removeItemDecoration(mWrapDefaultItemDecoration);
            mWrapDefaultItemDecoration = null;
        }

        isDefaultItemDecoration = false;

        super.addItemDecoration(decor);
    }

    private void addDefaultItemDecoration() {
        if (null != mWrapDefaultItemDecoration) {
            removeItemDecoration(mWrapDefaultItemDecoration);
            mWrapDefaultItemDecoration = null;
        }

        mWrapDefaultItemDecoration = new WrapDefaultItemDecoration(this, mVerticalDivider, mHorizontalDivider, mVerticalDividerHeight, mHorizontalDividerHeight);
        mWrapDefaultItemDecoration.setItemViewBothSidesMargin(mItemViewBothSidesMargin);
        mWrapDefaultItemDecoration.setHeaderDividersEnabled(isHeaderDividersEnabled);
        mWrapDefaultItemDecoration.setFooterDividersEnabled(isFooterDividersEnabled);
        super.addItemDecoration(mWrapDefaultItemDecoration);
    }

    private void processEmptyView() {
        if (null != mEmptyView) {
            boolean isShowEmptyView = (null != mReqAdapter ? mReqAdapter.getItemCount() : 0) == 0;

            if (isShowEmptyView == hasShowEmptyView) return;

            if (isKeepShowHeadOrFooter) {
                if (hasShowEmptyView) {
                    mWrapRecyclerViewAdapter.notifyItemRemoved(getHeaderViewsCount());
                }
            } else {
                mEmptyView.setVisibility(isShowEmptyView ? VISIBLE : GONE);
                setVisibility(isShowEmptyView ? GONE : VISIBLE);
            }

            this.hasShowEmptyView = isShowEmptyView;
        }
    }

    /**
     * 设置空布局(在setAdapter之前)
     */
    public void setEmptyView(View emptyView) {
        setEmptyView(emptyView, false);
    }

    /**
     * 设置空布局(在setAdapter之前)
     *
     * @param isKeepShowHeadOrFooter 是否显示头部和尾部
     */
    public void setEmptyView(View emptyView, boolean isKeepShowHeadOrFooter) {
        this.mEmptyView = emptyView;
        this.isKeepShowHeadOrFooter = isKeepShowHeadOrFooter;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyViewKeepShowHeadOrFooter(boolean isKeepShowHeadOrFoot) {
        this.isKeepShowHeadOrFooter = isKeepShowHeadOrFoot;
    }

    public boolean isShowEmptyView() {
        return hasShowEmptyView;
    }

    public boolean isKeepShowHeadOrFooter() {
        return isKeepShowHeadOrFooter;
    }

    public void setDivider(Drawable divider) {
        if (!isDefaultItemDecoration || (mVerticalDividerHeight <= 0 && mHorizontalDividerHeight <= 0))
            return;

        if (this.mVerticalDivider != divider) {
            this.mVerticalDivider = divider;
        }

        if (this.mHorizontalDivider != divider) {
            this.mHorizontalDivider = divider;
        }

        if (null == mWrapDefaultItemDecoration) {
            addDefaultItemDecoration();
        } else {
            mWrapDefaultItemDecoration.setVerticalDividerDrawable(mVerticalDivider);
            mWrapDefaultItemDecoration.setHorizontalDividerDrawable(mHorizontalDivider);

            if (null != mWrapRecyclerViewAdapter) {
                mWrapRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setDivider(Drawable dividerVertical, Drawable dividerHorizontal) {
        if (!isDefaultItemDecoration || (mVerticalDividerHeight <= 0 && mHorizontalDividerHeight <= 0))
            return;

        if (this.mVerticalDivider != dividerVertical) {
            this.mVerticalDivider = dividerVertical;
        }

        if (this.mHorizontalDivider != dividerHorizontal) {
            this.mHorizontalDivider = dividerHorizontal;
        }

        if (null == mWrapDefaultItemDecoration) {
            addDefaultItemDecoration();
        } else {
            mWrapDefaultItemDecoration.setVerticalDividerDrawable(mVerticalDivider);
            mWrapDefaultItemDecoration.setHorizontalDividerDrawable(mHorizontalDivider);

            if (null != mWrapRecyclerViewAdapter) {
                mWrapRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setDividerVertical(Drawable dividerVertical) {
        if (!isDefaultItemDecoration || mVerticalDividerHeight <= 0) return;

        if (this.mVerticalDivider != dividerVertical) {
            this.mVerticalDivider = dividerVertical;
        }

        if (null == mWrapDefaultItemDecoration) {
            addDefaultItemDecoration();
        } else {
            mWrapDefaultItemDecoration.setVerticalDividerDrawable(mVerticalDivider);

            if (null != mWrapRecyclerViewAdapter) {
                mWrapRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setDividerHorizontal(Drawable dividerHorizontal) {
        if (!isDefaultItemDecoration || mHorizontalDividerHeight <= 0) return;

        if (this.mHorizontalDivider != dividerHorizontal) {
            this.mHorizontalDivider = dividerHorizontal;
        }

        if (null == mWrapDefaultItemDecoration) {
            addDefaultItemDecoration();
        } else {
            mWrapDefaultItemDecoration.setHorizontalDividerDrawable(mHorizontalDivider);

            if (null != mWrapRecyclerViewAdapter) {
                mWrapRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setDividerHeight(int height) {
        this.mVerticalDividerHeight = height;
        this.mHorizontalDividerHeight = height;

        if (isDefaultItemDecoration && null != mWrapDefaultItemDecoration) {
            mWrapDefaultItemDecoration.setVerticalDividerDrawableHeight(mVerticalDividerHeight);
            mWrapDefaultItemDecoration.setHorizontalDividerDrawableHeight(mHorizontalDividerHeight);

            if (null != mWrapRecyclerViewAdapter) {
                mWrapRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setDividerVerticalHeight(int height) {
        this.mVerticalDividerHeight = height;

        if (isDefaultItemDecoration && null != mWrapDefaultItemDecoration) {
            mWrapDefaultItemDecoration.setVerticalDividerDrawableHeight(mVerticalDividerHeight);

            if (null != mWrapRecyclerViewAdapter) {
                mWrapRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setDividerHorizontalHeight(int height) {
        this.mHorizontalDividerHeight = height;

        if (isDefaultItemDecoration && null != mWrapDefaultItemDecoration) {
            mWrapDefaultItemDecoration.setHorizontalDividerDrawableHeight(mHorizontalDividerHeight);

            if (null != mWrapRecyclerViewAdapter) {
                mWrapRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addHeaderView(View v) {
        addHeaderView(v, false);
    }

    public void addHeaderView(View v, boolean isScrollTo) {
        if (mHeaderView.contains(v)) return;

        mHeaderView.add(v);
        if (null != mWrapRecyclerViewAdapter) {
            int pos = mHeaderView.size() - 1;
            mWrapRecyclerViewAdapter.notifyItemInserted(pos);

            if (isScrollTo) {
                scrollToPosition(pos);
            }
        }
    }

    public boolean removeHeaderView(View v) {
        if (!mHeaderView.contains(v)) return false;

        if (null != mWrapRecyclerViewAdapter) {
            mWrapRecyclerViewAdapter.notifyItemRemoved(mHeaderView.indexOf(v));
        }
        return mHeaderView.remove(v);
    }

    public void addFooterView(View v) {
        addFooterView(v, false);
    }

    public void addFooterView(View v, boolean isScrollTo) {
        if (mFooterView.contains(v)) return;

        mFooterView.add(v);
        if (null != mWrapRecyclerViewAdapter) {
            int pos = (null == mReqAdapter ? 0 : mReqAdapter.getItemCount()) + getHeaderViewsCount() + mFooterView.size() - 1;
            mWrapRecyclerViewAdapter.notifyItemInserted(pos);
            if (isScrollTo) {
                scrollToPosition(pos);
            }
        }
    }

    public boolean removeFooterView(View v) {
        if (!mFooterView.contains(v)) return false;

        if (null != mWrapRecyclerViewAdapter) {
            int pos = (null == mReqAdapter ? 0 : mReqAdapter.getItemCount()) + getHeaderViewsCount() + mFooterView.indexOf(v);
            mWrapRecyclerViewAdapter.notifyItemRemoved(pos);
        }
        return mFooterView.remove(v);
    }

    public int getHeaderViewsCount() {
        return mHeaderView.size();
    }

    public int getFooterViewsCount() {
        return mFooterView.size();
    }

    public int getFirstVisiblePosition() {
        LayoutManager layoutManager = getLayoutManager();

        if (null == layoutManager) return 0;

        int ret = -1;

        switch (mLayoutManagerType) {
            case LAYOUT_MANAGER_TYPE_LINEAR:
                ret = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() - getHeaderViewsCount();
                break;
            case LAYOUT_MANAGER_TYPE_GRID:
                ret = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() - getHeaderViewsCount();
                break;
            case LAYOUT_MANAGER_TYPE_STAGGERED_GRID:
                StaggeredGridLayoutManager tempStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] firstVisibleItemPositions = new int[tempStaggeredGridLayoutManager.getSpanCount()];
                tempStaggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(firstVisibleItemPositions);
                ret = firstVisibleItemPositions[0] - getHeaderViewsCount();
                break;
        }

        return ret < 0 ? 0 : ret;
    }

    public int getLastVisiblePosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (null == layoutManager) return -1;

        int curItemCount = (null != mReqAdapter ? mReqAdapter.getItemCount() - 1 : 0);
        int ret = -1;

        switch (mLayoutManagerType) {
            case LAYOUT_MANAGER_TYPE_LINEAR:
                ret = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() - getHeaderViewsCount();
                if (ret > curItemCount) {
                    ret -= getFooterViewsCount();
                }
                break;
            case LAYOUT_MANAGER_TYPE_GRID:
                ret = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() - getHeaderViewsCount();
                if (ret > curItemCount) {
                    ret -= getFooterViewsCount();
                }
                break;
            case LAYOUT_MANAGER_TYPE_STAGGERED_GRID:
                StaggeredGridLayoutManager tempStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] lastVisibleItemPositions = new int[tempStaggeredGridLayoutManager.getSpanCount()];
                tempStaggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastVisibleItemPositions);
                if (lastVisibleItemPositions.length > 0) {
                    int maxPos = lastVisibleItemPositions[0];
                    for (int curPos : lastVisibleItemPositions) {
                        if (curPos > maxPos) maxPos = curPos;
                    }
                    ret = maxPos - getHeaderViewsCount();
                    if (ret > curItemCount) {
                        ret -= getFooterViewsCount();
                    }
                }
                break;
        }

        return ret < 0 ? (null != mReqAdapter ? mReqAdapter.getItemCount() - 1 : 0) : ret;
    }

    public void setHeaderDividersEnabled(boolean isHeaderDividersEnabled) {
        this.isHeaderDividersEnabled = isHeaderDividersEnabled;
        if (isDefaultItemDecoration && null != mWrapDefaultItemDecoration) {
            mWrapDefaultItemDecoration.setHeaderDividersEnabled(isHeaderDividersEnabled);

            if (null != mWrapRecyclerViewAdapter) {
                mWrapRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setFooterDividersEnabled(boolean isFooterDividersEnabled) {
        this.isFooterDividersEnabled = isFooterDividersEnabled;
        if (isDefaultItemDecoration && null != mWrapDefaultItemDecoration) {
            mWrapDefaultItemDecoration.setFooterDividersEnabled(isFooterDividersEnabled);

            if (null != mWrapRecyclerViewAdapter) {
                mWrapRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        if (null == mWrapRecyclerViewAdapter) {
            mTempOnItemClickListener = listener;
        } else {
            mWrapRecyclerViewAdapter.setOnItemClickListener(listener);
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        if (null == mWrapRecyclerViewAdapter) {
            mTempOnItemLongClickListener = listener;
        } else {
            mWrapRecyclerViewAdapter.setOnItemLongClickListener(listener);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(WrapRecyclerView WrapRecyclerView, View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(WrapRecyclerView WrapRecyclerView, View view, int position);
    }

    private AdapterDataObserver mReqAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapRecyclerViewAdapter.notifyDataSetChanged();
            processEmptyView();
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapRecyclerViewAdapter.notifyItemInserted(getHeaderViewsCount() + positionStart);
            processEmptyView();
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapRecyclerViewAdapter.notifyItemRemoved(getHeaderViewsCount() + positionStart);
            processEmptyView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapRecyclerViewAdapter.notifyItemRangeChanged(getHeaderViewsCount() + positionStart, itemCount);
            processEmptyView();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapRecyclerViewAdapter.notifyItemMoved(getHeaderViewsCount() + fromPosition, getHeaderViewsCount() + toPosition);
        }
    };
}
