package manhua.easou.com.mydanmutest;

/**
 * Created by Administrator on 2018/12/29.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import manhua.easou.com.mydanmutest.bean.DanmuBean;
import manhua.easou.com.mydanmutest.utils.AppLog;
import manhua.easou.com.mydanmutest.utils.EasouUtil;
import manhua.easou.com.mydanmutest.utils.ImageHelper;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * Created by ouli on 2018/12/23.
 */

public class DanmuViewUtils {
    Timer timer = new Timer();
    private IDanmakuView mDanmakuView;
    private DanmakuContext mDanmuContext;
    private Activity activity;
    private Context context;
    private List<DanmuBean> danmakuData = new ArrayList<>();
    private int count = 1;
    private Bitmap defultHeaderImage, defultFooterImage, danmuBgImage, danmuBgImageMy;
    private boolean timerPause = false;
    private Duration duration_1, duration_2, duration_3;
    private BackgroundCacheStuffer cacheStufferMy, cacheStuffer;
    private HashMap<String, SoftReference<ImageSpan>> headerImageCache = new HashMap<>();
    private HashMap<String, SoftReference<ImageSpan>> footerImageCache = new HashMap<>();

    /**
     * 弹幕解析器
     */
    private BaseDanmakuParser myParser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };
    private int DANMU_PADDING = 0;
    private int DANMU_PADDING_LEFT = 4;
    private int DANMU_PADDING_TOP = 1;
    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
