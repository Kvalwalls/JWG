package cur.nettyHttpClient;

/**
 * @author created by WBC
 * @date 2020/12/4
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.ssl.SslContext;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    //SslContext信息
    private final SslContext mySslContext;
    //URI信息
    private final String myURI;

    /**
     * NettyClientInitializer构造器方法
     * @param mySslContext
     * @param myURI
     */
    public NettyClientInitializer(SslContext mySslContext, String myURI) {
        this.mySslContext = mySslContext;
        this.myURI = myURI;
    }

    /**
     * initChannel方法
     * @param mySC
     */
    @Override
    public void initChannel(SocketChannel mySC) {
        ChannelPipeline myCPipeline = mySC.pipeline();
        if (mySslContext != null) {//mySslContext不为空即为HTTPS协议
            myCPipeline.addLast(mySslContext.newHandler(mySC.alloc()));
        }
        myCPipeline.addLast(new HttpClientCodec());//HttpClientCodec对象
        myCPipeline.addLast(new HttpContentDecompressor());//HttpContentDecompressor对象
        myCPipeline.addLast(new NettyClientHandler(this.myURI));//NettyClientHandler对象
    }
}
