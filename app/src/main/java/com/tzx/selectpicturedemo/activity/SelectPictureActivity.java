package com.tzx.selectpicturedemo.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tzx.selectpicturedemo.R;
import com.tzx.selectpicturedemo.adapter.GLDividerItemDecoration;
import com.tzx.selectpicturedemo.adapter.SelectPictureGridAdapter;
import com.tzx.selectpicturedemo.fragment.PictureDirFragment;
import com.tzx.selectpicturedemo.model.LocalDirImage;
import com.tzx.selectpicturedemo.model.LocalImage;

import java.util.ArrayList;

/**
 * Created by Administrator
 * Date: 2016/3/14.
 */
public class SelectPictureActivity extends FragmentActivity implements View.OnClickListener{
    public static final String MAX_NUM_NAME = "MAX_NUM_NAME";
    private static final int COMPANY_SCAN_FILE = 110;
    private int maxPicNum;
    private int currentNum;
    private TextView headLeftTV;
    private TextView headCenterTv;
    private TextView headRightTV;
    private RecyclerView mRecyclerView;
    private TextView lookMoreTV;
    private TextView preViewTV;
    private SelectPictureGridAdapter adapter;
    ArrayList<LocalImage> list = new ArrayList<LocalImage>();
    private LocalDirImage dir = new LocalDirImage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_picture_layout);
        if (getIntent() != null) {
            maxPicNum = getIntent().getIntExtra(MAX_NUM_NAME, 1);
        }
        initView();
        initValue();
    }

    private void initView() {
        headCenterTv = (TextView) findViewById(R.id.head_center);
        headLeftTV = (TextView) findViewById(R.id.head_left);
        headRightTV = (TextView) findViewById(R.id.head_right);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        lookMoreTV = (TextView) findViewById(R.id.look_more);
        preViewTV = (TextView) findViewById(R.id.preview);

        headRightTV.setOnClickListener(this);
        headLeftTV.setOnClickListener(this);
        lookMoreTV.setOnClickListener(this);
        preViewTV.setOnClickListener(this);
    }

    private void initValue() {
        headRightTV.setVisibility(View.VISIBLE);
        //String txt = getResources().getString(R.string.complete_select_picture_tip);
        selectCompletePictureUpdateTV(0, headRightTV);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new SelectPictureGridAdapter(this,new CircularArray<String>());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new GLDividerItemDecoration(this));
        adapter.setSelectPictureItemOnClickListener(new MySelectPictureItemOnClickListener());
        getImages();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right:
                break;
            case R.id.look_more:
                startPicDirView();
                break;
            case R.id.preview:
                break;
            default:
                break;
        }
    }

    private void startPicDirView() {
        new PictureDirFragment(this,this.getSupportFragmentManager(),dir);
    }

    private class MySelectPictureItemOnClickListener extends SelectPictureGridAdapter.SelectPictureItemOnClickListener {

        @Override
        protected void onItemClick(View view, int position) {
            if (adapter.isCheck(position)) {
                adapter.setItemUnCheck(position);
                currentNum--;
                adapter.notifyItemChanged(position);
                selectCompletePictureUpdateTV(currentNum, headRightTV);
            } else {
                if (currentNum < maxPicNum) {
                    adapter.setItemCheck(position);
                    currentNum++;
                    selectCompletePictureUpdateTV(currentNum, headRightTV);
                    adapter.notifyItemChanged(position);
                } else {
                    Toast.makeText(SelectPictureActivity.this, "最多选择" + maxPicNum + "张图片", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COMPANY_SCAN_FILE:
                    for (int i = 0; i < list.size(); i++) {
                        adapter.getmData().addLast(list.get(i).getPath());
                    }
                    adapter.notifyDataSetChanged();
            }
        }
    };


    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        //mProgressDialog = ProgressDialog.show(this, null, "正在加载...");


        new Thread(new Runnable() {

            @Override
            public void run() {
                SparseArray<LocalImage> mImagePathMap = new SparseArray<LocalImage>();
                String[] proj = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
                String sortOrder = MediaStore.Images.Media.DATE_ADDED + " desc";
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        proj, null, null, sortOrder);
                if (cursor != null) {   //判断本地是否有图片
                    while (cursor.moveToNext()) {
                        LocalImage temp = new LocalImage();
                        temp.path = cursor.getString(1);
                        temp.thumbnails = null;
                        list.add(temp);
                        mImagePathMap.put(cursor.getInt(0), temp);
                        dir.put(temp);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }

                proj = new String[]{MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Video.Thumbnails.DATA};
                cursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                        proj, MediaStore.Images.Thumbnails.KIND + " = ?", new String[]{String.valueOf(MediaStore.Images.Thumbnails.MINI_KIND)}, null);
                if (cursor != null) {   //判断本地是否有图片
                    while (cursor.moveToNext()) {
                        LocalImage temp = mImagePathMap.get(cursor.getInt(0));
                        if (temp != null) {
                            temp.thumbnails = cursor.getString(1);
                        }
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                mImagePathMap.clear();
                // 通知Handler扫描图片完成
                mHandle.sendEmptyMessage(COMPANY_SCAN_FILE);
            }
        }).start();

    }
    public void selectCompletePictureUpdateTV(int currentNum, TextView view) {
        String txt = getResources().getString(R.string.complete_select_picture_tip);
        txt = txt.replace("currentnum", "" + currentNum);
        txt = txt.replace("maxnumber", "" + maxPicNum);
        view.setText(Html.fromHtml(txt));
    }
}
