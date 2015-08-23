package br.com.delogic.asd.content;

public class UploadedFile {

    private String name;
    private Long   size;
    private String url;
    private String thumbnailUrl;
    private String deleteUrl;
    private String deleteType;
    private String originalName;

    public UploadedFile() {
        super();
    }

    public UploadedFile(String name, Long size, String url) {
        super();
        this.name = name;
        this.size = size;
        this.url = url;
    }

    public UploadedFile(String name, String original_name, Long size, String url,
        String thumbnail_url, String delete_url, String delete_type) {
        super();
        this.name = name;
        this.originalName = original_name;
        this.size = size;
        this.url = url;
        this.thumbnailUrl = thumbnail_url;
        this.deleteUrl = delete_url;
        this.deleteType = delete_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }

}