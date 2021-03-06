package manhua.easou.com.mydanmutest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import manhua.easou.com.mydanmutest.bean.RewardGiftBean;
import manhua.easou.com.mydanmutest.utils.EasouUtil;

/**
 * ViewPager+GridView实现左右滑动查看更多分类的控件
 */
public  class PageGridView extends FrameLayout {
    public final static int DEFAULT_PAGE_Size = 8;
    public final static int DEFAULT_NUM_COUNT = 4;
    public final static boolean DEFAULT_IS_ShOW_INDICATOR = true;
    public final static int DEFAULT_SELECTED_INDICTOR = R.drawable.shape_dot_selected;
    public final static int DEFAULT_UN_SELECTED_INDICTOR = R.drawable.shape_dot_normal;
    public final static int DEFAULT_VP_BACKGROUND = android.R.color.white;
    public final static int DEFAULT_ITEM_VIEW = R.layout.reward_grid_item;
    public final static int DEFAULT_INDICATOR_GRAVITY = 1;
    public final static int DEFAULT_INDICATOR_PADDING = 0;
    public final static int DEFAULT_INDICATOR_BACKGROUND = Color.WHITE;
    public final static int DEFAULT_VP_PADDING = 0;
    private Context mContext;
    private LayoutInflater mInflater;
    private View mContentView;
    private ViewPager mViewPager;
    private LinearLayout mLlDot;
    private List<RewardGiftBean> mDatas;
    private List mPagerList;
    private View lastClickView;
    private boolean isDefault=true;

    /**
     * 每一页显示的个数
     */
    private int pageSize;
    /**
     * 总的页数
     */
    private int pageCount;

    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    /**
     * 列数
     */
    private int numColumns = 0;
    /**
     * 指示器位置
     */
    private int indicatorGravity;
    /**
     * 是否显示指示器
     */
    private boolean isShowIndicator;
    /**
     * 选中指示器资源ID
     */
    private int selectedIndicator;
    /**
     * 未选中指示器资源ID
     */
    private int unSelectedIndicator;
    /**
     * Item布局
     */
    private int mItemView;
    /**
     * 指示器内边距
     */
    private int indicatorPaddingLeft;
    private int indicatorPaddingRight;
    private int indicatorPaddingTop;
    private int indicatorPaddingBottom;
    private int indicatorPadding;
    /**
     * 指示器背景颜色
     */
    private int indicatorBackground;


    /**
     * ViewPager背景
     */
    private int vpBackground;

    private int vpPadding= EasouUtil.dip2px(this.mContext,5);

    public PageGridView(Context context) {
        this(context, null, 0);
    }

