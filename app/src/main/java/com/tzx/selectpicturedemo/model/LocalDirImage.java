package com.tzx.selectpicturedemo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator
 * Date: 2016/3/17.
 */
public class LocalDirImage {
    public static final String FILE_NAME = "所有图片";
    private Map<String,List<LocalImage>> mData;
    private List<String> dirName;
    private List<String> firstMap;
    public LocalDirImage() {
        mData = new HashMap<String, List<LocalImage>>();
    }

    public LocalDirImage(Map<String, List<LocalImage>> mData) {
        this.mData = mData;
    }
    public void put(LocalImage image) {
        if (image != null) {
            String folderPath = getFolderPath(image);
            if (folderPath != null) {
                List<LocalImage> folder = mData.get(folderPath);
                if (folder == null) {
                    folder = new ArrayList<LocalImage>();
                }
                folder.add(image);
                mData.put(folderPath, folder);
            }
        }
    }

    public boolean add(String key, List<LocalImage> value) {
        if (value != null) {
            if (mData.containsKey(key)) {
                List<LocalImage> folder = mData.get(key);
                folder.addAll(value);
            } else {
                mData.put(key, value);
            }
            return true;
        }
        return false;
    }

    private String getFolderPath(LocalImage image) {
        if (image.path != null && !"".equals(image.path)) {
            int index = image.path.lastIndexOf("/");
            if (index >= 0) {
                return image.path.substring(0,index);
            }
        }
        return null;
    }

    public List<String> getAllDir() {
        if (dirName == null) {
            dirName = new ArrayList<>();
            dirName.add(0, FILE_NAME);
            for (String key : mData.keySet()) {
                dirName.add(key);
            }
        }
        return dirName;
    }
    public LocalImage getCurrentDirFirstMap(String key) {
        LocalImage firstMap = null;
        if (!FILE_NAME.equals(key) && mData.get(key) != null) {
            firstMap = mData.get(key).get(0);
        } else if (FILE_NAME.equals(key)) {
            for (String tmp : mData.keySet()) {
                firstMap = mData.get(tmp).get(0);
                break;
            }
        }
        if (firstMap == null) {
            firstMap = new LocalImage();
        }
        return firstMap;
    }

    public int getAllImageCount() {
        int count = 0;
        for (String key : mData.keySet()) {
            count+=mData.get(key).size();
        }
        return count;
    }

    public int getCurrentDirCount(String key) {
        if (FILE_NAME.equals(key)) {
            return getAllImageCount();
        }
        if (mData.get(key) == null){
            return 0;
        }
        return mData.get(key).size();
    }
}
