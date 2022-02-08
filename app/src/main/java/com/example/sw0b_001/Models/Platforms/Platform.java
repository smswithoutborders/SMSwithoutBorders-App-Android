package com.example.sw0b_001.Models.Platforms;

import android.content.Context;
import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.sw0b_001.EmailThreadsActivity;
import com.example.sw0b_001.TextThreadActivity;

//@DatabaseView("SELECT platform.name, platform.description, platform.provider, platform.image, platform.id FROM platform")
@Entity(indices = {@Index(value={"name"}, unique = true)})
public class Platform {
    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="image")
    public int image;

    @ColumnInfo(name="short_name")
    public String shortName;

    @ColumnInfo(name="type")
    public String type;

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String getShortName() {
        return this.shortName;
    }

    public Platform() {
    }

    public Platform(long id) {
        this.id = id;
    }

    public Platform setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public Platform setType(String type) {
        this.type = type;
        return this;
    }

    public Platform setName(String name) {
        this.name = name;
        return this;
    }

    public Platform setImage(int image) {
        this.image = image;
        return this;
    }

    public Platform setId(long id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getImage() {
        return this.image;
    }

    public long getId() {
        return id;
    }
}
