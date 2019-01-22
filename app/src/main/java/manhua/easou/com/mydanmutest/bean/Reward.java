package manhua.easou.com.mydanmutest.bean;

import java.io.Serializable;

/**
 * Created by ouli
 */
public class Reward implements Serializable {
    public boolean success;
    public String errorlog; //-1标识该书不允许打赏,需要退出打赏界面
    public int balance;
    public int couponValue;
    ////第一名 100000000
    //书籍打赏排行变化 1：上升一名 0： 名次未变 -1：下降一名
    public int bookRank;
    //用户打赏排名变化 1：上升一名 0： 名次未变 -1：下降一名
    public int userRank;
    //书友排行
    public int bookFriendRank;

}

