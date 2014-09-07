package jprogger.org.weather.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RestClient {

    private static RequestQueue requestQueue;

    public static void addRequestToQueue(Context context, Request request) {
        getRequestQueue(context).add(request);
    }

    public static void cancelAllRequests(Context context, String tag) {
        getRequestQueue(context).cancelAll(tag);
    }

    private static RequestQueue getRequestQueue(Context context) {
        if (requestQueue != null) {
            return requestQueue;
        }
        requestQueue = Volley.newRequestQueue(context);
        return requestQueue;
    }

    public interface CityId {
        long BERLIN = 2950159;
        long PARIS = 2988507;
        long LOS_ANGELES = 5368361;
    }

    public interface Unit {
        String METRIC = "metric";
        String IMPERIAL = "imperial";
    }

    public static class RequestBuilder {

        private String url;
        private String tag;
        private int restMethod = Request.Method.GET;

        private Response.Listener<String> listener;
        private Response.ErrorListener errorListener;

        public RequestBuilder appendTag(String tag) {
            this.tag = tag;
            return this;
        }

        public RequestBuilder appendUrl(String url) {
            this.url = url;
            return this;
        }

        public RequestBuilder appendListener(Response.Listener<String> listener) {
            this.listener = listener;
            return this;
        }

        public RequestBuilder appendErrorListener(Response.ErrorListener errorListener) {
            this.errorListener = errorListener;
            return this;
        }

        public StringRequest build() {
            StringRequest request = new StringRequest(restMethod, url, listener, errorListener) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put(UrlBuilder.API_KEY_PARAM, UrlBuilder.API_KEY_VALUE);
                    return headers;
                }
            };
            request.addMarker(tag);
            return request;
        }
    }
}
