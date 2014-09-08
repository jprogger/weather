package jprogger.org.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Items implements Parcelable {

    @SerializedName("cnt")
    public int count;

    @SerializedName("cod")
    public int code;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public Item[] list = new Item[]{};

    public Items() {}

    private Items(Parcel parcel) {
        count = parcel.readInt();
        code = parcel.readInt();
        message = parcel.readString();
        parcel.readTypedArray(list, Item.CREATOR);
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
        dest.writeInt(count);
        dest.writeInt(code);
        dest.writeString(message);
        dest.writeTypedArray(list, flags);
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
