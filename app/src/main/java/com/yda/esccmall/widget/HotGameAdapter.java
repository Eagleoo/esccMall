package com.yda.esccmall.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yda.esccmall.Bean.Work;
import com.yda.esccmall.R;

import java.util.List;

public class HotGameAdapter extends BaseAdapter {
    private List<Work> mFruitList;
    int[] icon=new int[]{R.drawable.zhuanpan,R.drawable.walk,R.drawable.pk,R.drawable.zhuanpan,R.drawable.walk};


//    public interface OnItemClickListener{
//        void onItemClick(int position);
//    }
//    private OnItemClickListener mItemClickListener;
//
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        // View fruitView;
//        ImageView fruitImage;
//        TextView fruitName;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            //fruitView = itemView;
//            fruitImage = (ImageView)itemView.findViewById(R.id.iv_item_icon);
//            fruitName = (TextView)itemView.findViewById(R.id.tv_item_name);
//        }
//    }
//
//    public HotGameAdapter(List<Work> fruitInfo) {
//        mFruitList = fruitInfo;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.hot_game_item,parent,false);
//        ViewHolder holder = new ViewHolder(view);
//        view.setOnClickListener(this);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        Work fruit = mFruitList.get(position);
//        holder.fruitImage.setImageResource(icon[position]);
//        holder.fruitName.setText(fruit.getTitle());
//        holder.itemView.setTag(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mFruitList.size();
//    }
//
//    public void setItemClickListener(OnItemClickListener itemClickListener) {
//        mItemClickListener = itemClickListener;
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (mItemClickListener!=null){
//            mItemClickListener.onItemClick((Integer) v.getTag());
//        }
//    }


    private Context mContext;

    public HotGameAdapter(Context context,List<Work> fruitInfo){
        this.mContext = context;
        this.mFruitList=fruitInfo;
    }
    @Override
    public int getCount() {
        return mFruitList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.hot_game_item, parent, false);
            holder.mImage=(ImageView)convertView.findViewById(R.id.iv_item_icon);
            holder.mTitle=(TextView)convertView.findViewById(R.id.tv_item_name);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.mTitle.setText(mFruitList.get(position).getTitle());
        holder.mImage.setImageResource(icon[position]);

        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle ;
        private ImageView mImage;
    }

}
