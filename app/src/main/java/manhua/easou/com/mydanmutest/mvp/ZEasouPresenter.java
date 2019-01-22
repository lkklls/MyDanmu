package manhua.easou.com.mydanmutest.mvp;

import java.lang.ref.SoftReference;

/**
 * Created by WW on 2017/3/31.
 * 软引用来避免内存溢出
 */
public class ZEasouPresenter<V extends IEasouBaseView> {

    private SoftReference<V> mView;

    /**
     * 将presenter与view绑定
     */
    public void attachView(V view) {
        mView = new SoftReference<V>(view);
    }

    /**
     * 将presenter与view解绑
     */
    public void detachView() {
        if (mView != null) {
            mView.clear();
            mView=null;
        }
    }

    /**
     * 获取具体的view对象
     */
    public V getRealView() {
        return mView != null ? mView.get() : null;
    }
}
