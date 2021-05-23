package cur.cs;

/**
 * @author created by WBC
 * @date 2020/12/7
 */

import cur.nettyHttpClient.NettyClient;

public class Client {
    public static void main(String[] args) throws Exception {
        for(int i=0; i<args.length; i++) {
            new NettyClient(args[i]);
        }
    }
}