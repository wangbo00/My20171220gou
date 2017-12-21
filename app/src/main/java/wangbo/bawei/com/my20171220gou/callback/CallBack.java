package wangbo.bawei.com.my20171220gou.callback;

/**
 * Created by 杨文倩 on 2017/12/11.
 */

public interface CallBack {
    void onSuccess(String tag, Object o);
    void onFailed(String tag, Exception e);
}
