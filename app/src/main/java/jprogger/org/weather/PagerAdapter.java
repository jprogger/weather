package jprogger.org.weather;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Parcelable;
import android.support.v13.app.FragmentStatePagerAdapter;

import jprogger.org.weather.model.Items;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private Items items;

    public PagerAdapter(FragmentManager fm, Items items) {
        super(fm);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragment.newInstance(items.list.get(position), getColor(position));
    }

    @Override
    public int getCount() {
        return items.count;
    }

    public int getColor(int position){
        switch (position) {
            case 0:
                return R.color.page_bg_1;
            case 1:
                return R.color.page_bg_2;
            case 2:
                return R.color.page_bg_3;
            default:
                return R.color.page_bg_1;
        }
    }
}
