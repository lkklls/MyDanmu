package manhua.easou.com.mydanmutest.Presenter;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import manhua.easou.com.mydanmutest.ActReward;
import manhua.easou.com.mydanmutest.bean.DanmuBean;
import manhua.easou.com.mydanmutest.bean.RewardGiftBean;
import manhua.easou.com.mydanmutest.bean.RewardPageBean;
import manhua.easou.com.mydanmutest.mvp.RewardView;
import manhua.easou.com.mydanmutest.mvp.ZEasouPresenter;

/**
 * Created by ouli
 * mvp
 */

public class RewardPresenter extends ZEasouPresenter<RewardView> {
    Context context;
    private WeakReference<ActReward> weakReference;
    private RewardPageBean localReward=null;


    public RewardPresenter(Context context, ActReward activity) {
        this.context = context;
        weakReference = new WeakReference<ActReward>(activity);
    }

    /**
     * 获取打赏页面接口
     */
    public void getRewardPageData(String gid) {
        String json="{'item':{'gid':100105584,'balance':{'balance':4565.0,'couponValue':0},'nid':105584,'author':'天蚕土豆','bookName':'大主宰','bulletList':[{'avatar':'http://pic.ps.easou.com/servlet/image?k=4D5E2A885578299E5A5902AD295447C6','nickName':'宜搜网友_test02','giftId':20},{'avatar':'http://pic.ps.easou.com/servlet/image?k=DEA640EBB475802A038565FC252B2D52','nickName':'第九十九设今生今世经济的角度好的亟待解决','giftId':22},{'avatar':'http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271','nickName':'小伙伴_KTSEBIc583','giftId':24},{'avatar':'http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271','nickName':'小伙伴_KTSEBIc583','giftId':20},{'avatar':'http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271','nickName':'小伙伴_KTSEBIc583','giftId':24},{'avatar':'http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271','nickName':'小伙伴_KTSEBIc583','giftId':22},{'avatar':'http://pic.ps.easou.com/servlet/image?k=27FC2365CFF229C90B0B71C6FA075DD9','nickName':'你不是你','giftId':20},{'avatar':'http://pic.ps.easou.com/servlet/image?k=27FC2365CFF229C90B0B71C6FA075DD9','nickName':'你不是你','giftId':18},{'avatar':'http://pic.ps.easou.com/servlet/image?k=27FC2365CFF229C90B0B71C6FA075DD9','nickName':'你不是你','giftId':18},{'avatar':'http://pic.ps.easou.com/servlet/image?k=DEA640EBB475802A038565FC252B2D52','nickName':'QQtest','giftId':22},{'avatar':'http://pic.ps.easou.com/servlet/image?k=DEA640EBB475802A038565FC252B2D52','nickName':'QQtest','giftId':19},{'avatar':'http://pic.ps.easou.com/servlet/image?k=DEA640EBB475802A038565FC252B2D52','nickName':'QQtest','giftId':19},{'avatar':'http://pic.ps.easou.com/servlet/image?k=DEA640EBB475802A038565FC252B2D52','nickName':'QQtest','giftId':22},{'avatar':'http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271','nickName':'小伙伴_KTSEBIc583','giftId':24},{'avatar':'http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271','nickName':'小伙伴_KTSEBIc583','giftId':22},{'avatar':'http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271','nickName':'小伙伴_KTSEBIc583','giftId':22},{'avatar':'http://pic.ps.easou.com/servlet/image?k=D9B42DC19BBAEDC2A52D76A87CAD0F1D','nickName':'ㄌ_KUMgXFG','giftId':17},{'avatar':'http://pic.ps.easou.com/servlet/image?k=D9B42DC19BBAEDC2A52D76A87CAD0F1D','nickName':'ㄌ_KUMgXFG','giftId':17},{'avatar':'http://pic.ps.easou.com/servlet/image?k=D9B42DC19BBAEDC2A52D76A87CAD0F1D','nickName':'ㄌ_KUMgXFG','giftId':17},{'avatar':'http://pic.ps.easou.com/servlet/image?k=B0C5494153A0108A083649774C8C13AB','nickName':'Hunter·满','giftId':22},{'avatar':'http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271','nickName':'小伙伴_KTSEBIc583','giftId':24},{'avatar':'http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271','nickName':'小伙伴_KTSEBIc583','giftId':22}],'giftVersion':104,'giftList':[{'remark':'爱你的心情，贼甜！','status':1,'price':10,'giftId':22,'giftName':'棒棒糖','image1':'http://120.192.70.152/img/manager/7ceaf6d19a8f48c58b42ce1390a5a088.png','image2':'http://120.192.70.152/img/manager/e694cf3ee10d4b5ea16f0e0aced45d1d.png','giftDetails':'爱你的心情，贼甜！'},{'remark':'你在我心里长盛常开！','status':1,'price':20,'giftId':25,'giftName':'鲜花','image1':'http://120.192.70.152/img/manager/a2ffe63919e84ac8b8eeab05424ec3f9.png','image2':'http://120.192.70.152/img/manager/7a24b8f7a91e4531b55f21e2714748ae.png','giftDetails':'你在我心里长盛常开！'},{'remark':'你无情你残酷你无理取闹！','status':1,'price':50,'giftId':23,'giftName':'一杯狗血','image1':'http://120.192.70.152/img/manager/333c71ebfe92429ca2b8fdd2e850af4c.png','image2':'http://120.192.70.152/img/manager/15aca7d35bac4174b9526a158bcdfe21.png','giftDetails':'你无情你残酷你无理取闹！'},{'remark':'你闪亮的每一面我都爱！','status':1,'price':100,'giftId':24,'giftName':'尊贵钻戒','image1':'http://120.192.70.152/img/manager/9b7d6853859c44b79e3d8bf58171bbb2.png','image2':'http://120.192.70.152/img/manager/f4f86c65c7ed4ece82de2b98bc6f3824.png','giftDetails':'你闪亮的每一面我都爱！'},{'remark':'上了车，就是我的人了！','status':1,'price':500,'giftId':18,'giftName':'超级跑车','image1':'http://120.192.70.152/img/manager/a6ab54f2c5b446d8aa1dc331772d258f.png','image2':'http://120.192.70.152/img/manager/f76761d8271b496aba9f76b2a40240e7.png','giftDetails':'上了车，就是我的人了！'},{'remark':'邀请你畅游我的宇宙！','status':1,'price':2000,'giftId':19,'giftName':'光速飞船','image1':'http://120.192.70.152/img/manager/207c0f191df442bc8da940c14adf1bc4.png','image2':'http://120.192.70.152/img/manager/4f01538bddc947f0afe2e576956df283.png','giftDetails':'邀请你畅游我的宇宙！'},{'remark':'魔法般的奇迹都是因为爱你！','status':1,'price':5000,'giftId':20,'giftName':'移动城堡','image1':'http://120.192.70.152/img/manager/53045b053d254698984906909e12e8ca.png','image2':'http://120.192.70.152/img/manager/1096643bf8ff470baa99561add2229c5.png','giftDetails':'魔法般的奇迹都是因为爱你！'},{'remark':'给你源源不断灵力注入！','status':1,'price':10000,'giftId':21,'giftName':'灵感星球','image1':'http://120.192.70.152/img/manager/074dccad61f349209e9c30cb3e996440.png','image2':'http://120.192.70.152/img/manager/b082b5ea5c0e4db5b86d7c8786fea5e3.png','giftDetails':'给你源源不断灵力注入！'},{'remark':'1111111111','status':0,'price':11,'giftId':26,'giftName':'111','image1':'http://120.192.70.152/img/manager/2d8792e2a0884b28b6f4146a61d41a20.png','image2':'http://120.192.70.152/img/manager/0e01a0f1a4d8471ca80c438f80daf108.png','giftDetails':'1111111111'},{'remark':'太空旅行太空旅行太空旅行太空旅行','status':0,'price':17,'giftId':17,'giftName':'太空旅行旅行','image1':'http://120.192.70.152/img/manager/c5e15b36b1774dbc8e20c2a969565316.png','image2':'http://120.192.70.152/img/manager/c79ff3c4da294233a0be5532c8fe4345.png','giftDetails':'太空旅行太空旅行太空旅行太空旅行'},{'remark':'咋不上天咋不上天咋不上天咋不上天','status':0,'price':160000,'giftId':16,'giftName':'咋不上天天','image1':'http://120.192.70.152/img/manager/95fb5cd40ddb4332b9d1d26712edf9cd.png','image2':'http://120.192.70.152/img/manager/408d2c5f225f47bd801a1fbac5063587.png','giftDetails':'咋不上天咋不上天咋不上天咋不上天'},{'remark':'红超跑红超跑红超跑红超跑','status':0,'price':15,'giftId':15,'giftName':'红超跑','image1':'http://120.192.70.152/img/manager/fff4045253614d3ab808c23f6dd5b447.png','image2':'http://120.192.70.152/img/manager/fb276dd8c01044989497eeda0b8d4771.png','giftDetails':'红超跑红超跑红超跑红超跑'},{'remark':'黑洞黑洞黑洞黑洞黑洞','status':0,'price':14,'giftId':14,'giftName':'黑洞','image1':'http://120.192.70.152/img/manager/bb0633e6aa1d4a3cacbdf6822f7ffdc4.png','image2':'http://120.192.70.152/img/manager/be58b3c588d24d1fac8443ff40159d69.png','giftDetails':'黑洞黑洞黑洞黑洞黑洞'},{'remark':'倒计时发射倒计时发射倒计时发射','status':0,'price':13,'giftId':13,'giftName':'倒计时发射','image1':'http://120.192.70.152/img/manager/78606825ac6b4b42bb2fa8996ec534ad.png','image2':'http://120.192.70.152/img/manager/e2369609b1a34a31b645dadb2cf87a16.png','giftDetails':'倒计时发射倒计时发射倒计时发射'},{'remark':'红色滴滴红色滴滴红色滴滴','status':0,'price':12,'giftId':12,'giftName':'红色滴','image1':'http://120.192.70.152/img/manager/586f6c345afb44d7a8751d6c6678a166.png','image2':'http://120.192.70.152/img/manager/708635b268df42b7a67800e935b514e4.png','giftDetails':'红色滴滴红色滴滴红色滴滴'},{'remark':'小宇宙爆发小宇宙爆发小宇宙爆发','status':0,'price':11,'giftId':11,'giftName':'小宇宙爆发','image1':'http://120.192.70.152/img/manager/df84b471593440e19824d890b9f5f247.png','image2':'http://120.192.70.152/img/manager/7eaeaa613f5246ba933f819cf641414b.png','giftDetails':'小宇宙爆发小宇宙爆发小宇宙爆发'},{'remark':'一飞冲天一飞冲天一飞冲天','status':0,'price':10,'giftId':10,'giftName':'一飞冲天','image1':'http://120.192.70.152/img/manager/af5713e27d514c878fb63c878b3053d1.png','image2':'http://120.192.70.152/img/manager/513c74b7fd614626a946611463ab659c.png','giftDetails':'一飞冲天一飞冲天一飞冲天'},{'remark':'红色滴滴滴滴滴滴滴滴滴滴','status':0,'price':9,'giftId':9,'giftName':'红色滴滴','image1':'http://120.192.70.152/img/manager/a377520d2dcb45f0b178f74bfd6e4127.png','image2':'http://120.192.70.152/img/manager/507dc0112509478881db64c7e71433ac.png','giftDetails':'红色滴滴滴滴滴滴滴滴滴滴'},{'remark':'病毒','status':0,'price':8,'giftId':8,'giftName':'病毒','image1':'http://120.192.70.152/img/manager/e515ad7476d04ffc83c3ece13060dfa7.png','image2':'http://120.192.70.152/img/manager/1cfe0e827bc044469eee86c82b9b2a61.png','giftDetails':'病毒'},{'remark':'大别野大别野大别野大别野大别野','status':0,'price':7,'giftId':7,'giftName':'大别野','image1':'http://120.192.70.152/img/manager/05309e422651438595bda163986f8b2d.png','image2':'http://120.192.70.152/img/manager/72c4767dea42482a98a040fa907bcc89.png','giftDetails':'大别野大别野大别野大别野大别野'},{'remark':'毒气弹毒气弹毒气弹毒气弹毒气弹','status':0,'price':6,'giftId':6,'giftName':'毒气弹','image1':'http://120.192.70.152/img/manager/62431e5843f14332ad9de2829d27a783.png','image2':'http://120.192.70.152/img/manager/3c903753945d4a318b8dca3697d82d25.png','giftDetails':'毒气弹毒气弹毒气弹毒气弹毒气弹'},{'remark':'拖拉机拖拉机拖拉机拖拉机拖拉机','status':0,'price':5,'giftId':5,'giftName':'拖拉机','image1':'http://120.192.70.152/img/manager/82873088d1394b89acf0e2d103cca6a4.png','image2':'http://120.192.70.152/img/manager/1fbd72b051af4530a535d588f03c7b13.png','giftDetails':'拖拉机拖拉机拖拉机拖拉机拖拉机'},{'remark':'宇宙宇宙宇宙宇宙宇宙宇宙','status':0,'price':4,'giftId':4,'giftName':'宇宙','image1':'http://120.192.70.152/img/manager/b2187b8543074b9b89cf34aca42bc10f.png','image2':'http://120.192.70.152/img/manager/a5c2deebc392435087f273a11b7c4a94.png','giftDetails':'宇宙宇宙宇宙宇宙宇宙宇宙'},{'remark':'城堡城堡城堡城堡城堡城堡城堡城堡','status':0,'price':3,'giftId':3,'giftName':'城堡','image1':'http://120.192.70.152/img/manager/715a6b6bce3c4ebbbec0652ded7e0b41.png','image2':'http://120.192.70.152/img/manager/d97a17bff0494dd2851c8d7f8584bcee.png','giftDetails':'城堡城堡城堡城堡城堡城堡城堡城堡'},{'remark':'火箭火箭火箭火箭火箭火箭火箭火箭','status':0,'price':2,'giftId':2,'giftName':'火箭','image1':'http://120.192.70.152/img/manager/265632f628fe4a34892588e5e1d7335c.png','image2':'http://120.192.70.152/img/manager/bc7b2d25642c4eb9844fa01670dcf4cb.png','giftDetails':'火箭火箭火箭火箭火箭火箭火箭火箭'},{'remark':'超跑超跑超跑超跑超跑超跑超跑超跑','status':0,'price':1,'giftId':1,'giftName':'超跑','image1':'http://120.192.70.152/img/manager/e07e01d4c289496b8c664aa177992565.png','image2':'http://120.192.70.152/img/manager/c762f66639334f6a8ace61f3a1d2d377.png','giftDetails':'超跑超跑超跑超跑超跑超跑超跑超跑'}]},'success':true,'errorlog':''}";
        RewardPageBean rewardPageBean=null;
        try {
            rewardPageBean = getRewardPageResult(json);
        }catch (JSONException e) {
            e.printStackTrace();
        }
//        List<DanmuBean> bulletList=new ArrayList<>();
//        DanmuBean danmuBean=new DanmuBean();
//        danmuBean.avatar="http://pic.ps.easou.com/servlet/image?k=4D5E2A885578299E5A5902AD295447C6";
//        danmuBean.nickName="宜搜网友_test02";
//        danmuBean.giftId=20;
//
//        DanmuBean danmuBean2=new DanmuBean();
//        danmuBean2.avatar="http://pic.ps.easou.com/servlet/image?k=DEA640EBB475802A038565FC252B2D52";
//        danmuBean2.nickName="第九十九设今生今世经济的角度好的亟待解决";
//        danmuBean2.giftId=22;
//
//        DanmuBean danmuBean3=new DanmuBean();
//        danmuBean3.avatar="http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271";
//        danmuBean3.nickName="小伙伴_KTSEBIc583";
//        danmuBean3.giftId=24;
//
//        DanmuBean danmuBean4=new DanmuBean();
//        danmuBean4.avatar="http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271";
//        danmuBean4.nickName="小伙伴_KTSEBIc583";
//        danmuBean4.giftId=20;
//
//        DanmuBean danmuBean5=new DanmuBean();
//        danmuBean5.avatar="http://pic.ps.easou.com/servlet/image?k=0E222DA34B7D44BFBB0FF83C8FB24271";
//        danmuBean5.nickName="小伙伴_KTSEBIc583";
//        danmuBean5.giftId=24;
//
//        DanmuBean danmuBean6=new DanmuBean();
//        danmuBean6.avatar="http://pic.ps.easou.com/servlet/image?k=D9B42DC19BBAEDC2A52D76A87CAD0F1D";
//        danmuBean6.nickName="ㄌ_KUMgXFG";
//        danmuBean6.giftId=17;
//
//        DanmuBean danmuBean7=new DanmuBean();
//        danmuBean7.avatar="http://pic.ps.easou.com/servlet/image?k=B0C5494153A0108A083649774C8C13AB";
//        danmuBean7.nickName="Hunter·满";
//        danmuBean7.giftId=22;
//
//        bulletList.add(danmuBean);
//        bulletList.add(danmuBean2);
//        bulletList.add(danmuBean3);
//        bulletList.add(danmuBean4);
//        bulletList.add(danmuBean5);
//        bulletList.add(danmuBean6);
//        bulletList.add(danmuBean7);
//
//        RewardPageBean rewardPageBean=new RewardPageBean();
//        rewardPageBean.author="ouli";
//        rewardPageBean.balance=1000;
//        rewardPageBean.bookName="新中国";
//        rewardPageBean.couponValue=1000;
//        rewardPageBean.bulletList.addAll(bulletList);
        if(rewardPageBean.giftList!=null&&rewardPageBean.giftList.size()>0) {

            weakReference.get().getRewardPageSucess(rewardPageBean);
        }

    }

