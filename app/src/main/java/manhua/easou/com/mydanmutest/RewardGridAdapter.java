package manhua.easou.com.mydanmutest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import manhua.easou.com.mydanmutest.bean.RewardGiftBean;
import manhua.easou.com.mydanmutest.utils.BitmapCache;

/**
 * Created by Administrator on 2018/12/25.
 */

public class RewardGridAdapter implements PageGridView.ItemModel {
    PageGridView mPageGridView;
    List<RewardGiftBean> mList;

    public RewardGridAdapter(PageGridView mPageGridView) {
        this.mPageGridView = mPageGridView;
    }

    @Override
    public void setView(PageGridView.BaseViewHolder viewHolder, View convertView) {
        MyBaseViewHolder holder = (MyBaseViewHolder) viewHolder;
        if (holder == null)
            return;
        holder.iv = (NetworkImageView) convertView.findViewById(R.id.iv_image);
        holder.title = (TextView) convertView.findViewById(R.id.tv_title);
        holder.money = (TextView) convertView.findViewById(R.id.tv_money);
    }

    @Override
    public PageGridView.BaseViewHolder childViewHolder() {
        return new MyBaseViewHolder();
    }

    @Override
    public void setViewHolder(PageGridView.BaseViewHolder viewHolder, int pisition) {
        MyBaseViewHolder holder = (MyBaseViewHolder) viewHolder;
        if (holder == null)
            return;
        mList = mPageGridView.getDatas();
        if (mList != null && mList.size() > 0) {
           // ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueue(), BitmapCache.instance());
           // holder.iv.setImageUrl(mList.get(pisition).image1, imageLoader);
            holder.title.setText(mList.get(pisition).giftName);
            holder.money.setText(mList.get(pisition).price+"书券");
        }
    }


    public static class MyBaseViewHolder extends PageGridView.BaseViewHolder {
        public View itemView;
        public NetworkImageView iv;
        public TextView title;
        public TextView money;
    }
}
