package com.thlh.baselib.model.response;

import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.IceboxPhoto;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class AlbumResponse extends BaseResponse {

    /**
     * total : 3
     * count : 20
     * total_page : 1
     * current_page : 1
     * photos : [{"id":"2","photo":"/images/album/MzN/lNz/VmZ/1468600627318.jpeg"},{"id":"3","photo":"/images/album/MzN/lNz/VmZ/1468600627911.jpeg"},{"id":"1","photo":"/images/album/MzN/lNz/VmZ/1468600599187.jpeg"}]
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String total;
        private int count;
        private int total_page;
        private int current_page;
        private int screen_saver;
        /**
         * id : 2
         * photo : /images/album/MzN/lNz/VmZ/1468600627318.jpeg
         */

        private List<IceboxPhoto> photos;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public List<IceboxPhoto> getPhotos() {
            return photos;
        }

        public void setPhotos(List<IceboxPhoto> photos) {
            this.photos = photos;
        }

        public int getScreen_saver() {
            return screen_saver;
        }

        public void setScreen_saver(int screen_saver) {
            this.screen_saver = screen_saver;
        }
    }
}
