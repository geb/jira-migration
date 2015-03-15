package ratpack.http.client;

import io.netty.buffer.ByteBufAllocator;
import ratpack.exec.ExecControl;
import ratpack.exec.ExecController;
import ratpack.exec.Execution;
import ratpack.exec.Promise;
import ratpack.func.Action;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;
import ratpack.http.client.internal.DefaultHttpClient;
import ratpack.http.client.internal.ProxyingContentAggregatingRequestAction;

import java.net.SocketAddress;
import java.net.URI;

import static ratpack.util.Exceptions.uncheck;

public class ProxySupportingHttpClient extends DefaultHttpClient {

    private final ExecController execController;
    private final ByteBufAllocator byteBufAllocator;
    private final int maxContentLengthBytes;
    private final SocketAddress proxyAddress;

    public ProxySupportingHttpClient(ExecController execController, ByteBufAllocator byteBufAllocator, int maxContentLengthBytes, SocketAddress proxyAddress) {
        super(execController, byteBufAllocator, maxContentLengthBytes);
        this.execController = execController;
        this.byteBufAllocator = byteBufAllocator;
        this.maxContentLengthBytes = maxContentLengthBytes;
        this.proxyAddress = proxyAddress;
    }

    @Override
    public Promise<ReceivedResponse> request(URI uri, Action<? super RequestSpec> requestConfigurer) {
        final ExecControl execControl = execController.getControl();
        final Execution execution = execControl.getExecution();

        try {
            ProxyingContentAggregatingRequestAction requestAction = new ProxyingContentAggregatingRequestAction(requestConfigurer, uri, execution,
                    byteBufAllocator, maxContentLengthBytes, proxyAddress);
            return execController.getControl().promise(requestAction);
        } catch (Exception e) {
            throw uncheck(e);
        }
    }
}
