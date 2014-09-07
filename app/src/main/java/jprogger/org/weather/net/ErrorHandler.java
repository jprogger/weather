package jprogger.org.weather.net;

import android.content.Context;

import com.android.volley.VolleyError;

public interface ErrorHandler {

    void handleError(Context ctx, VolleyError error);
}
