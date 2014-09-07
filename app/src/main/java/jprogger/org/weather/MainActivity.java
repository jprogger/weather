package jprogger.org.weather;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import jprogger.org.weather.model.Items;
import jprogger.org.weather.net.RestClient;
import jprogger.org.weather.net.UrlBuilder;


public class MainActivity extends Activity implements RetryActionFragment.RetryActionListener {

    private static final String TAG = "Weather";

    private Items items;
    private int position;
    private ViewPager mViewPager;
    private PagerAdapter pagerAdapter;

    private ProgressFragment progressFragment;
    private RetryActionFragment retryFragment;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        getActionBar().hide();
        setContentView(R.layout.main);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "Selected position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (state != null) {
            position = state.getInt("position");
            items = state.getParcelable("items");
            pagerAdapter = new PagerAdapter(getFragmentManager(), items);
            mViewPager.setAdapter(pagerAdapter);
            mViewPager.setCurrentItem(position);
        } else {
            refreshWeather();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RestClient.cancelAllRequests(this, TAG);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("items", items);
        outState.putInt("position", mViewPager.getCurrentItem());
    }

    @Override
    public void onRetryAction() {
        refreshWeather();
    }

    private void refreshWeather() {
        toggleFragments(true, false);
        StringRequest request = new RestClient.RequestBuilder()
                .appendTag(TAG)
                .appendUrl(new UrlBuilder()
                        .withGroupCities()
                        .withUnit(RestClient.Unit.METRIC)
                        .appendCityId(RestClient.CityId.BERLIN)
                        .appendCityId(RestClient.CityId.PARIS)
                        .appendCityId(RestClient.CityId.LOS_ANGELES)
                        .build())
                .appendListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        toggleFragments(false, true);
                        VolleyLog.d("Response: " + response);
                        items = new Gson().fromJson(response, Items.class);
                        if (items.isSuccess()) {
                            pagerAdapter = new PagerAdapter(getFragmentManager(), items);
                            mViewPager.setAdapter(pagerAdapter);
                        } else {
                            toggleFragments(false, false);
                            Toast.makeText(MainActivity.this, R.string.error_resource_not_found_message, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .appendErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toggleFragments(false, false);
                        VolleyLog.e("Response: " + error.toString());
                    }
                })
                .build();
        RestClient.addRequestToQueue(this, request);
    }

    private void toggleFragments(boolean loading, boolean success) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (loading) {

            resetViewPager();
            removeRetryFragment(ft);
            recreateProgressFragment(ft);
            ft.commit();
            return;
        }
        if (success) {
            resetViewPager();
            removeAllFragments(ft);
            ft.commit();
            return;
        }
        resetViewPager();
        removeProgressFragment(ft);
        recreateRetryFragment(ft);
        ft.commit();
    }

    private void resetViewPager() {
        mViewPager.setAdapter(null);
    }

    private void recreateProgressFragment(FragmentTransaction ft) {
        progressFragment = new ProgressFragment();
        ft.replace(R.id.container_id, progressFragment);
    }

    private void recreateRetryFragment(FragmentTransaction ft) {
        retryFragment = new RetryActionFragment();
        ft.replace(R.id.container_id, retryFragment);
    }

    private void removeProgressFragment(FragmentTransaction ft) {
        if (progressFragment != null && progressFragment.isAdded()) {
            ft.remove(progressFragment);
        }
    }

    private void removeRetryFragment(FragmentTransaction ft) {
        if (retryFragment != null && retryFragment.isAdded()) {
            ft.remove(retryFragment);
        }
    }

    private void removeAllFragments(FragmentTransaction ft) {
        removeRetryFragment(ft);
        removeProgressFragment(ft);
    }
}
