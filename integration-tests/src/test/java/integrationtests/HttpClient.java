package integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import static java.lang.String.format;

class HttpClient {

    private static final MediaType JSON = MediaType.parse("application/json");
    private final OkHttpClient okHttp = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    Response get(String url) {
        return fetch(new Request.Builder().url(url));
    }

    Response post(String url, Map<String, Object> jsonBody) {
        try {
            Request.Builder reqBuilder = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, objectMapper.writeValueAsString(jsonBody)));

            return fetch(reqBuilder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Response put(String url, Map<String, Object> jsonBody) {
        try {
            Request.Builder reqBuilder = new Request.Builder()
                .url(url)
                .put(RequestBody.create(JSON, objectMapper.writeValueAsString(jsonBody)));

            return fetch(reqBuilder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response delete(String url) {
        return fetch(new Request.Builder().delete().url(url));
    }


    private Response fetch(Request.Builder requestBuilder) {
        try {
            Request request = requestBuilder
                .header("Authorization", format("Basic %s", credentials))
                .build();

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

    private static String credentials = Base64.getEncoder().encodeToString("user:password".getBytes());

    public static class Response {
        public final int status;
        public final String body;

        public Response(int status, String body) {
            this.status = status;
            this.body = body;
        }
    }
}
