package ratpack.http.client.internal;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.HttpProxyHandler;
import ratpack.exec.Execution;
import ratpack.exec.Fulfiller;
import ratpack.func.Action;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;

import java.net.SocketAddress;
import java.net.URI;

public class ProxyingContentAggregatingRequestAction extends ContentAggregatingRequestAction {
    private final SocketAddress proxyAddress;

    public ProxyingContentAggregatingRequestAction(Action<? super RequestSpec> requestConfigurer, URI uri, Execution execution,
                                                   ByteBufAllocator byteBufAllocator, int maxContentLengthBytes, SocketAddress proxyAddress) {
        super(requestConfigurer, uri, execution, byteBufAllocator, maxContentLengthBytes);
        this.proxyAddress = proxyAddress;
    }

    @Override
    protected void addResponseHandlers(ChannelPipeline p, Fulfiller<? super ReceivedResponse> fulfiller) {
        p.addFirst("proxy", new HttpProxyHandler(proxyAddress));
        super.addResponseHandlers(p, fulfiller);
    }
}
