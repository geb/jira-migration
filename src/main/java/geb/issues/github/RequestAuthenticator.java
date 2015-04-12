package geb.issues.github;

import ratpack.http.MutableHeaders;
import ratpack.http.client.RequestSpec;

import javax.inject.Inject;
import java.util.Base64;

public class RequestAuthenticator {

    private final String user;
    private final String encodedCredentials;

    @Inject
    public RequestAuthenticator(Credentials credentials) {
        this.encodedCredentials = encode(credentials);
        this.user = credentials.getUser();
    }

    private String encode(Credentials credentials) {
        String credentialsString = credentials.getUser() + ":" + credentials.getPassword();
        return Base64.getMimeEncoder().encodeToString(credentialsString.getBytes());
    }

    void authenticate(RequestSpec requestSpec) {
        MutableHeaders headers = requestSpec.getHeaders();
        headers.add("Authorization", "Basic " + encodedCredentials);
        headers.add("User-Agent", user);
    }
}
