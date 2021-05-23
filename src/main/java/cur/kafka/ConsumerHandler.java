package cur.kafka;

/**
 * @author created by WBC
 * @date 2020/12/4
 */

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConsumerHandler {
    //KafkaConsumer对象变量
    private final KafkaConsumer<String, String> consumer;
    //ExecutorService对象变量
    private ExecutorService executors;

    /**
     * 消费者处理逻辑方法
     * @param topics List<String>
     */
    public ConsumerHandler(List<String> topics) {
        /*Consumer的配置信息*/
        Properties props = new Properties();//创建Properties实例
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        /*创建KafkaConsumer实例*/
        consumer = new KafkaConsumer(props);
        /*提交topic*/
        consumer.subscribe(topics);
        /*调用生产者监听方法*/
        ConsumerListen(100);
    }

    /**
     * 生产者监听方法
     * @param workerNum int
     */
    public void ConsumerListen(int workerNum) {
        /*创建线程池*/
        executors = new ThreadPoolExecutor (
                workerNum,//核心线程池大小
                workerNum,//最大线程池大小
                0L,//线程最大空闲时间
                TimeUnit.MILLISECONDS,//毫秒时间单位
                new ArrayBlockingQueue(1000),//线程等待队列大小
                new ThreadPoolExecutor.CallerRunsPolicy()//线程创建工厂
        );
        //子线程跳转的lambda表达式
        Thread myThread = new Thread(//启动子线程监听kafka消息
                () -> {
                    while (true) {//死循环接收消息
                        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(200));//创建消息记录实例
                        for (final ConsumerRecord record : records) {
                            /*输出消息结果*/
                            System.out.println("【time】 " + new Date().toString());
                            System.out.println("【Kafka】监听到kafka的TOPIC【" + record.topic() + "】的消息");
                            System.out.println("【Kafka】消息key：  " + record.key());
                            System.out.println("【Kafka】消息value：" + record.value());
                            executors.submit(new ConsumerWorker(record));//转到ConsumerWorker进行处理
                        }
                    }
                }
        );
        myThread.start();
    }
}