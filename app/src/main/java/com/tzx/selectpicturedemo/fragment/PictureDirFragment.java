package com.tzx.selectpicturedemo.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.tzx.selectpicturedemo.R;
import com.tzx.selectpicturedemo.adapter.PictureDirLLItemAdapter;
import com.tzx.selectpicturedemo.adapter.WrapLinearLayoutManager;
import com.tzx.selectpicturedemo.model.LocalDirImage;

/**
 * Created by Administrator
 * Date: 2016/3/15.
 */
public class PictureDirFragment extends Fragment {
    public static final String TAG = "PictureDirFragment";
    private Context mContext;
    private FragmentManager manager;
    private LayoutInflater inflater;
    private ViewGroup parentView;
    private View popView;
    private static boolean isPop = false;
    private View currentView;
    private RelativeLayout relativeLayout;
    private RecyclerView mRecyclerView;
    private PictureDirLLItemAdapter adapter;
    private LocalDirImage image;
    public PictureDirFragment(Context mContext, FragmentManager manager, LocalDirImage image) {
        this.mContext = mContext;
        this.manager = manager;
        this.image = image;
        popView();
    }

    public void popView() {
        if (!isPop) {
            isPop = true;
            FragmentTransaction transaction = this.manager.beginTransaction();
            //transaction.add(this, TAG);
            transaction.replace(R.id.select_picture_activity_layout, this);
            //transaction.replace(android.R.id.content, this);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    public void hideView() {
        if (isPop) {
            if (this.manager != null && this.manager.getBackStackEntryCount() != 0) {
                this.manager.popBackStack();
                FragmentTransaction transaction = this.manager.beginTransaction();
                transaction.remove(this);
                transaction.commitAllowingStateLoss();
                isPop = false;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        parentView = (ViewGroup) getActivity().getWindow().getDecorView();
        popView = createPopView();
        //parentView.addView(popView);
        //currentView.startAnimation(createAlphaInAnimation());
        //relativeLayout.startAnimation(createTranslationInAnimation());
        //return super.onCreateView(inflater, container, savedInstanceState);
        View relativeLayout = inflater.inflate(R.layout.picture_dir_fragment_layout, container, false);
        relativeLayout.setBackgroundColor(Color.argb(90, 0, 0, 0));
        View content = relativeLayout.findViewById(R.id.pircture_dri_fragment_layout);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideView();
            }
        });
        mRecyclerView = (RecyclerView) relativeLayout.findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new WrapLinearLayoutManager(this.mContext));
        adapter = new PictureDirLLItemAdapter(mContext, image);
        mRecyclerView.addItemDecoration(new LLDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        //relativeLayout.startAnimation(createTranslationInAnimation());
        return relativeLayout;
    }

    private View createPopView() {
        FrameLayout parent = new FrameLayout(getActivity());
        parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        currentView = new View(getActivity());
        currentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        currentView.setBackgroundColor(Color.argb(90, 0, 0, 0));
        currentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideView();
            }
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        params.setMargins(0, 0, 0, (int) mContext.getResources().getDimension(R.dimen.headbar_height));
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.picture_dir_fragment_layout, null);
        relativeLayout.setLayoutParams(params);
        parent.addView(currentView);
        parent.addView(relativeLayout);
        //mRecyclerView = (RecyclerView) relativeLayout.findViewById(R.id.id_recyclerview);
        return parent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currentView.startAnimation(createAlphaOutAnimation());
        relativeLayout.startAnimation(createTranslationOutAnimation());
        isPop = false;
        popView.postDelayed(new Runnable() {
            @Override
            public void run() {
                parentView.removeView(popView);
                parentView = null;
                popView = null;
                inflater = null;
                currentView = null;
                relativeLayout = null;
            }
        }, 300);
    }

    private Animation createAlphaInAnimation() {
        AlphaAnimation an = new AlphaAnimation(0, 1);
        an.setDuration(300);
        return an;
    }

    private Animation createAlphaOutAnimation() {
        AlphaAnimation an = new AlphaAnimation(1, 0);
        an.setDuration(300);
        an.setFillAfter(true);
        return an;
    }

    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        //float width = DensityUtil.gettDisplayHight(mContext) - getResources().getDimension(R.dimen.headbar_height);
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type, 1, type, 0);
        an.setDuration(300);
        return an;
    }

    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type, 0, type, 1);
        an.setDuration(300);
        an.setFillAfter(true);
        return an;
    }
}
