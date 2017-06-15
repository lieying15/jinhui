package com.thlh.jhmjmw.other;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 相册帮助类
 */
public class AlbumHelper {
    private final String TAG = getClass().getSimpleName();
    //是否已创建相册集
    private boolean hasBuildImagesBucketList = false;
    private Context context;
    private ContentResolver cr;

    // 缩略图列表
    private HashMap<String, String> thumbnailList = new HashMap<String, String>();
    // 专辑列表
    private  List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    private  HashMap<String, AlbumBucket> bucketList = new HashMap<String, AlbumBucket>();

    private static AlbumHelper instance;

    private AlbumHelper() {}

    public static AlbumHelper getHelper() {
        if (instance == null) {
            instance = new AlbumHelper();
        }
        return instance;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
        }
    }

    /**
     * 从数据库中得到缩略图
     */
    private void getThumbnail() {
        String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,null, null, null);
        if (cursor.moveToFirst()) {
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cursor.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);
            do {
                _id = cursor.getInt(_idColumn);
                image_id = cursor.getInt(image_idColumn);
                image_path = cursor.getString(dataColumn);
                thumbnailList.put("" + image_id, image_path);
            } while (cursor.moveToNext());
        }
    }


    /**
     * 得到原图
     */
    void getAlbum() {
        String[] projection = { Albums._ID, Albums.ALBUM, Albums.ALBUM_ART, Albums.ALBUM_KEY, Albums.ARTIST, Albums.NUMBER_OF_SONGS };
        Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null,null, null);
        getAlbumColumnData(cursor);
    }

    /**
     * 从本地数据库中得到原图
     *
     * @param cur
     */
    private void getAlbumColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            String album;
            String albumArt;
            String albumKey;
            String artist;
            int numOfSongs;
            int _idColumn = cur.getColumnIndex(Albums._ID);
            int albumColumn = cur.getColumnIndex(Albums.ALBUM);
            int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
            int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
            int artistColumn = cur.getColumnIndex(Albums.ARTIST);
            int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                album = cur.getString(albumColumn);
                albumArt = cur.getString(albumArtColumn);
                albumKey = cur.getString(albumKeyColumn);
                artist = cur.getString(artistColumn);
                numOfSongs = cur.getInt(numOfSongsColumn);
                // Do something with the values.
                Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt + "albumKey: " + albumKey + " artist: " + artist + " numOfSongs: " + numOfSongs + "---");
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("_id", _id + "");
                hash.put("album", album);
                hash.put("albumArt", albumArt);
                hash.put("albumKey", albumKey);
                hash.put("artist", artist);
                hash.put("numOfSongs", numOfSongs + "");
                albumList.add(hash);
            } while (cur.moveToNext());

        }
    }



    /**
     * 得到图片集
     */
    public void  buildImagesBucketList() {
        long startTime = System.currentTimeMillis();
        // 构造缩略图索引
        getThumbnail();
        // 构造相册索引
        String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
                Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
                Media.SIZE, Media.BUCKET_DISPLAY_NAME };
        // 得到一个游标
        Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,null);
        if (cur.moveToFirst()) {
            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
            // 获取图片总数
            int totalNum = cur.getCount();

            do {
                String _id = cur.getString(photoIDIndex);
                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
                String picasaId = cur.getString(picasaIdIndex);

                L.d(TAG, _id + ", bucketId: " + bucketId + ", picasaId: "
                        + picasaId + " name:" + name + " path:" + path
                        + " title: " + title + " size: " + size + " bucket: "
                        + bucketName + "---");

                AlbumBucket bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new AlbumBucket();
                    bucketList.put(bucketId, bucket);
                    bucket.setAlbumItems(new ArrayList<AlbumItem>());
                    bucket.setAlbumName(bucketName);
                }
                int count = bucket.getCount() +1;
                bucket.setCount(count);
                AlbumItem albumItem = new AlbumItem();
                albumItem.setImageId(_id);
                albumItem.setImagePath(path);
                albumItem.setThumbnailPath(thumbnailList.get(_id));
                bucket.getAlbumItems().add(albumItem);

            } while (cur.moveToNext());
        }

        Iterator<Map.Entry<String, AlbumBucket>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, AlbumBucket> entry = (Map.Entry<String, AlbumBucket>) itr.next();
            AlbumBucket bucket = entry.getValue();
            Log.d(TAG, entry.getKey() + ", " + bucket.getAlbumName() + ", " + bucket.getCount() + " ---------- ");
            for (int i = 0; i < bucket.getAlbumItems().size(); ++i) {
                AlbumItem image = bucket.getAlbumItems().get(i);
                Log.d(TAG, "----- " + image.getImageId() + ", " + image.getImagePath() + ", " + image.getThumbnailPath());
            }
        }
        hasBuildImagesBucketList = true;
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
    }

    /**
     * 得到图片集
     *
     * @param refresh
     * @return
     */
    public List<AlbumBucket> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        List<AlbumBucket> tmpList = new ArrayList<AlbumBucket>();
        Iterator<Map.Entry<String, AlbumBucket>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, AlbumBucket> entry = (Map.Entry<String, AlbumBucket>) itr.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    /**
     * 得到原始图像路径
     *
     * @param image_id
     * @return
     */
    String getOriginalImagePath(String image_id) {
        String path = null;
        Log.i(TAG, "---(^o^)----" + image_id);
        String[] projection = { Media._ID, Media.DATA };
        Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "=" + image_id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(Media.DATA));

        }
        return path;
    }

}
