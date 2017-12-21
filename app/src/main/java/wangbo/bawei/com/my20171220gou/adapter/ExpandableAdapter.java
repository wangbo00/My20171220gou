package wangbo.bawei.com.my20171220gou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import wangbo.bawei.com.my20171220gou.MainActivity;
import wangbo.bawei.com.my20171220gou.R;
import wangbo.bawei.com.my20171220gou.bean.ChildBean;
import wangbo.bawei.com.my20171220gou.bean.GroupBean;
import wangbo.bawei.com.my20171220gou.view.AddDeleteView;

/**
 * Created by 杨文倩 on 2017/12/11.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
// private static final String TAG = "ExpandableAdapter二级列表适配器";

    private Context context;
    private List<GroupBean> groupBeen=new ArrayList<>();
    private List<List<ChildBean>> childBeen=new ArrayList<>();

    public ExpandableAdapter(Context context, List<GroupBean> groupBeen, List<List<ChildBean>> childBeen) {
        this.context = context;
        this.groupBeen = groupBeen;
        this.childBeen = childBeen;
    }

    @Override
    public int getGroupCount() {
        return groupBeen.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childBeen.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupBeen.get(i).getSellerName();
    }

    @Override
    public Object getChild(int i, int i1) {
        return childBeen.get(i).get(i1).getTitle();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //一级组
    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        //加载视图
        view=View.inflate(context, R.layout.ex_group_item ,null);

        final CheckBox groupCb= (CheckBox) view.findViewById(R.id.group_checkbox);
        TextView shopName= (TextView) view.findViewById(R.id.shop_name);


        shopName.setText(groupBeen.get(i).getSellerName());
        groupCb.setChecked(groupBeen.get(i).isGropuCb());

        //组复选按钮
        groupCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean gchecked = groupCb.isChecked();
                groupBeen.get(i).setGropuCb(gchecked);
                MainActivity main= (MainActivity) context;
                for(GroupBean i: groupBeen){
                    boolean gropuCb = i.isGropuCb();
                    if(!gropuCb){
                        main.allCheckbox.setChecked(false);
                        break;
                    }else{
                        main.allCheckbox.setChecked(true);
                    }
                }
                int size = childBeen.get(i).size();
                if(gchecked){
                    for(int r=0;r<size;r++){
                        //Toast.makeText(context,"group按钮"+ gchecked+""+size, Toast.LENGTH_SHORT).show();
                        childBeen.get(i).get(r).setChildCb(true);
                    }
                }else{
                    for(int r=0;r<size;r++){
                        //Toast.makeText(context,"group按钮"+ gchecked+""+size, Toast.LENGTH_SHORT).show();
                        childBeen.get(i).get(r).setChildCb(false);
                    }
                }
                notifyDataSetChanged();
                main.changesum(childBeen);
            }
        });
        return view;
    }

    //二级组
    @Override
    public View getChildView(final int i, final int i1, boolean b, View v, ViewGroup viewGroup) {
        //加载视图
        v=View.inflate(context, R.layout.ex_child_item ,null);

        final CheckBox childCb = (CheckBox) v.findViewById(R.id.child_checkbox);
        TextView shopTitle= (TextView) v.findViewById(R.id.shop_title);
        TextView shopPrice= (TextView) v.findViewById(R.id.shop_price);
        ImageView shopImg= (ImageView) v.findViewById(R.id.shop_img);
        final AddDeleteView adv = (AddDeleteView) v.findViewById(R.id.adv);
        Button shop_delete= (Button) v.findViewById(R.id.shop_delete);

        childCb.setChecked(childBeen.get(i).get(i1).isChildCb());
        Glide.with(context).load(childBeen.get(i).get(i1).getImages()).into(shopImg);
        shopTitle.setText(childBeen.get(i).get(i1).getTitle());
        shopPrice.setText(childBeen.get(i).get(i1).getPrice()+"");
        adv.setNumber(childBeen.get(i).get(i1).getNum());

        final MainActivity main= (MainActivity) context;
        //控制删除按钮的显隐
        if(childBeen.get(i).get(i1).isBtn()){
            shop_delete.setVisibility(View.VISIBLE);
        }else{
            shop_delete.setVisibility(View.INVISIBLE);
        }
        //删除按钮监听
        shop_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = childBeen.get(i).size();
                if(size==1){
                    childBeen.remove(i);
                    groupBeen.remove(i);
                }else{
                    childBeen.get(i).remove(i1);
                }
                //点击删除后隐藏所有删除按钮
                for (List<ChildBean> i1:childBeen){
                    for(int r=0;r<i1.size();r++) {
                        i1.get(r).setBtn(false);
                    }
                }
                notifyDataSetChanged();
                main.changesum(childBeen);
            }
        });

        //加减器逻辑

        adv.setOnAddDelClickListener(new AddDeleteView.OnAddDelClickListener() {
            @Override
            public void onAddClick(View v) {
                int number = adv.getNumber();
                number++;
                adv.setNumber(number);
                childBeen.get(i).get(i1).setNum(number);
                main.changesum(childBeen);
            }

            @Override
            public void onDelClick(View v) {
                int number = adv.getNumber();
                number--;
                adv.setNumber(number);
                childBeen.get(i).get(i1).setNum(number);
                main.changesum(childBeen);
            }
        });

        //二级组的复选框监听
        childCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag=false;
                boolean cchecked = childCb.isChecked();
                childBeen.get(i).get(i1).setChildCb(cchecked);
                //Toast.makeText(context,"child按钮"+ cchecked+""+i1, Toast.LENGTH_SHORT).show();
                MainActivity main= (MainActivity) context;
                for (List<ChildBean> i1:childBeen){
                    for(int r=0;r<i1.size();r++) {
                        boolean childCb1 = i1.get(r).isChildCb();
                        if(!childCb1){
                            main.allCheckbox.setChecked(false);
                            groupBeen.get(i).setGropuCb(false);
                            flag=true;
                            break;
                        }else{
                            main.allCheckbox.setChecked(true);
                            groupBeen.get(i).setGropuCb(true);
                        }
                    }
                    if(flag){
                        break;
                    }
                }

                int size = childBeen.get(i).size();
                for(int x=0;x<size;x++) {
                    boolean childCb1 = childBeen.get(i).get(x).isChildCb();
                    if(!childCb1){
                        groupBeen.get(i).setGropuCb(false);
                        break;
                    }else{
                        groupBeen.get(i).setGropuCb(true);
                    }
                }
                notifyDataSetChanged();
                main.changesum(childBeen);
            }
        });


        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
