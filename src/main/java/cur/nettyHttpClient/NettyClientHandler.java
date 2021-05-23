package cur.nettyHttpClient;

/**
 * @author created by WBC
 * @date 2020/12/4
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.LastHttpContent;
import cur.kafka.ProducerHandler;

import java.util.Date;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //URI信息
    private final String myURI;
    //html文本信息
    private String textStr = "";

    /**
     * NettyClientHandler构造器方法
     * @return void
     * @param myURI
     */
    public NettyClientHandler(String myURI) {
        this.myURI = myURI;
    }

    /**
     * channelRead方法
     * @param myCHC ChannelHandlerContext
     * @param message Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext myCHC, Object message) throws Exception {
        //HttpContext类型信息
        if (message instanceof HttpContent) {
            HttpContent content = (HttpContent) message;//类型转换
            ByteBuf myByteBuf = content.content();//Byte数组的缓冲区存储信息内容
            textStr += myByteBuf.toString(io.netty.util.CharsetUtil.UTF_8);//html信息的拼接
            myByteBuf.release();//释放Byte数组的缓冲区
        }
        if (message instanceof LastHttpContent) {
            textStr = HtmlToText.regexHtmlToText(textStr);//通过正则表达式转换为html文本信息
            System.out.println("【time】 " + new Date().toString());
            System.out.println("【netty】爬虫结果：" + textStr);
            //发送Kafka消息队列
            new ProducerHandler("MYTOPIC",myURI,textStr);
            myCHC.close();//关闭ChannelHandlerContext
        }
    }

    /**
     * exceptionCaught方法
     * @param myCHC ChannelHandlerContext
     * @param cause Throwable
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext myCHC, Throwable cause) {
        cause.printStackTrace();
        myCHC.close();
    }
}