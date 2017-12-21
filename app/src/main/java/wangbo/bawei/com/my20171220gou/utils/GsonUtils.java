package wangbo.bawei.com.my20171220gou.utils;

import com.google.gson.Gson;

/**
 * Created by 杨文倩 on 2017/12/11.
 */

public class GsonUtils {
    private static Gson gson;

    public static Gson getInstance(){
        if(gson==null){
            gson=new Gson();
        }
        return gson;
    }

}