    public PageGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public PageGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initViews(context);
    }


    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.PageGridView);
        pageSize = typedArray.getInteger(R.styleable.PageGridView_pageSize, DEFAULT_PAGE_Size);
        numColumns = typedArray.getInteger(R.styleable.PageGridView_numColumns, DEFAULT_NUM_COUNT);
        isShowIndicator = typedArray.getBoolean(R.styleable.PageGridView_isShowIndicator, DEFAULT_IS_ShOW_INDICATOR);
        selectedIndicator = typedArray.getResourceId(R.styleable.PageGridView_selectedIndicator, DEFAULT_SELECTED_INDICTOR);
        unSelectedIndicator = typedArray.getResourceId(R.styleable.PageGridView_unSelectedIndicator, DEFAULT_UN_SELECTED_INDICTOR);
        mItemView = typedArray.getResourceId(R.styleable.PageGridView_itemView, DEFAULT_ITEM_VIEW);
        indicatorGravity = typedArray.getInt(R.styleable.PageGridView_indicatorGravity, DEFAULT_INDICATOR_GRAVITY);
        indicatorPaddingLeft = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPaddingLeft, DEFAULT_INDICATOR_PADDING);
        indicatorPaddingRight = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPaddingRight, DEFAULT_INDICATOR_PADDING);
        indicatorPaddingTop = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPaddingTop, DEFAULT_INDICATOR_PADDING);
        indicatorPaddingBottom = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPaddingBottom, DEFAULT_INDICATOR_PADDING);
        indicatorPadding = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_indicatorPadding, -1);
       // indicatorBackground = typedArray.getColor(R.styleable.PageGridView_indicatorBackground, DEFAULT_INDICATOR_BACKGROUND);
        vpBackground = typedArray.getResourceId(R.styleable.PageGridView_vpBackground, DEFAULT_VP_BACKGROUND);
        vpPadding = typedArray.getDimensionPixelOffset(R.styleable.PageGridView_vpPadding, DEFAULT_VP_PADDING);
        typedArray.recycle();
    }

    private void initViews(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mContentView = mInflater.inflate(R.layout.reward_vp_gridview, this, true);
        mViewPager = (ViewPager) mContentView.findViewById(R.id.view_pager);
        mViewPager.setBackgroundResource(vpBackground);
        mViewPager.setPadding(0, 0, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //动态设置ViewPager
        float rate = (float) pageSize / (float) numColumns;
        int rows = (int) Math.ceil(rate);
        View itemView = mInflater.inflate(mItemView, this, false);

        ViewGroup.LayoutParams itemLayoutParams = itemView.getLayoutParams();
        int itemHeight = itemLayoutParams.height;
        layoutParams.height = rows * itemHeight;
        layoutParams.height += vpPadding * 2;
        mViewPager.setLayoutParams(layoutParams);
        mLlDot = (LinearLayout) mContentView.findViewById(R.id.ll_dot);
        if (indicatorGravity == 0) {
            mLlDot.setGravity(Gravity.LEFT);
        } else if (indicatorGravity == 1) {
            mLlDot.setGravity(Gravity.CENTER);
        } else if (indicatorGravity == 2) {
            mLlDot.setGravity(Gravity.RIGHT);
        }
        if (indicatorPadding != -1) {
            mLlDot.setPadding(indicatorPadding, indicatorPadding, indicatorPadding, indicatorPadding);
        } else {
            mLlDot.setPadding(indicatorPaddingLeft, indicatorPaddingTop, indicatorPaddingRight, indicatorPaddingBottom);
        }
       // mLlDot.setBackgroundColor(indicatorBackground);

    }


    public void setData(List<RewardGiftBean> data) {
        this.mDatas = data;
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        curIndex = 0;
        for (int i = 0; i < pageCount; i++) {
            // 每个页面都是inflate出一个新实例
            final GridView gridView = new GridView(mContext);
            gridView.setNumColumns(numColumns);
            gridView.setOverScrollMode(OVER_SCROLL_NEVER);

            GridViewAdapter gridViewAdapter = new GridViewAdapter(mContext, mDatas, i, pageSize);
            gridView.setAdapter(gridViewAdapter);
            mPagerList.add(gridView);
            gridViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int pos = position + curIndex * pageSize;
                    if (null != mOnItemClickListener) {
                        mOnItemClickListener.onItemClick(view,pos);
                        GridView gridView=(GridView)mPagerList.get(0);
                        if(gridView!=null&&isDefault&&pos!=0){
                            NetworkImageView iv_image=(NetworkImageView)gridView.getChildAt(0).findViewById(R.id.iv_image);
                            if(iv_image!=null){
                                iv_image.setSelected(false);
                                isDefault=false;
                            }
                        }
                        if(lastClickView!=null){
                            NetworkImageView iv_image=(NetworkImageView)lastClickView.findViewById(R.id.iv_image);
                            if(iv_image!=null){
                                iv_image.setSelected(false);
                            }
                        }
                        if(view!=null){
                            NetworkImageView iv_image=(NetworkImageView)view.findViewById(R.id.iv_image);
                            if(iv_image!=null){
                                iv_image.setSelected(true);
                                lastClickView=view;
                            }
                        }
                    }
                }
            });
        }
        //设置适配器
        mViewPager.setAdapter(new ViewPagerAdapter(mPagerList));
        //设置圆点
        if (isShowIndicator && pageCount > 1) {
            setOvalLayout();
        } else {
            if (mLlDot.getChildCount() > 0) mLlDot.removeAllViews();
            mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
                public void onPageSelected(int position) {
                    curIndex = position;
                }
            });
        }


    }

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        if (mLlDot.getChildCount() > 0) mLlDot.removeAllViews();
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(mInflater.inflate(R.layout.reward_viewpager_dot, null));
            ImageView imageView = (ImageView) mLlDot.getChildAt(i).findViewById(R.id.v_dot);
            imageView.setImageResource(unSelectedIndicator);
        }
        // 默认显示第一页
        ImageView imageView =(ImageView) mLlDot.getChildAt(0).findViewById(R.id.v_dot);

        imageView.setImageResource(selectedIndicator);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                ImageView lastImageView = (ImageView) mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot);
                lastImageView.setImageResource(unSelectedIndicator);
                // 圆点选中
                ImageView nextImageView = (ImageView) mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot);
                nextImageView.setImageResource(selectedIndicator);
                curIndex = position;
            }
        });
    }

    class ViewPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public ViewPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return (mViewList.get(position));
        }

        @Override
        public int getCount() {
            if (mViewList == null)
                return 0;
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    class GridViewAdapter<T> extends BaseAdapter {
        private List<T> mDatas;
        private LayoutInflater inflater;

        private OnItemClickListener mOnItemClickListener;

        /**
         * 页数下标,从0开始(当前是第几页)
         */
        private int curIndex;
        /**
         * 每一页显示的个数
         */
        private int pageSize;

        public GridViewAdapter(Context context, List<T> mDatas, int curIndex, int pageSize) {
            inflater = LayoutInflater.from(context);
            this.mDatas = mDatas;
            this.curIndex = curIndex;
            this.pageSize = pageSize;
        }

        public void setData(List<T> data) {
            this.mDatas = data;
            notifyDataSetChanged();
        }

        /**
         * 先判断数据集的大小是否足够显示满本页,如果够，则直接返回每一页显示的最大条目个数pageSize,如果不够，则有几项就返回几,(也就是最后一页的时候就显示剩余item)
         */
        @Override
        public int getCount() {
            return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);

        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position + curIndex * pageSize);
        }

        @Override
        public long getItemId(int position) {
            return position + curIndex * pageSize;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            /**
             * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize
             */
            int pos = position + curIndex * pageSize;
            BaseViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(mItemView, parent, false);
                if(itemModel!=null) {
                    viewHolder=itemModel.childViewHolder();
                    itemModel.setView(viewHolder, convertView);
                    viewHolder.itemView = convertView;
                }
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (BaseViewHolder) convertView.getTag();
            }

            if(pos==0&&isDefault){
                NetworkImageView iv_image=(NetworkImageView)viewHolder.itemView.findViewById(R.id.iv_image);
                if(iv_image!=null){
                    iv_image.setSelected(true);
                }
            }

            if(itemModel!=null) {
                itemModel.setViewHolder(viewHolder,pos);
            }
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mOnItemClickListener) {
                        mOnItemClickListener.onItemClick(view,position);
                    }
                }
            });
            return convertView;
        }


        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }


    }

    public static class BaseViewHolder {
        public View itemView;
        public ImageView iv;
    }


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private ItemModel itemModel;

    public void setItemModel( ItemModel item) {
        itemModel = item;
    }

    public interface ItemModel {


         void setView(BaseViewHolder viewHolder, View convertView);

         BaseViewHolder childViewHolder();

        /**
         *
         * @param viewHolder
         */
         void setViewHolder(BaseViewHolder viewHolder, int position);
    }

    /**
     * dp转px
     */
    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, mContext.getResources().getDisplayMetrics());
    }

    public int getDimensionPixelOffset(@DimenRes int resId) {
        return mContext.getResources().getDimensionPixelOffset(resId);
    }

    class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public List<RewardGiftBean> getDatas() {
        return mDatas;
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }
}
