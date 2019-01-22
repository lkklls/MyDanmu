package manhua.easou.com.mydanmutest;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import manhua.easou.com.mydanmutest.Presenter.RewardPresenter;
import manhua.easou.com.mydanmutest.bean.DanmuBean;
import manhua.easou.com.mydanmutest.bean.Reward;
import manhua.easou.com.mydanmutest.bean.RewardGiftBean;
import manhua.easou.com.mydanmutest.bean.RewardPageBean;
import manhua.easou.com.mydanmutest.mvp.RewardView;
import manhua.easou.com.mydanmutest.mvp.ZEasouActivity;
import manhua.easou.com.mydanmutest.utils.EasouUtil;
import manhua.easou.com.mydanmutest.utils.SystemBarTintManager;
import master.flame.danmaku.controller.IDanmakuView;

/**
 * <打赏界面>
 *
 * @author ouli
 */
public class ActReward extends ZEasouActivity<RewardView, RewardPresenter> implements RewardView {

    private final int DURANTIONTIME = 200;
    List<RewardGiftBean> mList;
    int duration = 0;
    private IDanmakuView mDanmakuView;
    private TextView tv_money, tv_book_name, tv_book_writer;
    private ImageView iv_balance_refresh;
    private DanmuViewUtils danmuViewUtils;
    //private PageGridView mPageGridView;
    private String gid, name, author, page, entrance, booktype;
    private int lastPisiton = 0;
    private RewardPageBean rewardPageBean;
    private RewardGiftBean selectedGift;
    private Button bt_reward;
    private RelativeLayout rl_back;
    private int sucess_type;// sucess_type //1书友排行榜  2书籍打赏排行榜 3 本书打赏榜/阅读页菜单栏打赏入口/每章节末页打赏入口进入

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.act_reward);
        initView();
    }

    @Override
    public RewardPresenter createPresenter() {
        return new RewardPresenter(getApplicationContext(), this);
    }

    @Override
    public RewardView createViewer() {
        return this;
    }


    protected void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_book_name = (TextView) findViewById(R.id.tv_book_name);
        tv_book_writer = (TextView) findViewById(R.id.tv_book_writer);
        iv_balance_refresh = (ImageView) findViewById(R.id.iv_balance_refresh);
        bt_reward = (Button) findViewById(R.id.bt_reward);
        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);
        FrameLayout fl_loadingPage = (FrameLayout) findViewById(R.id.fl_loadingPage);
        danmuViewUtils = new DanmuViewUtils(mDanmakuView, getApplicationContext(), this);
        danmuViewUtils.initView();
        Intent intent = getIntent();
        bt_reward.setEnabled(false);
        if (SystemBarTintManager.canModifyStatusBar(this) || Build.VERSION.SDK_INT >= 21) {
            rl_back.setPadding(0, EasouUtil.dip2px(this, 34), 0, 0);
            SystemBarTintManager.setActivityBar(this, R.color.white, true, false);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mDanmakuView.getView().getLayoutParams();
                lp.setMargins(0, EasouUtil.dip2px(getApplicationContext(),60),0 , 0);
            mDanmakuView.getView().setLayoutParams(lp);
        }

//        mPageGridView = (PageGridView) findViewById(R.id.vp_grid_view);
//        mPageGridView.setItemModel(new RewardGridAdapter(mPageGridView));
//        mPageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                bt_reward.setEnabled(true);
//                lastPisiton = position;
//                if (rewardPageBean != null) {
//                    selectedGift = rewardPageBean.giftList.get(position);
//
//                        bt_reward.setText("立即赠送");
//                }
//            }
//        });
        if (intent != null) {
            gid = intent.getStringExtra("gid");
            if (TextUtils.isEmpty(gid)) {
                gid = String.valueOf(intent.getIntExtra("gid", 0));
            }
            name = intent.getStringExtra("name");
            author = intent.getStringExtra("author");
            page = intent.getStringExtra("page");
            entrance = intent.getStringExtra("entrance");
            sucess_type = intent.getIntExtra("sucess_type", 3);
            booktype = intent.getStringExtra("bookType");
            mPresenter.getRewardPageData(gid);
        }


    }

    public void getRewardPageSucess(RewardPageBean rewardPageBean) {
        if (rewardPageBean == null || rewardPageBean.giftList == null) {
            return;
        }
        this.rewardPageBean = rewardPageBean;
        List<RewardGiftBean> rewardGiftBeanList=new ArrayList<>();
        for(RewardGiftBean rewardGiftBean: rewardPageBean.giftList){
            if(rewardGiftBean.status==1) {
                rewardGiftBeanList.add(rewardGiftBean);
            }
        }
        tv_book_writer.setText(rewardPageBean.author);
        tv_book_name.setText(rewardPageBean.bookName);
       // mPageGridView.setData(rewardGiftBeanList);
        tv_money.setText("1000" + "金币" + "  " + "1000" + "书券");
        for (DanmuBean danmuBean : rewardPageBean.bulletList) {
            for (RewardGiftBean rewardGiftBean : rewardPageBean.giftList) {
                if (danmuBean.giftId == rewardGiftBean.giftId) {
                    danmuBean.giftName = rewardGiftBean.giftName;
                    danmuBean.image1 = rewardGiftBean.image1;
                    danmuBean.image2 = rewardGiftBean.image2;
                    danmuBean.giftDetails=rewardGiftBean.giftDetails;
                }
            }
            if (TextUtils.isEmpty(danmuBean.giftDetails)) {
                rewardPageBean.bulletList.remove(danmuBean);
            }
        }
        bt_reward.setEnabled(true);
        if (rewardPageBean != null) {
            selectedGift = rewardPageBean.giftList.get(0);
                bt_reward.setText("立即赠送");
        }
       danmuViewUtils.startDanmu(rewardPageBean.bulletList);
    }

    public void quitRewardPage() {
        danmuViewUtils.cancelDanmu();
        danmuViewUtils.release();
        finish();
    }

    public void getRewardError() {
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        danmuViewUtils.cancelDanmu();
        danmuViewUtils.release();

    }

    @Override
    public void showDanmuView() {

    }

    @Override
    public void pauseDanmuView() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        danmuViewUtils.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        danmuViewUtils.resume();
    }

    public void rl_back(View view) {
        danmuViewUtils.cancelDanmu();
        danmuViewUtils.release();
        finish();
    }

    public void rl_bookRank(View view) {

    }

    public void pay(View v) {
        if (lastPisiton == -1){
            mPresenter.giveReward(rewardPageBean.gid + "", rewardPageBean.giftList.get(lastPisiton).giftId + "");
        }
    }

    public void balanceRefresh(View v) {
        mPresenter.refreshBlance();
    }



}