    /**
     * 打赏接口
     */
    public void giveReward(String gid, String giftId) {


    }

    /**
     * 从网络获取用户信息
     */
    public void refreshBlance() {

    }

    public static RewardPageBean getRewardPageResult(String jsonString)
            throws JSONException {
        RewardPageBean result = new RewardPageBean();
        JSONObject jsonObject = new JSONObject(jsonString);
        result.success = jsonObject.getBoolean("success");
        result.errorlog = jsonObject.getString("errorlog");
        if (!result.success) {
            return result;
        }

        JSONObject itemObject = jsonObject.getJSONObject("item");
        if (itemObject == null) {
            return null;
        }
        if (!itemObject.isNull("bookName")) {
            String bookName = itemObject.getString("bookName");
            result.bookName = bookName;
        }
        if (!itemObject.isNull("gid")) {
            int gid = itemObject.getInt("gid");
            result.gid = gid;
        }
        if (!itemObject.isNull("nid")) {
            int nid = itemObject.getInt("nid");
            result.nid = nid;
        }
        if (!itemObject.isNull("author")) {
            String author = itemObject.getString("author");
            result.author = author;
        }
        if (!itemObject.isNull("giftVersion")) {
            int giftVersion = itemObject.getInt("giftVersion");
            result.giftVersion = giftVersion;
        }
        JSONObject blanceObject = itemObject.getJSONObject("balance");
        if (blanceObject != null) {
            result.balance = blanceObject.getInt("balance");
            result.couponValue = blanceObject.getInt("couponValue");
        }
        if (!itemObject.isNull("bulletList")) {
            result.bulletList = new ArrayList<>();
            JSONArray bulletArray = itemObject.getJSONArray("bulletList");
            if (bulletArray != null) {
                for (int i = 0; i < bulletArray.length(); i++) {
                    JSONObject jsonObj = bulletArray.getJSONObject(i);
                    if (jsonObj != null) {
                        DanmuBean danmu = new DanmuBean();
                        danmu.nickName = jsonObj.optString("nickName");
                        danmu.avatar = jsonObj.optString("avatar");
                        danmu.giftId = jsonObj.optInt("giftId");
                        result.bulletList.add(danmu);
                    }
                }
            }
        }
        if (!itemObject.isNull("giftList")) {
            result.giftList = new ArrayList<>();
            JSONArray giftArray = itemObject.getJSONArray("giftList");
            if (giftArray != null && giftArray.length() > 0) {
                for (int i = 0; i < giftArray.length(); i++) {
                    JSONObject jsonObj = giftArray.getJSONObject(i);
                    if (jsonObj != null) {
                        RewardGiftBean gift = new RewardGiftBean();
                        gift.giftName = jsonObj.optString("giftName");
                        gift.status = jsonObj.optInt("status");
                        gift.price = jsonObj.optInt("price");
                        gift.giftId = jsonObj.optInt("giftId");
                        gift.image1 = jsonObj.optString("image1");
                        gift.image2 = jsonObj.optString("image2");
                        gift.giftDetails=jsonObj.optString("giftDetails");
                        result.giftList.add(gift);
                    }
                }
            }
        }

        return result;
    }
}
