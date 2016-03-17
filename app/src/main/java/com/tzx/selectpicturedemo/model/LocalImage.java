package com.tzx.selectpicturedemo.model;

import android.text.TextUtils;

import java.io.File;

/**
 * Created by Administrator
 * Date: 2016/3/15.
 */
public class LocalImage {
    /** 图片的真实路径 */
    public String path;
    /** 图片对应的系统缩略图的路径。可能为null。 */
    public String thumbnails;
    /**是否被选中*/
    public boolean isSelected = false;

    /**
     * 获取图片的显示路径
     * @return 有缩略图时返回<b> 缩略图路径 </b>，否则返回图片<b> 真实路径 </b>
     */
    public String getPath() {
        if (!TextUtils.isEmpty(thumbnails)) {
            if (new File(thumbnails).exists()) {
                return thumbnails;
            }
            thumbnails = null;
        }
        return path;
    }
}
