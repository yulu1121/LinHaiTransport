package com.anshi.linhaitransport.entry;

public class ContentEntry {
    private int imageSrc;
    private String contentDesc;

    public ContentEntry(int imageSrc, String contentDesc) {
        this.imageSrc = imageSrc;
        this.contentDesc = contentDesc;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }
}
