package geb.issues;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.netty.buffer.ByteBufAllocator;
import ratpack.exec.ExecController;
import ratpack.http.client.HttpClient;
import ratpack.server.ServerConfig;
import ratpack.test.exec.ExecHarness;

public class IssuesModule extends AbstractModule {

    protected void configure() {
    }

    @Provides
    @Singleton
    ExecHarness execHarnessProvider() {
        return ExecHarness.harness();
    }

    @Provides
    ExecController execControllerProvider(ExecHarness execHarness) {
        return execHarness.getController();
    }

    @Provides
    HttpClient httpClientProvider(ExecController execController) {
        return HttpClient.httpClient(execController, ByteBufAllocator.DEFAULT, ServerConfig.DEFAULT_MAX_CONTENT_LENGTH);
    }

    @Singleton
    @Provides
    ObjectMapper objectMapperProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JSR310Module());
        return objectMapper;
    }
}
