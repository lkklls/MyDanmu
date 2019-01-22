package manhua.easou.com.mydanmutest.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;

/**
 * Created by WW on 2017/3/31.
 */
public abstract class ZEasouActivity<ViewLayerType extends IEasouBaseView, PresenterLayer extends ZEasouPresenter<ViewLayerType>>
        extends Activity implements IEasouBaseView {

    protected PresenterLayer mPresenter;
    protected ViewLayerType mViewer;
    protected SparseArray<ZEasouPresenter> mPresenterArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建presenter
        mViewer = createViewer();
        mPresenter = createPresenter();
        if (mViewer != null && mPresenter != null) {
            mPresenter.attachView(mViewer);
        }
    }

    public abstract PresenterLayer createPresenter();

    public abstract ViewLayerType createViewer();

    /**
     * 实现view绑定多个presenter
     */
    public void extendPresenter(ZEasouPresenter... presenterArray) {

        for (ZEasouPresenter presenter : presenterArray) {
            if (mViewer != null && presenter != null) {
                presenter.attachView(mViewer);
            }
        }

    }

    @Override
    protected void onDestroy() {
        if(mPresenter!=null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }
}
