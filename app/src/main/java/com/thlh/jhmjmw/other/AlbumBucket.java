package com.thlh.jhmjmw.other;

import java.util.ArrayList;

/**
 * 相册目录
 */
public class AlbumBucket {
    private int count = 0;
    private String albumName;
    private ArrayList<AlbumItem> albumItems;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<AlbumItem> getAlbumItems() {
        return albumItems;
    }

    public void setAlbumItems(ArrayList<AlbumItem> albumItems) {
        this.albumItems = albumItems;
    }
}
