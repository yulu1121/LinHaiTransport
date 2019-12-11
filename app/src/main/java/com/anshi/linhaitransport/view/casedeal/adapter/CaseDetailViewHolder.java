package com.anshi.linhaitransport.view.casedeal.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.selfview.EaseImageView;

public class CaseDetailViewHolder extends RecyclerView.ViewHolder {
    public EaseImageView mDetailIv;
    public ImageView mDeleteIv;
    CaseDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        mDetailIv = itemView.findViewById(R.id.detail_iv);
        mDeleteIv = itemView.findViewById(R.id.delete_iv);
    }
}
