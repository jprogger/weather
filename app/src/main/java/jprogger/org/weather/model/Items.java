package jprogger.org.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Items implements Parcelable {

    @SerializedName("cnt")
    public int count;

    @SerializedName("cod")
    public int code;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public ArrayList<Item> list = new ArrayList<Item>();

    public Items() {}

    private Items(Parcel parcel) {

    }

    public boolean isSuccess() {
        return message == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel source) {
            return new Items(source);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };
}
