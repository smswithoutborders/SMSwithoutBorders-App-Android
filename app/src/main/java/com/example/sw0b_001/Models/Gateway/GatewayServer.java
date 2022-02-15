package com.example.sw0b_001.Models.Gateway;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity(indices = {@Index(value={"number", "country_code"}, unique = true)})
@Entity
public class GatewayServer {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name="url")
    public String url;

    @ColumnInfo(name="publicKey")
    public String publicKey;

    public GatewayServer() {}

    public GatewayServer GatewayServer(String url) {
        this.url = url;
        return this;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPublicKey() {
        return this.publicKey;
    }
}
