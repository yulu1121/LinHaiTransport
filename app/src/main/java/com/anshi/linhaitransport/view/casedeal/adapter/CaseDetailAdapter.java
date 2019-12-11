package com.anshi.linhaitransport.view.casedeal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.glide.GlideApp;

import java.util.List;

public class CaseDetailAdapter extends RecyclerView.Adapter<CaseDetailViewHolder> {
    private Context mContext;
    private List<String> mList;
    private  AddEnvClickListener addClickListener;
    public CaseDetailAdapter(Context context, List<String> mPhotoList, AddEnvClickListener addClickListener){
        this.mContext = context;
        this.mList = mPhotoList;
        this.addClickListener = addClickListener;
    }

    @NonNull
    @Override
    public CaseDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_iv_publish,viewGroup,false);
        return new CaseDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaseDetailViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (mList!=null&&position<mList.size()){
            String photoPath = mList.get(position);
            holder.mDeleteIv.setVisibility(View.VISIBLE);
            GlideApp.with(mContext).load(Constants.IMAGE_URL+photoPath).error(R.drawable.ease_default_image).into(holder.mDetailIv);

            holder.mDetailIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addClickListener.addEnvClick(false,position);
                }
            });
            holder.mDeleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addClickListener.addDeleEnvClick(position);
                }
            });
        }else {
            holder.mDeleteIv.setVisibility(View.GONE);
            holder.mDetailIv.setImageResource(R.drawable.add_photo);
            holder.mDetailIv.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.mDetailIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addClickListener.addEnvClick(true,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null==mList?1:mList.size()+1;
    }

    public interface  AddEnvClickListener{
        void addDeleEnvClick(int position);
        void addEnvClick(boolean isClick, int position);
    }
}
