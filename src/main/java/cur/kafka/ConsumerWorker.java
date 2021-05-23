package cur.kafka;

/**
 * @author created by WBC
 * @date 2020/12/4
 */

import cur.redis.RedisHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class ConsumerWorker implements Runnable {
    //ConsumerRecord对象变量
    private final ConsumerRecord<String, String> consumerRecord;

    /**
     * ConsumerWorker构造器方法
     * @param record ConsumerRecord
     */
    public ConsumerWorker(ConsumerRecord record) {
        this.consumerRecord = record;
    }

    /**
     * run方法
     */
    @Override
    public void run() {
        RedisHandler.RedisWrite(consumerRecord.key(),consumerRecord.value());
    }
}
