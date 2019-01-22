package manhua.easou.com.mydanmutest.mvp;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by WW on 2016/3/31.
 */
public abstract class ZEasouFragment<ViewLayerType extends IEasouBaseView, PresenterLayerType extends ZEasouPresenter<IEasouBaseView>> extends Fragment {

    private ViewLayerType mView;
    private PresenterLayerType mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建presenter
        mView = createViewer();
        mPresenter = createPresenter();
    }

    protected abstract PresenterLayerType createPresenter();

    public abstract ViewLayerType createViewer();


}
