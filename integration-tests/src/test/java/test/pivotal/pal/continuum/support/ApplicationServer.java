package test.pivotal.pal.continuum.support;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.Assert.fail;

public class ApplicationServer {

    private final String workingDir = System.getProperty("user.dir");
    private final HttpClient httpClient;

    private Process serverProcess;

    public ApplicationServer(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    public void start(Map<String, String> env) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder()
            .command("java", "-jar", workingDir + "/../application/build/libs/application.jar")
            .inheritIO();

        env.forEach((key, value) -> processBuilder.environment().put(key, value));

        serverProcess = processBuilder.start();

        waitUntilServerIsUp();
    }

    public void stop() {
        serverProcess.destroyForcibly();
    }


    private void waitUntilServerIsUp() throws InterruptedException {
        String port = "8080";
        int timeout = 20;
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
}
