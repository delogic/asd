package br.com.delogic.asd.content;

public class File {

    private String name;
    private Long size;
    private String url;
    private String originalName;

    public File(String name, String originalName, Long size, String url) {
        this.name = name;
        this.originalName = originalName;
        this.size = size;
        this.url = url;
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

}