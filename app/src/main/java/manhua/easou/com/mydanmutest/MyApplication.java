package manhua.easou.com.mydanmutest;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Administrator on 2019/1/8.
 */

public class MyApplication extends Application {
    public static RequestQueue queue;
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        queue = Volley.newRequestQueue(getApplicationContext());
    }
    public static RequestQueue getHttpQueue() {
        return queue;
    }
}
