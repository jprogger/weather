package jprogger.org.weather;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProgressFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.indeterminate_progress_with_bottom_label, null);
        rootView.setBackgroundColor(getResources().getColor(R.color.page_bg_1));
        return rootView;
    }
}
