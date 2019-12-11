package com.anshi.linhaitransport.view.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.view.home.child.CommonChildFrag;

import java.util.ArrayList;
import java.util.List;

public class HomeFrag extends Fragment {
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    public static HomeFrag getInstance(){
        return new HomeFrag();
    }
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.white);
        StatusBarUtils.setStatusTextColor(true,getActivity());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (this!=null&&!hidden){
            StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.white);
            StatusBarUtils.setStatusTextColor(true,getActivity());
        }
    }

    private void initView(View view) {
        mTabLayout = view.findViewById(R.id.home_tab);
        mViewPager = view.findViewById(R.id.home_vp);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        completeFragments();
    }

    private void  completeFragments(){
        mTitles.add("天气信息");
        mFragments.add(CommonChildFrag.getInstance("天气信息"));
        mTitles.add("路况信息");
        mFragments.add(CommonChildFrag.getInstance("路况信息"));
        mTitles.add("交通信息");
        mFragments.add(CommonChildFrag.getInstance("交通信息"));
        mTitles.add("失业信息查询");
        mFragments.add(CommonChildFrag.getInstance("失业信息查询"));
        ViewPagerFragAdapter viewPagerFragAdapter = new ViewPagerFragAdapter(getChildFragmentManager());
        mViewPager.setAdapter(viewPagerFragAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    class  ViewPagerFragAdapter extends FragmentPagerAdapter{

        ViewPagerFragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return null==mFragments?0:mFragments.size();
        }

    }
}
