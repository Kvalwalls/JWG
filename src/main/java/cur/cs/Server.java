package cur.cs;

/**
 * @author created by WBC
 * @date 2020/12/7
 */

import cur.kafka.ConsumerHandler;
import java.util.Arrays;

public class Server {
    public static void main(String[] args) {
        new ConsumerHandler(Arrays.asList("MYTOPIC"));
    }
}
