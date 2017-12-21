package wangbo.bawei.com.my20171220gou.presenter;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 杨文倩 on 2017/12/11.
 */

public class Logger implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();
        HttpUrl url=original.url().newBuilder()
                //需要自己添加 俩参数看拦截器
                .addQueryParameter("source","android")
                .build();
        //添加请求头
        Request request = original.newBuilder()
                .url(url)
                .build();
        return chain.proceed(request);
    }
}
