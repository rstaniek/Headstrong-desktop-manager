package com.headstrongpro.desktop.model.resource;

/**
 * Created by rajmu on 17.05.08.
 */
class VideoResource implements Resource {

    private int id, type;
    private String name, description, url;
    private boolean isForAchievement;

    public VideoResource(int type, String name, String description, String url, boolean isForAchievement) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.url = url;
        this.isForAchievement = isForAchievement;
    }

    public VideoResource(int id, int type, String name, String description, String url, boolean isForAchievement) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.url = url;
        this.isForAchievement = isForAchievement;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isForAchievement() {
        return isForAchievement;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getID() {
        return id;
    }
}
