package geb.issues;

import com.google.inject.AbstractModule;
import geb.issues.github.Credentials;

public class CredentialsModule extends AbstractModule {

    private final Credentials credentials;

    public CredentialsModule(String user, String password) {
        credentials = new Credentials(user, password);
    }

    protected void configure() {
        bind(Credentials.class).toInstance(credentials);
    }
}
