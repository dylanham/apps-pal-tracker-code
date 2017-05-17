package integrationtests;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

class HttpClient {

    private final OkHttpClient okHttp = new OkHttpClient();

    String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okHttp.newCall(request).execute();
        ResponseBody body = response.body();

        if (body == null) {
            return "";
        }

        return body.string();
    }
}
