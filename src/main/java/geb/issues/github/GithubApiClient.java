package geb.issues.github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.exec.Result;
import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.http.HttpUrlBuilder;
import ratpack.http.client.HttpClient;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;

import javax.inject.Inject;
import java.io.OutputStream;
import java.net.URI;

import static ratpack.util.Exceptions.uncheck;

public class GithubApiClient {
    private static final String REMAINING_LIMIT_HEADER = "X-RateLimit-Remaining";
    private static final String IMPORT_API_ACCEPT_HEADER = "application/vnd.github.golden-comet-preview+json";
    private static final Logger LOGGER = LoggerFactory.getLogger(GithubApiClient.class);

    private final HttpClient httpClient;
    private final RequestAuthenticator requestAuthenticator;
    private final ObjectMapper objectMapper;

    @Inject
    public GithubApiClient(HttpClient httpClient, RequestAuthenticator requestAuthenticator, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.requestAuthenticator = requestAuthenticator;
        this.objectMapper = objectMapper;
    }

    public Promise<ReceivedResponse> method(String method, Action<HttpUrlBuilder> urlConfig) {
        return method(method, urlConfig, arg -> {});
    }

    public <T> Promise<ReceivedResponse> method(String method, Action<HttpUrlBuilder> urlConfig, T content) {
        Action<RequestSpec> writeContentAction = requestSpec -> requestSpec.getBody().type(MediaType.JSON_UTF_8.toString()).stream(valueWriter(content));
        return method(method, urlConfig, writeContentAction);
    }

    public Promise<ReceivedResponse> method(String method, Action<HttpUrlBuilder> urlConfig, Action<RequestSpec> requestConfig) {
        Action<RequestSpec> config = Action.join(requestAuthenticator::authenticate, requestConfig, requestSpec -> {
            requestSpec.method(method).headers(headers -> headers.add("Accept", IMPORT_API_ACCEPT_HEADER));
        });
        return httpClient.request(uri(urlConfig), config)
                .wiretap(GithubApiClient::printRemainingLimit);
    }

    public <T> Promise<T> get(Action<HttpUrlBuilder> urlConfig, TypeReference<T> representation) {
        return method("GET", urlConfig).map(responseTransformer(representation));
    }

    public Promise<ReceivedResponse> delete(Action<HttpUrlBuilder> urlConfig) {
        return method("DELETE", urlConfig);
    }

    public <T> Promise<ReceivedResponse> post(Action<HttpUrlBuilder> urlConfig, T content) {
        return method("POST", urlConfig, content);
    }

    public <C, R> Promise<R> post(Action<HttpUrlBuilder> urlConfig, C content, TypeReference<R> response) {
        return method("POST", urlConfig, content).map(responseTransformer(response));
    }

    private URI uri(Action<HttpUrlBuilder> urlConfig) {
        HttpUrlBuilder urlBuilder = baseUrlBuilder();
        try {
            urlConfig.execute(urlBuilder);
        } catch (Exception e) {
            throw uncheck(e);
        }
        return urlBuilder.build();
    }

    private HttpUrlBuilder baseUrlBuilder() {
        return HttpUrlBuilder.https().host("api.github.com").segment("repos").segment("geb").segment("issues");
    }

    private <T> Function<ReceivedResponse, T> responseTransformer(TypeReference<T> representation) {
        return response -> objectMapper.readValue(response.getBody().getInputStream(), representation);
    }

    private <T> Action<? super OutputStream> valueWriter(T payload) {
        return outputStream -> objectMapper.writeValue(outputStream, payload);
    }

    private static void printRemainingLimit(Result<ReceivedResponse> response) {
        try {
            String remainingLimit = response.getValueOrThrow().getHeaders().get(REMAINING_LIMIT_HEADER);
            LOGGER.info("Remaining GitHub API request limit: {}", remainingLimit);
        } catch (Exception e) {
            throw uncheck(e);
        }
    }
}