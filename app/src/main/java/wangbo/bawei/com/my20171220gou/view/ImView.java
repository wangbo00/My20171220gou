package wangbo.bawei.com.my20171220gou.view;

/**
 * Created by 杨文倩 on 2017/12/11.
 */

public interface ImView {
    void onSuccess(String tag, Object o);
    void onFailed(String tag, Exception e);

}
