package wangbo.bawei.com.my20171220gou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import wangbo.bawei.com.my20171220gou.R;
import wangbo.bawei.com.my20171220gou.bean.ImageBean;

/**
 * Created by 杨文倩 on 2017/12/11.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<ImageBean.DataBean.DefaultGoodsListBean> listBeen;
    Context context;

    public MyAdapter(List<ImageBean.DataBean.DefaultGoodsListBean> listBeen, Context context) {
        this.listBeen = listBeen;
        this.context = context;
    }

    public interface OnItemClieckLinster{

        void onItemClickListener(View view, int pos);
        void onItemLongClickListener(View view, int pos);
    }
    private  OnItemClieckLinster onItemClieckLinster;

    public void setOnItemClieckLinster(OnItemClieckLinster listener){

        this.onItemClieckLinster = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= View.inflate(context, R.layout.item,null);
        ViewHolder vh=new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(listBeen.get(position).getGoods_img()).into(holder.img);
        //   holder.text2.setText(listBeen.get(position).getTitle());
       /* holder.text.setText(listBeen.get(position).getGoods_name());
         holder.text2.setText(listBeen.get(position).getEfficacy());*/

        if(onItemClieckLinster != null){

            //onitemclicklistener
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClieckLinster.onItemClickListener(holder.itemView , position);
                }
            });

            //onitemlongclicklistener
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    onItemClieckLinster.onItemLongClickListener(holder.itemView , position);
                    return false;
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return listBeen.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        //TextView text,text2;


        public ViewHolder(View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.img);
            /*text=(TextView)itemView.findViewById(R.id.text);
            text2=(TextView)itemView.findViewById(R.id.text2);*/
        }
    }


}
