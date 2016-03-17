package com.tzx.selectpicturedemo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tzx.selectpicturedemo.R;
import com.tzx.selectpicturedemo.common.DensityUtil;

/**
 * Created by Administrator
 * Date: 2016/3/15.
 */
public class SelectPictureGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    public static final String TAG = "SelectPictureGridAdapter";
    public static final String RESOURCE_SRC = "res://com.tzx.selectpicturedemo/";
    public static final String FILE_PREFIX = "file://";
    public static final int TYPE_NORMAL_ITEM = 0;
    public static final int TYPE_TAKE_PHOTO_ITEM = 1;
    public static final String TYPE_TAKE_PHOTO_ITEM_TITLE = "TACK_PHOTO";
    private Context mContext;
    private CircularArray<String> mData;
    //selectPictureStr = [1] + [2]
    private String selectPictureStr = "";
    private SelectPictureItemOnClickListener selectPictureItemOnClickListener;
    private ResizeOptions resizeOptions;
    private int mWidth;
    public SelectPictureGridAdapter(Context mContext, CircularArray<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mWidth = (DensityUtil.gettDisplayWidth(mContext)) / 3;
        resizeOptions = new ResizeOptions(mWidth, mWidth);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;
        if (viewType == TYPE_TAKE_PHOTO_ITEM) {
        } else {
        }
        holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_pircture_grid_adapter_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_TAKE_PHOTO_ITEM) {

        } else {

        }
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            if (isCheck(position)) {
                myViewHolder.imageView.setImageResource(R.drawable.option_checked);
            } else {
                myViewHolder.imageView.setImageResource(R.drawable.option_check);
            }
            //showThumb(Uri.parse(FILE_PREFIX + mData.get(position)), myViewHolder.simpleDraweeView);
            ViewGroup.LayoutParams params = myViewHolder.simpleDraweeView.getLayoutParams();
            params.width = mWidth;
            params.height = mWidth;
            myViewHolder.simpleDraweeView.setImageURI(Uri.parse(FILE_PREFIX + mData.get(position)));
            myViewHolder.imageView.setTag(position);
            myViewHolder.imageView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String str = mData.get(position);
        if (TYPE_TAKE_PHOTO_ITEM_TITLE.equals(str)) {
            return TYPE_TAKE_PHOTO_ITEM;
        } else {
            return TYPE_NORMAL_ITEM;
        }
    }

    public CircularArray<String> getmData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setSelectPictureItemOnClickListener(SelectPictureItemOnClickListener selectPictureItemOnClickListener) {
        this.selectPictureItemOnClickListener = selectPictureItemOnClickListener;
    }

    public boolean isCheck(int postion) {
        String str = "[" + postion + "]";
        return selectPictureStr.contains(str);
    }

    public void setItemCheck(int postion) {
        String str = "[" + postion + "]";
        selectPictureStr += str;
    }

    public void setItemUnCheck(int postion) {
        String str = "[" + postion + "]";
        selectPictureStr = selectPictureStr.replace(str,"");
    }

    @Override
    public void onClick(View v) {
        if (selectPictureItemOnClickListener != null) {
            selectPictureItemOnClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public static abstract class SelectPictureItemOnClickListener{

        protected abstract void onItemClick(View view, int position);
    }

    private void showThumb(Uri uri, SimpleDraweeView view){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(resizeOptions)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(view.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        view.setController(controller);

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView simpleDraweeView;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.id_picture);
            imageView = (ImageView) itemView.findViewById(R.id.id_check);
        }
    }
}
