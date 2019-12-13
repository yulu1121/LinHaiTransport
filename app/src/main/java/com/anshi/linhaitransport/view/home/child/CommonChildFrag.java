package com.anshi.linhaitransport.view.home.child;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseApplication;
import com.anshi.linhaitransport.entry.ContentEntry;
import com.anshi.linhaitransport.entry.NewsEntry;
import com.anshi.linhaitransport.net.AppHttpService;
import com.anshi.linhaitransport.selfview.CanScorllRecyclerView;
import com.anshi.linhaitransport.selfview.EaseImageView;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.Utils;
import com.anshi.linhaitransport.utils.glide.GlideApp;
import com.anshi.linhaitransport.view.roadmanager.RoadManagerActivity;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CommonChildFrag extends Fragment {
    private Context mContext;
    private Banner mBanner;
    private RecyclerView mContentRecycler;
    private CanScorllRecyclerView mInnerRecycler;
    private CanScorllRecyclerView mOutRecycler;
    private List<String> mImageList = new ArrayList<>();
    private AppHttpService mService;
    private List<NewsEntry.DataBean> mOutList = new ArrayList<>();
    private CommonAdapter<NewsEntry.DataBean> mOutAdapter;
    private List<NewsEntry.DataBean> mInnerList = new ArrayList<>();
    private CommonAdapter<NewsEntry.DataBean> mInnerAdapter;
    public static final String COMMON_TYPE = "common_type";
    public static CommonChildFrag getInstance(String type){
        CommonChildFrag commonChildFrag = new CommonChildFrag();
        Bundle bundle = new Bundle();
        bundle.putString(COMMON_TYPE,type);
        commonChildFrag.setArguments(bundle);
        return commonChildFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null!=getArguments()){
            String type = getArguments().getString(COMMON_TYPE);
            Log.e("xxx",type);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mService = BaseApplication.getInstances().getAppRetrofit().create(AppHttpService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_common_child,container,false);
        initView(view);
        return view;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (this!=null&&!hidden){
            StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.top_blue);

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.top_blue);
    }

    private void initView(View view) {
        mBanner = view.findViewById(R.id.home_banner);
        mBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mContentRecycler = view.findViewById(R.id.common_recycler);
        mContentRecycler.setLayoutManager(new GridLayoutManager(mContext,5){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mOutRecycler = view.findViewById(R.id.out_recyler);
        mOutRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mInnerRecycler = view.findViewById(R.id.inner_recyler);
        mInnerRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    private void getOutNews(){
        mService.newList()
                .map(new Func1<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody call(ResponseBody responseBody) {
                        return responseBody;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string();
                            if (Utils.isGoodJson(string)){
                                Gson gson = new Gson();
                                NewsEntry newsEntry = gson.fromJson(string, NewsEntry.class);
                                if (newsEntry.getCode()==Constants.SUCCESS_CODE){
                                    if (newsEntry.getData()!=null&&newsEntry.getData().size()>0){
                                        for (int i = 0; i <newsEntry.getData().size() ; i++) {
                                            NewsEntry.DataBean dataBean = newsEntry.getData().get(i);
                                            if (dataBean.getNoticeType().equals("2")){
                                                mInnerList.add(dataBean);
                                            }else {
                                                mOutList.add(dataBean);
                                            }
                                        }
                                        mOutAdapter.notifyDataSetChanged();
                                        mInnerAdapter.notifyDataSetChanged();
                                    }
                                }else {
                                    Toast.makeText(mContext, newsEntry.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    private void completeNewsRecycler(){
        mOutAdapter = new CommonAdapter<NewsEntry.DataBean>(mContext,R.layout.item_common_news,mOutList) {
            @Override
            protected void convert(ViewHolder holder, final NewsEntry.DataBean dataBean, int position) {
                EaseImageView iv = holder.getView(R.id.pay_iv);
                TextView contentTv = holder.getView(R.id.pay_trans_tv);
                TextView timeTv = holder.getView(R.id.time_tv);
                GlideApp.with(mContext).load(Constants.IMAGE_URL+dataBean.getLogoImg()).error(R.drawable.ease_default_image).into(iv);
                contentTv.setText(dataBean.getNoticeTitle());
                timeTv.setText(dataBean.getCreateTime());
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, RoadManagerActivity.class);
                        intent.putExtra("id",dataBean.getNoticeId());
                        intent.putExtra("title",dataBean.getNoticeTitle());
                        startActivity(intent);
                    }
                });
            }
        };
        mInnerAdapter = new CommonAdapter<NewsEntry.DataBean>(mContext,R.layout.item_common_news,mInnerList) {
            @Override
            protected void convert(ViewHolder holder, final NewsEntry.DataBean dataBean, int position) {
                EaseImageView iv = holder.getView(R.id.pay_iv);
                TextView contentTv = holder.getView(R.id.pay_trans_tv);
                TextView timeTv = holder.getView(R.id.time_tv);
                GlideApp.with(mContext).load(Constants.IMAGE_URL+dataBean.getLogoImg()).error(R.drawable.ease_default_image).into(iv);
                contentTv.setText(dataBean.getNoticeTitle());
                timeTv.setText(dataBean.getCreateTime());
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, RoadManagerActivity.class);
                        intent.putExtra("id",dataBean.getNoticeId());
                        intent.putExtra("title",dataBean.getNoticeTitle());
                        startActivity(intent);
                    }
                });
            }
        };
        mOutRecycler.setAdapter(mOutAdapter);
        mInnerRecycler.setAdapter(mInnerAdapter);
    }


    private void completeContentRecycler(){
        List<ContentEntry> list = new ArrayList<>();
        list.add(new ContentEntry(R.drawable.pg_location_query,"公交查询"));
        list.add(new ContentEntry(R.drawable.pg_taxi,"一键打车"));
        list.add(new ContentEntry(R.drawable.pg_travel_car,"旅游包车"));
        list.add(new ContentEntry(R.drawable.pg_road_query,"路况查询"));
        list.add(new ContentEntry(R.drawable.pg_road,"公路分布"));
        CommonAdapter<ContentEntry> contentEntryCommonAdapter = new CommonAdapter<ContentEntry>( mContext,R.layout.item_home_content,list) {
            @Override
            protected void convert(ViewHolder holder, ContentEntry contentEntry, int position) {
                ImageView imageView =holder.getView(R.id.item_iv);
                TextView textView = holder.getView(R.id.item_location);
                GlideApp.with(mContext).load(contentEntry.getImageSrc()).into(imageView);
                textView.setText(contentEntry.getContentDesc());
            }
        };
        mContentRecycler.setAdapter(contentEntryCommonAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadImageList(mBanner);
        completeContentRecycler();
        completeNewsRecycler();
        getOutNews();
    }





    private void loadImageList(Banner banner){
        mImageList.add("http://www.gov.cn/govweb/c1293/201911/5457125/images/e69a08d807a845b49e16e1b137dab5c7.jpg");
        mImageList.add("http://www.gov.cn/govweb/c1293/201912/5457487/images/a340282d2ed74220a9b041476dd2f388.jpg");
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(mImageList);
        banner.start();
    }
    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            String realPath = (String) path;
            GlideApp.with(context).load(realPath).centerCrop().into(imageView);
        }
    }

}
