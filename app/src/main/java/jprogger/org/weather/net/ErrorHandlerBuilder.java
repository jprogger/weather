package jprogger.org.weather.net;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import java.util.HashMap;
import java.util.Map;

import jprogger.org.weather.R;

public final class ErrorHandlerBuilder {

    private final int RESOURCE_NO_FOUND_ERROR = 404;
    private final int INTERNAL_SERVER_ERROR = 500;
    private final int SERVICE_UNAVAILABLE_ERROR = 503;
    private final int CONNECTION_TIMEOUT_ERROR = 504;
    private final int NO_INTERNET_CONNECTION_ERROR = -1;

    private static final String LOG_TAG = ErrorHandlerBuilder.class.getSimpleName();

    static class DefaultErrorHandler implements ErrorHandler {

        @Override
        public void handleError(Context ctx, VolleyError error) {
            logError(error);
            showToastMessage(ctx, buildErrorString(error));
        }
    }

    public static class Http500ErrorHandler implements  ErrorHandler {

        @Override
        public void handleError(Context ctx, VolleyError error) {
            logError(error);
            showToastMessage(ctx, R.string.error_http_500_message);
        }
    }

    public static class ResourceNotFoundHandler implements ErrorHandler {

        @Override
        public void handleError(Context ctx, VolleyError error) {
            logError(error);
            showToastMessage(ctx, R.string.error_resource_not_found_message);
        }
    }

    /**
     *  Error handler responsible for routing error
     *  depends on it's error code
     */
    class RouterErrorHandler implements ErrorHandler {

        @Override
        public void handleError( Context ctx, VolleyError error) {
            if (error == null) {
                throw new IllegalArgumentException("ERROR can not be null");
            }
            if (ctx == null) {
                throw new IllegalArgumentException("Context can not be null");
            }
            if (error.networkResponse != null) {
                handlers.get(error.networkResponse.statusCode).handleError(ctx, error);
            } else {
                handlers.get(NO_INTERNET_CONNECTION_ERROR).handleError(ctx, error);
            }
        }
    }


    private ErrorHandler routeHandler = new RouterErrorHandler();
    private Map<Integer, ErrorHandler> handlers = new HashMap<Integer, ErrorHandler>();

    public ErrorHandlerBuilder() {
        handlers.put(RESOURCE_NO_FOUND_ERROR, new ResourceNotFoundHandler());
        handlers.put(INTERNAL_SERVER_ERROR, new Http500ErrorHandler());
        handlers.put(SERVICE_UNAVAILABLE_ERROR, new ErrorHandler() {
            @Override
            public void handleError(Context ctx, VolleyError error) {
                showToastMessage(ctx, R.string.error_connection_timeout_message);
            }
        });
        handlers.put(CONNECTION_TIMEOUT_ERROR, new ErrorHandler() {
            @Override
            public void handleError(Context ctx, VolleyError error) {
                showToastMessage(ctx, R.string.error_server_unavailable_message);
            }
        });
        handlers.put(NO_INTERNET_CONNECTION_ERROR, new ErrorHandler() {
            @Override
            public void handleError(Context ctx, VolleyError error) {
                showToastMessage(ctx, R.string.error_network_unavailable_message);
            }
        });
    }

    // internal server error (e.g. HTTP 500)

    public ErrorHandlerBuilder overrideInternalServerErrorDefaultHandler(ErrorHandler handler) {
        checkNotNullOrThrow(handler);
        handlers.put(INTERNAL_SERVER_ERROR, handler);
        return this;
    }

    // server/client timeout (e.g. HTTP 504/408)

    public ErrorHandlerBuilder overrideConnectionTimeoutErrorDefaultHandler(ErrorHandler handler) {
        checkNotNullOrThrow(handler);
        handlers.put(CONNECTION_TIMEOUT_ERROR, handler);
        return this;
    }

    // server unavailable (e.g. HTTP 503)

    public ErrorHandlerBuilder overrideServiceUnavailableErrorDefaultHandler(ErrorHandler handler) {
        checkNotNullOrThrow(handler);
        handlers.put(SERVICE_UNAVAILABLE_ERROR, handler);
        return this;
    }

    // resource not found (e.g. HTTP 404)

    public ErrorHandlerBuilder overrideResourceNotFoundDefaultHandler(ErrorHandler handler) {
        checkNotNullOrThrow(handler);
        handlers.put(RESOURCE_NO_FOUND_ERROR, handler);
        return this;
    }

    public ErrorHandlerBuilder overrideNoInternetConnectionDefaultHandler(ErrorHandler handler) {
        checkNotNullOrThrow(handler);
        handlers.put(NO_INTERNET_CONNECTION_ERROR, handler);
        return this;
    }


    public ErrorHandler build() {
        return routeHandler;
    }

    private void checkNotNullOrThrow( ErrorHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler argument can not be NULL");
        }
    }

    private static void logError(VolleyError error) {
        VolleyLog.e(LOG_TAG + ": "+ buildErrorString(error));
    }


    private static String buildErrorString(VolleyError error) {
        return error.toString();
    }

    private static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private static void showToastMessage(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }
}
