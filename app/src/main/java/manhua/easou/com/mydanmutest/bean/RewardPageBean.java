package manhua.easou.com.mydanmutest.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ouli
 */
public class RewardPageBean implements Serializable {
    public boolean success;
    public String errorlog; //-1标识该书不允许打赏,需要退出打赏界面
    public String bookName;
    public int gid;
    public int nid;
    public String author;
    public int balance;
    public int couponValue;
    public int giftVersion;
    public List<DanmuBean> bulletList;
    public List<RewardGiftBean> giftList=new ArrayList<>();

}

