package integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

class HttpClient {

    private static final MediaType JSON = MediaType.parse("application/json");
    private final OkHttpClient okHttp = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    Response get(String url) {
        Request request = new Request.Builder().url(url).build();
        return fetch(request);
    }

    Response post(String url, Map<String, Object> jsonBody) {
        try {
            Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, objectMapper.writeValueAsString(jsonBody)))
                .build();

            return fetch(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Response put(String url, Map<String, Object> jsonBody) {
        try {
            Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(JSON, objectMapper.writeValueAsString(jsonBody)))
                .build();

            return fetch(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response delete(String url) {
        Request request = new Request.Builder().delete().url(url).build();
        return fetch(request);
    }


    private Response fetch(Request request) {
        try {
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
