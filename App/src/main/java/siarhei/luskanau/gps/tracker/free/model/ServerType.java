package siarhei.luskanau.gps.tracker.free.model;

import com.google.gson.annotations.SerializedName;

public enum ServerType {

    @SerializedName("socket")
    SOCKET,
    @SerializedName("json")
    JSON,
    @SerializedName("json_form")
    JSON_FORM,
    @SerializedName("wialon")
    WIALON

}
