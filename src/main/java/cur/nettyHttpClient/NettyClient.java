package cur.nettyHttpClient;

/**
 * @author created by WBC
 * @date 2020/12/4
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.net.URI;

public class NettyClient {
    /**
     * netty客户端实现方法
     * @param strURI String
     * @throws Exception
     */
   public NettyClient(String strURI) throws Exception {
       /*部分变量的设置与初始化*/
       URI myURI = new URI(strURI);//URI名
       int myPort = myURI.getPort();//端口号
       String myHost = myURI.getHost();//主机名
       String myScheme = myURI.getScheme() == null ? "http" : myURI.getScheme();//协议名
       boolean boolSsl = false;//判断是否为HTTPS协议
       /*端口号的设置*/
       if (myPort == -1) {//端口号为-1
           if ("http".equalsIgnoreCase(myScheme)) {//HTTP协议
               myPort = 80;//HTTP协议使用80端口
               boolSsl = false;
           } else if ("https".equalsIgnoreCase(myScheme)) {//HTTPS协议
               myPort = 443;//HTTPS协议使用443端口
               boolSsl = true;
           } else {//不支持的协议
               throw new Exception("ONLY HTTP(S) IS AVAILABLE!");
           }
       } else {//端口号不为-1
           throw new Exception("PORT ERROR!");
       }
       /*创建SslContext实例*/
       SslContext mySslContext = null;
       if (boolSsl) {//HTTPS协议下创建SslContext实例
           mySslContext = SslContextBuilder
                   .forClient()//设置为客户端类型
                   .trustManager(InsecureTrustManagerFactory.INSTANCE)//设置证书信任
                   .build();//创建实例
       } else {//HTTP协议下不创建SslContext实例
           mySslContext = null;
       }
       /*netty客户端*/
       EventLoopGroup workerGroup = new NioEventLoopGroup();//创建处理客户端连接、IO读写的Reactor线程组
       try {
           /*创建netty客户端*/
           Bootstrap myBootstrap = new Bootstrap();//用户线程创建Bootstrap实例
           myBootstrap.group(workerGroup);//指定线程组
           myBootstrap.channel(NioSocketChannel.class);//指定Channel类型
           myBootstrap.option(ChannelOption.SO_KEEPALIVE, true);//设置TCP连接属性
           myBootstrap.handler(new NettyClientInitializer(mySslContext,strURI));//初始化业务处理逻辑
           ChannelFuture myCF = myBootstrap.connect(myHost, myPort).sync();//建立同步TCP连接
           /*发送爬虫请求*/
           HttpRequest myHttpRequest = new HttpRequest();//实例化爬虫请求
           myHttpRequest.sendHttpRequest(myURI, myCF);//发送爬虫请求
           /*结束netty客户端*/
           myCF.channel().flush();//刷新内存队列
           myCF.channel().closeFuture().sync();//关闭异步TCP连接
       } finally {
           workerGroup.shutdownGracefully();//关闭线程组
       }
   }
}