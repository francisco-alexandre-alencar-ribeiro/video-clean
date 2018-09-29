package com.alexandrealencar.videoclean.entities;

public final class QueryHistory {


    private Long id;
    private String description;
    private String link;
    private Long dateUpdate;
    private Long dateCreate;
    private int visualized = 0;
    private int isFavorite = 0;
    private int currentPosition = 0;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public QueryHistory() {
    }

    public QueryHistory(String description, String link, Long dateCreate, Long dateUpdate) {
        this.id = id;
        this.description = description;
        this.link = link;
        this.dateCreate = dateCreate;
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

    public Long getDateUpdate() {
        return dateUpdate;
    }

    public Long getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(long dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setDateUpdate(Long dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public int getVisualized() {
        return visualized;
    }

    public void setVisualized(int visualized) {
        this.visualized = visualized;
    }


    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }
}
