package integrationtests;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.Assert.fail;

class ApplicationServer {

    private final String workingDir = System.getProperty("user.dir");
    private final HttpClient httpClient;

    private Process serverProcess;

    ApplicationServer(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    void start(Map<String, String> env) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder()
            .command("java", "-jar", workingDir + "/../application/build/libs/application.jar")
            .inheritIO();

        env.forEach((key, value) -> processBuilder.environment().put(key, value));

        serverProcess = processBuilder.start();

        waitUntilServerIsUp();
    }

    void stop() {
        serverProcess.destroyForcibly();
    }


    private void waitUntilServerIsUp() throws InterruptedException {
        String port = "8080";
        int timeout = Integer.parseInt(getEnv("SERVER_START_TIMEOUT", "20"));
        Instant start = Instant.now();
        boolean isUp = false;

        while (!isUp) {
            try {
                httpClient.get("http://localhost:" + port);
                isUp = true;
            } catch (Throwable e) {

                long timeSpent = ChronoUnit.SECONDS.between(start, Instant.now());
                if (timeSpent > timeout) {
                    fail("Timed out waiting for server on port " + port);
                }

                System.out.println(String.format("Waiting on port %s, %s", port, e.getClass().toString()));
                Thread.sleep(200);
            }
        }
    }

    private String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }
}
