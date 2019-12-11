package com.anshi.linhaitransport.entry;

import android.graphics.Rect;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.previewlibrary.enitity.IThumbViewInfo;

public class ImageInfo implements IThumbViewInfo {


        private String url;  //图片地址
        private Rect mBounds; // 记录坐标
        private String user = "用户字段";
        private String videoUrl;
        private int imagePath;

        public ImageInfo(String url) {
            this.url = url;
        }
        public ImageInfo(String videoUrl, String url) {
            this.url = url;
            this.videoUrl = videoUrl;
        }
        public ImageInfo(int imagePath){
            this.imagePath = imagePath;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        @Override
        public String getUrl() {//将你的图片地址字段返回
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public Rect getBounds() {//将你的图片显示坐标字段返回
            return mBounds;
        }

        @Nullable
        @Override
        public String getVideoUrl() {
            return videoUrl;
        }

        public void setBounds(Rect bounds) {
            mBounds = bounds;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.url);
            dest.writeParcelable(this.mBounds, flags);
            dest.writeString(this.user);
            dest.writeString(this.videoUrl);
            dest.writeInt(this.imagePath);
        }

        protected ImageInfo(Parcel in) {
            this.url = in.readString();
            this.mBounds = in.readParcelable(Rect.class.getClassLoader());
            this.user = in.readString();
            this.videoUrl = in.readString();
            this.imagePath = in.readInt();
        }

        public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
            @Override
            public ImageInfo createFromParcel(Parcel source) {
                return new ImageInfo(source);
            }

            @Override
            public ImageInfo[] newArray(int size) {
                return new ImageInfo[size];
            }
        };

    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }
}
