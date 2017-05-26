package test.pivotal.pal.continuum.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class HttpClient {

    private static final MediaType JSON = MediaType.parse("application/json");

    private final OkHttpClient okHttp = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public Response get(String url) {
        return fetch(new Request.Builder().url(url));
    }

    public Response post(String url, Map<String, Object> jsonBody) {
        try {
            Request.Builder reqBuilder = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, objectMapper.writeValueAsString(jsonBody)));

            return fetch(reqBuilder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Response fetch(Request.Builder requestBuilder) {
        try {
            Request request = requestBuilder.build();

            okhttp3.Response response = okHttp.newCall(request).execute();
            ResponseBody body = response.body();

            if (body == null) {
                return new Response(response.code(), "");
            }

            return new Response(response.code(), body.string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Response {
        public final int status;
        public final String body;

        public Response(int status, String body) {
            this.status = status;
            this.body = body;
        }
    }
}
