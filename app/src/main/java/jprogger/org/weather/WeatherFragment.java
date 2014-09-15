package jprogger.org.weather;


import android.app.Fragment;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.neovisionaries.i18n.CountryCode;

import org.apache.commons.lang3.StringUtils;

import jprogger.org.weather.model.Item;
import jprogger.org.weather.net.ErrorHandlerBuilder;
import jprogger.org.weather.net.RestClient;
import jprogger.org.weather.net.UrlBuilder;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class WeatherFragment extends Fragment {

    private static final String TAG = "request";
    private static final String ITEM = "item";
    private static final String COLOR = "color";

    private int color;
    private Item item;
    private PullToRefreshLayout mPullToRefreshLayout;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(Item item, int color) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable(ITEM, item);
        args.putInt(COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = getArguments().getParcelable(ITEM);
            color = getArguments().getInt(COLOR);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ITEM, item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_fragment, container, false);
        rootView.setBackgroundColor(getResources().getColor(color));
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        refreshWeather();
                    }
                })
                .setup(mPullToRefreshLayout);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateContent(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        RestClient.cancelAllRequests(getActivity(), TAG);
    }

    private void refreshWeather() {
        StringRequest request = new RestClient.RequestBuilder()
                .appendTag(TAG)
                .appendUrl(new UrlBuilder()
                        .withWeatherCondition()
                        .withUnit(RestClient.Unit.METRIC)
                        .appendCityId(item.id)
                        .build())
                .appendListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        VolleyLog.e("Response: " + response.toString());
                        item = new Gson().fromJson(response, Item.class);
                        toggleProgress(false, mPullToRefreshLayout);
                        if (item.isSuccess()) {
                            updateContent(item);
                        } else {
                            Toast.makeText(getActivity(), R.string.error_resource_not_found_message, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .appendErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toggleProgress(false, mPullToRefreshLayout);
                        new ErrorHandlerBuilder().build().handleError(getActivity(), error);
                        VolleyLog.e("Response: " + error.toString());
                    }
                })
                .build();
        RestClient.addRequestToQueue(getActivity(), request);
    }

    private void toggleProgress(boolean loading, PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.setRefreshing(loading);
    }

    private void updateContent(Item item) {
        TextView locationView = (TextView) getView().findViewById(R.id.location_text);
        locationView.setText(new StringBuilder()
                .append(item.name)
                .append(", ")
                .append(CountryCode.getByCode(item.system.country).getName())
                .toString());

        TextView currentTempView = (TextView) getView().findViewById(R.id.current_temp_text);
        currentTempView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf"));
        currentTempView.setText(new StringBuilder()
                .append((int) item.main.temp)
                .append(" ")
                .append("\u00B0")
                .append("C")
                .toString());

        TextView conditionView = (TextView) getView().findViewById(R.id.condition_view);
        conditionView.setText(new StringBuilder()
                .append(getString(R.string.weather_condition))
                .append(": ")
                .append(StringUtils.capitalize(item.weathers[0].description))
                .toString());

        TextView humidityView = (TextView) getView().findViewById(R.id.humidity);
        humidityView.setText(new StringBuilder()
                .append(getString(R.string.humidity))
                .append(": ")
                .append(item.main.humidity)
                .append(" % ")
                .toString());
    }
}
