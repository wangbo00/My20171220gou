package wangbo.bawei.com.my20171220gou.presenter;


import java.util.Map;

import wangbo.bawei.com.my20171220gou.callback.CallBack;
import wangbo.bawei.com.my20171220gou.utils.HttpUtils;
import wangbo.bawei.com.my20171220gou.view.ImView;

/**
 * Created by 杨文倩 on 2017/12/11.
 */

public class Presenter {
    private ImView inv;
    public void attachView(ImView inv){
        this.inv=inv;
    }

    public void get(String url, Map<String,String> map, String tag, Class clv){
        HttpUtils.getInstance().get(url, map, new CallBack() {
            @Override
            public void onSuccess(String tag, Object o) {
                if(o!=null){
                    inv.onSuccess(tag,o);
                }
            }

            @Override
            public void onFailed(String tag, Exception e) {
                inv.onFailed(tag,e);
            }
        },clv,tag);
    }
    //创建对象方便 v层进行释放
    public void deleteView(){
        if(inv!=null){
            inv=null;
        }
    }
   /* //销毁线程 资源释放 在Activity 主类
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.deleteView();
        }
    }*/
}
