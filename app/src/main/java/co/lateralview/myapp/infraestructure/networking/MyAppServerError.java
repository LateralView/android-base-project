package co.lateralview.myapp.infraestructure.networking;

import com.google.gson.annotations.SerializedName;

public class MyAppServerError {
    @SerializedName("error_code")
    private Integer mErrorCode;

    @SerializedName("message")
    private String mErrorMessage;

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}