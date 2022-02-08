package com.example.sw0b_001.Models.Gateway;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import com.example.sw0b_001.Database.Datastore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Entity(indices = {@Index(value={"number", "country_code"}, unique = true)})
public class GatewayClient {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name="MSISDN")
    String MSISDN;

    @ColumnInfo(name="default")
    boolean _default = false;

    @ColumnInfo(name="operatorName")
    String operatorName;

    public GatewayClient() {}
    public GatewayClient(String MSISDN, String operatorName, boolean _default) {
        this.MSISDN = MSISDN;
        this.operatorName = operatorName;
        this._default = _default;
    }

    public long getId() {
        return id;
    }

    public String getMSISDN() {
        return this.MSISDN;
    }

    public String getOperatorName() {
        return this.operatorName;
    }

    public boolean isDefault() {
        return this._default;
    }

    public GatewayClient setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
        return this;
    }

    public GatewayClient setOperatorName(String operatorName) {
        this.operatorName = operatorName;
        return this;
    }

    public GatewayClient setDefault(boolean _default) {
        this._default = _default;
        return this;
    }


}
