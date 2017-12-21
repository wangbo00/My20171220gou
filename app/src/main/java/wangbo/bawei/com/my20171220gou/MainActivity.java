package wangbo.bawei.com.my20171220gou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wangbo.bawei.com.my20171220gou.adapter.ExpandableAdapter;
import wangbo.bawei.com.my20171220gou.bean.ChildBean;
import wangbo.bawei.com.my20171220gou.bean.GroupBean;
import wangbo.bawei.com.my20171220gou.bean.ShopCarBean;
import wangbo.bawei.com.my20171220gou.presenter.Presenter;
import wangbo.bawei.com.my20171220gou.view.ImView;

public class MainActivity extends AppCompatActivity implements ImView {

    private ExpandableListView exListView;
    public CheckBox allCheckbox;
    private TextView totalPrice;
    private TextView totalnumber;

    List<GroupBean> groupBeen=new ArrayList<>();
    List<List<ChildBean>> childBeen=new ArrayList<>();

    private Presenter presenter;
    private ExpandableAdapter expandableAdapter;
    private TextView edit;

    private boolean flagedit=true;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=(ImageView)findViewById(R.id.back);
        /*img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });*/
        getSupportActionBar().hide();

        initView();


        presenter = new Presenter();
        presenter.attachView(this);
        Map<String,String> map = new HashMap<>();
        presenter.get("http://120.27.23.105/product/getCarts?uid=100",map,"car", ShopCarBean.class);

        //获取二级列表适配器
        expandableAdapter = new ExpandableAdapter(MainActivity.this, groupBeen, childBeen);
        exListView.setAdapter(expandableAdapter);

        for(int i = 0; i < expandableAdapter.getGroupCount(); i++){

            exListView.expandGroup(i);

        }

        exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        //全选监听
        allCheckbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean checked = allCheckbox.isChecked();
                //改变一级item复选框
                for (GroupBean i:groupBeen){
                    i.setGropuCb(checked);
                }
                //改变二级item复选框
                for (List<ChildBean> i1:childBeen){
                    for(int r=0;r<i1.size();r++) {
                        i1.get(r).setChildCb(checked);
                    }
                }
                expandableAdapter.notifyDataSetChanged();
                changesum(childBeen);
                //Toast.makeText(Main2Activity.this,"全选按钮"+checked,Toast.LENGTH_SHORT).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (List<ChildBean> i1:childBeen){
                    for(int r=0;r<i1.size();r++) {
                        i1.get(r).setBtn(flagedit);
                    }
                }
                flagedit=!flagedit;
                expandableAdapter.notifyDataSetChanged();
            }
        });

    }
    //初始化控件
    private void initView() {
        exListView = (ExpandableListView) findViewById(R.id.exListView);
        allCheckbox = (CheckBox)findViewById(R.id.all_chekbox);
        totalPrice = (TextView)findViewById(R.id.total_price);
        totalnumber = (TextView)findViewById(R.id.total_number);
        edit = (TextView) findViewById(R.id.edit);
    }
    @Override
    public void onSuccess(String tag, Object o) {
        if(o!=null&&tag.equals("car")){
            ShopCarBean shopCarBean= (ShopCarBean) o;

            List<ShopCarBean.DataBean> data = shopCarBean.getData();
            for (ShopCarBean.DataBean i:data){
                GroupBean groupBean = new GroupBean(i.getSellerName(), false);
                this.groupBeen.add(groupBean);
                List<ShopCarBean.DataBean.ListBean> list = i.getList();
                List<ChildBean> ls=new ArrayList<>();
                for (ShopCarBean.DataBean.ListBean w:list){
                    String[] split = w.getImages().split("\\|");
                    ChildBean childBean = new ChildBean(w.getTitle(), split[0], w.getPrice(), 1, false,false);
                    ls.add(childBean);
                }
                this.childBeen.add(ls);

            }
            for(int i = 0; i < expandableAdapter.getGroupCount(); i++){
                exListView.expandGroup(i);
            }
            expandableAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onFailed(String tag, Exception e) {
        Toast.makeText(MainActivity.this,e.getMessage()+"",Toast.LENGTH_SHORT).show();
    }

   /* @Override
    public void success(String tag, Object o) {
        if(o!=null&&tag.equals("car")){
            ShopCarBean shopCarBean= (ShopCarBean) o;

            List<ShopCarBean.DataBean> data = shopCarBean.getData();
            for (ShopCarBean.DataBean i:data){
                GroupBean groupBean = new GroupBean(i.getSellerName(), false);
                this.groupBeen.add(groupBean);
                List<ShopCarBean.DataBean.ListBean> list = i.getList();
                List<ChildBean> ls=new ArrayList<>();
                for (ShopCarBean.DataBean.ListBean w:list){
                    String[] split = w.getImages().split("\\|");
                    ChildBean childBean = new ChildBean(w.getTitle(), split[0], w.getPrice(), 1, false,false);
                    ls.add(childBean);
                }
                this.childBeen.add(ls);

            }
            for(int i = 0; i < expandableAdapter.getGroupCount(); i++){
                exListView.expandGroup(i);
            }
            expandableAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void failed(String tag, Exception e) {
        Toast.makeText(Main2Activity.this,e.getMessage()+"",Toast.LENGTH_SHORT).show();
    }*/

    //计算和数量总价
    public void changesum(List<List<ChildBean>> childBeen){
        int count=0;
        double sum=0;
        for (List<ChildBean> i1:childBeen){
            for(int r=0;r<i1.size();r++) {
                boolean childCb1 = i1.get(r).isChildCb();
                if(childCb1){
                    double price = i1.get(r).getPrice();
                    int num = i1.get(r).getNum();
                    sum+=price*num;
                    count++;
                }
            }
        }
        totalPrice.setText("￥"+sum);
        totalnumber.setText("共有商品:"+count+"件");
    }

}