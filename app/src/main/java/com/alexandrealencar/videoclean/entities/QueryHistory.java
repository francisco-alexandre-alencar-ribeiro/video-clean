package com.alexandrealencar.videoclean.entities;

public final class QueryHistory {


    private Long id;
    private String description;
    private String link;
    private String dateUpdate;

    public QueryHistory(){}

    public QueryHistory(String description,String link,String dateUpdate) {
        this.id = id;
        this.description = description;
        this.link = link;
        this.dateUpdate = dateUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}
