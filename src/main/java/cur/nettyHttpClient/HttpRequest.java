package cur.nettyHttpClient;

/**
 * @author created by WBC
 * @date 2020/12/4
 */

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.*;

import java.net.URI;

public class HttpRequest {
    /**
     * 发送爬虫请求方法
     * @param myURI URI
     * @param myCF ChannelFuture
     * @throws Exception
     */
    public void sendHttpRequest(URI myURI, ChannelFuture myCF) throws Exception{
        String msg = "";
        //初始化请求
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1,//版本号
                HttpMethod.GET,//请求类型
                myURI.toASCIIString(),//URI信息
                Unpooled.wrappedBuffer(msg.getBytes("UTF-8"))//缓冲区
        );
        /*构建请求*/
        //主机
        request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
        //连接状态
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        //头部长度
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        //浏览器代理
        request.headers().set(HttpHeaderNames.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.67 Safari/537.36 Edg/87.0.664.52");
        //文件类型
        request.headers().set(HttpHeaderNames.ACCEPT, "text/html,application/xhtml+xml," + "application/xml;q=0.9,image/webp,*/*;q=0.8");
        //语言
        request.headers().set(HttpHeaderNames.ACCEPT_LANGUAGE,"zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        /*发送请求*/
        myCF.channel().write(request);
    }
}
