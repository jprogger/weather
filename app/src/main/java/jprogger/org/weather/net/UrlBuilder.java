package jprogger.org.weather.net;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Locale;

public class UrlBuilder {

    public static final String API_KEY_PARAM = "x-api-key";
    public static final String API_KEY_VALUE = "c5878d3275967f5c8e332f6bc45923f3";

    private Uri uri;
    private String unit = RestClient.Unit.METRIC;
    private ArrayList<Long> cityIds = new ArrayList<Long>();

    public UrlBuilder() {
        uri = new Uri.Builder()
                .scheme("http")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .build();
    }

    public UrlBuilder withWeatherCondition() {
        uri = uri.buildUpon().appendPath("weather").build();
        return this;
    }

    public UrlBuilder withGroupCities() {
         uri = uri.buildUpon().appendPath("group").build();
        return this;
    }

    public UrlBuilder appendCityId(Long cityId) {
        cityIds.add(cityId);
        return this;
    }

    public UrlBuilder withUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cityIds.size(); i++) {
            if (i > 0) builder.append(",");
            builder.append(cityIds.get(i));
        }
        return uri.buildUpon()
                .appendQueryParameter("id", builder.toString())
                .appendQueryParameter("units", unit)
                .build().toString();
    }

}