//            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
//            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
            if (danmaku.text instanceof Spanned) {
                danmaku.text = "";
            }
            //ImageLoader.getInstance().cancelDisplayTask(imageWare);
        }
    };

    public DanmuViewUtils(IDanmakuView mDanmakuView, Context context, Activity activity) {
        this.mDanmakuView = mDanmakuView;
        this.activity = activity;
        this.context = context;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        defultHeaderImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_defaut_round, options);
        defultFooterImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.reward_damu_gift_default, options);
        danmuBgImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.reward_danmu_bg, options);
        danmuBgImageMy = BitmapFactory.decodeResource(context.getResources(), R.drawable.reward_danmu_bg_my, options);
        cacheStufferMy = new BackgroundCacheStuffer(context, true);
        cacheStuffer = new BackgroundCacheStuffer(context, false);
        duration_1 = new Duration(7550);
        duration_2 = new Duration(7200);
        duration_3 = new Duration(8000);

    }

    /**
     * 图片缩放
     *
     * @param targetWidget 目标尺寸
     * @param targetHeight 目标尺寸
     */
    private static Bitmap resizeBitmap(Bitmap bitmap, float targetWidget, float targetHeight) {
        Matrix matrix = new Matrix();

        float scaleWidth = targetWidget / bitmap.getWidth();
        float scaleHeight = targetHeight / bitmap.getHeight();
        matrix.postScale(scaleWidth, scaleHeight); //长和宽放大缩小的比例
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();
        }
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }

        return bmp;

    }

    public void initView() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 4); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mDanmuContext = DanmakuContext.create();
        mDanmuContext
                .setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(2.9f)
                .setScaleTextSize(1.2f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
        if (mDanmakuView != null) {
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    AppLog.e("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {

                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    AppLog.e("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
                    BaseDanmaku latest = danmakus.last();
                    if (null != latest) {
                        AppLog.e("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onDanmakuLongClick(IDanmakus danmakus) {
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
                    return false;
                }
            });
            mDanmakuView.prepare(myParser, mDanmuContext);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    public void cancelDanmu() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void release() {
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
        this.danmakuData.clear();
        headerImageCache.clear();
        footerImageCache.clear();
        if (defultHeaderImage != null && !defultHeaderImage.isRecycled()) {
            defultHeaderImage.recycle();
            defultHeaderImage = null;
        }
        if (defultFooterImage != null && !defultFooterImage.isRecycled()) {
            defultFooterImage.recycle();
            defultFooterImage = null;
        }
        if (danmuBgImage != null && !danmuBgImage.isRecycled()) {
            danmuBgImage.recycle();
            danmuBgImage = null;
        }
        if (danmuBgImageMy != null && danmuBgImageMy.isRecycled()) {
            danmuBgImageMy.recycle();
            danmuBgImageMy = null;
        }
        if (cacheStufferMy != null) {
            cacheStufferMy.clearCaches();
        }
        if (cacheStuffer != null) {
            cacheStuffer.clearCaches();
        }
    }

    public void pause() {
        if (mDanmakuView != null) {
            mDanmakuView.pause();
        }
        timerPause = true;
    }

    public void resume() {
        if (mDanmakuView != null) {
            mDanmakuView.resume();
        }
        timerPause = false;
    }

    public void startDanmu(List<DanmuBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.danmakuData.clear();
        this.danmakuData.addAll(list);
        cancelDanmu();
        count = 1;
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new AsyncAddTask() {
                public void run() {
                    if (!timerPause) {
                        setData();
                    }
                }
            }, 1000, 10);
        }
    }

    private void setData() {
        if (danmakuData == null || danmakuData.size() == 0)
            return;

        if (danmakuData == null || danmakuData.size() == 0)
            return;
        // AppLog.e("弾幕==============线程======", Thread.currentThread().getName());
        addDanmaKuShowTextAndImage(false, danmakuData.get(count - 1));

        try {
            Thread.sleep(1700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (count == danmakuData.size()) {
            count = 1;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            ++count;
        }
    }

    private void sendDanmaAfterReward(DanmuBean danmuBean) {
        if (danmakuData.size() == 0) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {

            }

            List<DanmuBean> list = new ArrayList<>();
            list.add(danmuBean);
            startDanmu(list);
        } else {
            this.danmakuData.add(0, danmuBean);
        }
    }

    public void addDanmaKuShowTextAndImage(final boolean islive, final DanmuBean danmuBean) {
        final BaseDanmaku danmaku = mDanmuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        //弹幕实例BaseDanmaku,传入参数是弹幕方向
        if (danmuBean.isMy) {
            mDanmuContext.setCacheStuffer(cacheStufferMy, mCacheStufferAdapter); //定义背景样式
        } else {
            mDanmuContext.setCacheStuffer(cacheStuffer, mCacheStufferAdapter); //定义背景样式
        }
        if (headerImageCache.get(danmuBean.avatar) == null || headerImageCache.get(danmuBean.avatar).get() == null) {
            FutureTarget<Bitmap> futureTarget = GlideApp.with(activity)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .load(danmuBean.avatar)
                    .submit(50, 50);
            try {
                Bitmap bitmap = futureTarget.get();
                if (bitmap != null && !bitmap.isRecycled()) {
                    putHeaderImageSpanToCache(danmaku, islive, danmuBean, bitmap);
                } else if (defultHeaderImage != null) {
                    putHeaderImageSpanToCache(danmaku, islive, danmuBean, defultHeaderImage);
                }
            } catch (Exception e) {
                if (defultHeaderImage != null) {
                    putHeaderImageSpanToCache(danmaku, islive, danmuBean, defultHeaderImage);
                }
            }
        } else {
            addHeaderImage(danmaku, islive, danmuBean, headerImageCache.get(danmuBean.avatar).get());
        }
    }

    private void putHeaderImageSpanToCache(BaseDanmaku danmaku, final boolean islive, DanmuBean danmuBean, Bitmap headerBitmap) {
        Bitmap circleBitmap = ImageHelper.getCircleBitmap(headerBitmap);
        if (circleBitmap == null)
            return;
        if (headerBitmap != null && !headerBitmap.isRecycled()) {
            headerBitmap.recycle();
            headerBitmap = null;
            System.gc();
        }
        ImageSpan imageSpan = new VerticalImageSpan(context, resizeBitmap(circleBitmap, EasouUtil.dip2px(context, 23), EasouUtil.dip2px(context, 23)));
        SoftReference<ImageSpan> spanSoftReference = new SoftReference<ImageSpan>(imageSpan);
        headerImageCache.put(danmuBean.avatar, spanSoftReference);
        if (spanSoftReference.get() != null) {
            addHeaderImage(danmaku, islive, danmuBean, spanSoftReference.get());
        }
    }


    private void addHeaderImage(BaseDanmaku danmaku, final boolean islive, DanmuBean danmuBean, ImageSpan imageSpan) {

        String content = "";
        if (danmuBean.nickName.length() > 10) {
            content = "  " + danmuBean.nickName.substring(0, 10) + "...   赠送：" + danmuBean.giftDetails;
        } else {
            content = "  " + danmuBean.nickName + "   赠送：" + danmuBean.giftDetails;
        }
        String avatar = "  abcd";
        SpannableStringBuilder spannableString = new SpannableStringBuilder(avatar);
        spannableString.setSpan(imageSpan, 1, avatar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.append(" ");
        spannableString.append(content);
        loadDanmuPicture(danmaku, islive, danmuBean, spannableString);
    }

    private void loadDanmuPicture(final BaseDanmaku danmaku, final boolean islive, final DanmuBean danmuBean, final SpannableStringBuilder spannableString) {
        if (footerImageCache.get(danmuBean.image2) == null || footerImageCache.get(danmuBean.image2).get() == null) {
            FutureTarget<Bitmap> futureTarget =
                    GlideApp.with(activity)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .load(danmuBean.image2)
                            .submit(50, 50);

            try {
                Bitmap bitmap = futureTarget.get();
                if (bitmap != null && !bitmap.isRecycled()) {
                    putFooterImageSpanToCache(danmaku, islive, danmuBean, spannableString, bitmap);
                } else if (defultFooterImage != null) {
                    putFooterImageSpanToCache(danmaku, islive, danmuBean, spannableString, defultFooterImage);
                }
            } catch (Exception e) {
                if (defultFooterImage != null) {
                    putFooterImageSpanToCache(danmaku, islive, danmuBean, spannableString, defultFooterImage);
                }
            }
        } else {
            addFooterImageToDanmu(danmaku, islive, danmuBean, spannableString, footerImageCache.get(danmuBean.image2).get());
        }
    }

    private void putFooterImageSpanToCache(BaseDanmaku danmaku, final boolean islive, DanmuBean danmuBean, SpannableStringBuilder spannableString, Bitmap footerBitmap) {
        Bitmap bitmap = resizeBitmap(footerBitmap, EasouUtil.dip2px(context, 50), EasouUtil.dip2px(context, 50));
        ImageSpan imageSpan = new VerticalImageSpan(context, bitmap);
        SoftReference<ImageSpan> spanSoftReference = new SoftReference<ImageSpan>(imageSpan);
        footerImageCache.put(danmuBean.image2, spanSoftReference);
        if (spanSoftReference.get() != null) {
            addFooterImageToDanmu(danmaku, islive, danmuBean, spannableString, spanSoftReference.get());
        }
    }

    private void addFooterImageToDanmu(final BaseDanmaku danmaku, final boolean islive, final DanmuBean danmuBean, final SpannableStringBuilder spannableString, ImageSpan imageSpan) {
        if (mDanmakuView == null) {
            return;
        }
        String avatar = danmuBean.giftDetails;
        spannableString.append("    ");
        spannableString.setSpan(imageSpan, spannableString.length() - 1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.append("        ");
        danmaku.text = spannableString;
        danmaku.isLive = islive;
        int random = new Random().nextInt(3);
        if (random == 1) {
            danmaku.setDuration(duration_1);
        } else if (random == 2) {
            danmaku.setDuration(duration_2);
        } else {
            danmaku.setDuration(duration_3);
        }
        danmaku.setTime(mDanmakuView.getCurrentTime());
        danmaku.textColor = Color.WHITE;
        danmaku.textSize = 15f * (myParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        mDanmakuView.addDanmaku(danmaku);
        if (islive) {
            sendDanmaAfterReward(danmuBean);
        }
    }


    /**
     * span图片垂直居中
     */
    private static class VerticalImageSpan extends ImageSpan {

        @SuppressWarnings("unused")
        public VerticalImageSpan(Context arg0, int arg1) {
            super(arg0, arg1);
        }

        public VerticalImageSpan(Context context, Bitmap b) {
            super(context, b);
        }

        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fm) {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fm.ascent = -bottom;
                fm.top = -bottom;
                fm.bottom = top;
                fm.descent = top;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            canvas.save();
            int transY;
            transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private class BackgroundCacheStuffer extends SpannedCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();
        Context context;
        boolean personal;

        public BackgroundCacheStuffer(Context context, Boolean personal) {
            this.context = context;
            this.personal = personal;
        }

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            // 在背景绘制模式下增加padding
            // danmaku.padding = EasouUtil.dip2px(context,DANMU_PADDING);
            //文本颜色
            // danmaku.textColor = context.getResources().getColor(R.color.white);
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setAntiAlias(true);
            if (danmaku.isGuest) {//如果是赞 就不要设置背景
                paint.setColor(Color.TRANSPARENT);
            }
            Bitmap bitmap;
            if (personal) {
                bitmap = danmuBgImageMy;
            } else {
                bitmap = danmuBgImage;
            }
            //获取点9块
            NinePatch np = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
            np.draw(canvas, new RectF(left, top + EasouUtil.dip2px(context, DANMU_PADDING_TOP)
                    , left + danmaku.paintWidth,
                    top + danmaku.paintHeight - EasouUtil.dip2px(context, DANMU_PADDING_TOP)));
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }

    class AsyncAddTask extends TimerTask {

        @Override
        public void run() {
            setData();
        }
    }
}
