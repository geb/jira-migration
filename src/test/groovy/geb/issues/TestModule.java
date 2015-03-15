package geb.issues;

import co.freeside.betamax.Recorder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.netty.buffer.ByteBufAllocator;
import ratpack.exec.ExecController;
import ratpack.http.client.HttpClient;
import ratpack.http.client.ProxySupportingHttpClient;
import ratpack.server.internal.DefaultServerConfig;
import ratpack.test.exec.ExecHarness;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class TestModule extends AbstractModule {
    @Override
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
        SocketAddress proxyAddress = new InetSocketAddress("localhost", Recorder.DEFAULT_PROXY_PORT);
        return new ProxySupportingHttpClient(execController, ByteBufAllocator.DEFAULT, DefaultServerConfig.DEFAULT_MAX_CONTENT_LENGTH, proxyAddress);
    }
}
