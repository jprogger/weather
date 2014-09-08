package jprogger.org.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    @SerializedName("id")
    public long id;
    @SerializedName("cod")
    public int code;
    @SerializedName("name")
    public String name;
    @SerializedName("message")
    public String message;
    @SerializedName("dt")
    public long dateTime;
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("sys")
    public System system;
    @SerializedName("weather")
    public Weather[] weathers;
    @SerializedName("coord")
    public Coordinates coordinates;

    public Item() {
    }

    private Item(Parcel parcel) {
        id = parcel.readLong();
        code = parcel.readInt();
        name = parcel.readString();
        message = parcel.readString();
        dateTime = parcel.readLong();
        main = parcel.readParcelable(Main.class.getClassLoader());
        wind = parcel.readParcelable(Wind.class.getClassLoader());
        system = parcel.readParcelable(System.class.getClassLoader());
        parcel.readTypedArray(weathers, Weather.CREATOR);
        coordinates = parcel.readParcelable(Coordinates.class.getClassLoader());
    }

    public boolean isSuccess() {
        return code == 200 && message == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeString(message);
        dest.writeLong(dateTime);
        dest.writeParcelable(main, flags);
        dest.writeParcelable(wind, flags);
        dest.writeParcelable(system, flags);
        dest.writeTypedArray(weathers, flags);
        dest.writeParcelable(coordinates, flags);
    }

    public static class Wind implements Parcelable {

        public static final Creator<Wind> CREATOR = new Creator<Wind>() {
            @Override
            public Wind createFromParcel(Parcel source) {
                return new Wind(source);
            }

            @Override
            public Wind[] newArray(int size) {
                return new Wind[size];
            }
        };
        @SerializedName("speed")
        public double speed;
        @SerializedName("deg")
        public double degree;

        public Wind() {
        }

        private Wind(Parcel parcel) {
            speed = parcel.readDouble();
            degree = parcel.readDouble();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(speed);
            dest.writeDouble(degree);
        }
    }

    public static class Weather implements Parcelable {

        public static final Creator<Weather> CREATOR = new Creator<Weather>() {
            @Override
            public Weather createFromParcel(Parcel source) {
                return new Weather(source);
            }

            @Override
            public Weather[] newArray(int size) {
                return new Weather[size];
            }
        };
        @SerializedName("id")
        public int id;
        @SerializedName("main")
        public String main;
        @SerializedName("icon")
        public String iconId;
        @SerializedName("description")
        public String description;

        public Weather() {
        }

        private Weather(Parcel parcel) {
            id = parcel.readInt();
            main = parcel.readString();
            iconId = parcel.readString();
            description = parcel.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(main);
            dest.writeString(iconId);
            dest.writeString(description);
        }
    }

    public static class System implements Parcelable {

        public static final Creator<System> CREATOR = new Creator<System>() {
            @Override
            public System createFromParcel(Parcel source) {
                return new System(source);
            }

            @Override
            public System[] newArray(int size) {
                return new System[size];
            }
        };
        @SerializedName("country")
        public String country;
        @SerializedName("sunrise")
        public long sunrise;
        @SerializedName("sunset")
        public long sunset;

        public System() {
        }

        private System(Parcel parcel) {
            country = parcel.readString();
            sunrise = parcel.readLong();
            sunset = parcel.readLong();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(country);
            dest.writeLong(sunrise);
            dest.writeLong(sunset);
        }
    }

    public static class Main implements Parcelable {

        public static final Creator<Main> CREATOR = new Creator<Main>() {
            @Override
            public Main createFromParcel(Parcel source) {
                return new Main(source);
            }

            @Override
            public Main[] newArray(int size) {
                return new Main[size];
            }
        };
        @SerializedName("temp")
        public double temp;
        @SerializedName("pressure")
        public int pressure;
        @SerializedName("humidity")
        public int humidity;
        @SerializedName("temp_min")
        public double tempMin;
        @SerializedName("temp_max")
        public double tempMax;

        public Main() {
        }

        private Main(Parcel parcel) {
            temp = parcel.readDouble();
            pressure = parcel.readInt();
            humidity = parcel.readInt();
            tempMin = parcel.readDouble();
            tempMax = parcel.readDouble();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(temp);
            dest.writeInt(pressure);
            dest.writeInt(humidity);
            dest.writeDouble(tempMin);
            dest.writeDouble(tempMax);
        }
    }

    public static class Coordinates implements Parcelable {

        public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
            @Override
            public Coordinates createFromParcel(Parcel source) {
                return new Coordinates(source);
            }

            @Override
            public Coordinates[] newArray(int size) {
                return new Coordinates[size];
            }
        };
        @SerializedName("lon")
        public double lon;
        @SerializedName("lat")
        public double lat;

        public Coordinates() {
        }

        private Coordinates(Parcel parcel) {
            lon = parcel.readDouble();
            lat = parcel.readDouble();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(lon);
            dest.writeDouble(lat);
        }
    }
}
