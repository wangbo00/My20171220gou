package wangbo.bawei.com.my20171220gou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wangbo.bawei.com.my20171220gou.adapter.MyAdapter;
import wangbo.bawei.com.my20171220gou.bean.ImageBean;
import wangbo.bawei.com.my20171220gou.presenter.Presenter;
import wangbo.bawei.com.my20171220gou.view.ImView;

public class Main3Activity extends AppCompatActivity implements ImView{
    private XRecyclerView recyclerView;
    MyAdapter adapter;
    Presenter presenter;
    List<ImageBean.DataBean.DefaultGoodsListBean> listBeen=new ArrayList<>();
    //XRecyclerView xRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getSupportActionBar().hide();
        recyclerView=(XRecyclerView)findViewById(R.id.recy);
       /* //刷新
        recyclerView.setLoadingListener(new RecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                //recyclerView.refreshComplete();
            }
        });*/
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {

            }
        });

        adapter=new MyAdapter(listBeen,Main3Activity.this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClieckLinster(new MyAdapter.OnItemClieckLinster() {
            @Override
            public void onItemClickListener(View view, int pos) {
                Intent intent=new Intent(Main3Activity.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClickListener(View view, int pos) {
                Toast.makeText(Main3Activity.this, "长按点击事件" + pos , Toast.LENGTH_SHORT).show();

            }
        });


        Map<String,String> map = new HashMap<>();
        presenter=new Presenter();
        presenter.attachView(this);

        presenter.get("http://result.eolinker.com/umIPmfS6c83237d9c70c7c9510c9b0f97171a308d13b611?uri=homepage",map,
                "img", ImageBean.class);

    }

    @Override
    public void onSuccess(String tag, Object o) {
        if(tag.equals("img")){

            ImageBean imgBean= (ImageBean) o;
            List<ImageBean.DataBean.DefaultGoodsListBean>defaultGoodsList=imgBean.getData().getDefaultGoodsList();
            listBeen.addAll(defaultGoodsList);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFailed(String tag, Exception e) {
        Toast.makeText(Main3Activity.this,"你已进入没有网络的异次元",Toast.LENGTH_SHORT).show();
    }

    //销毁线程 资源释放
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.deleteView();
        }
    }
}