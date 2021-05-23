package cur.kafka;

/**
 * @author created by WBC
 * @date 2020/12/4
 */

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Date;

public class ProducerHandler {
    //KafkaProducer对象变量
    private KafkaProducer<String, String> producer;

    /**
     * 生产者处理逻辑方法
     * @param topic String
     * @param key String
     * @param value String
     */
    public ProducerHandler(String topic, String key, String value) {
        /*Producer的配置信息*/
        Properties props = new Properties();//创建Properties实例
        props.put("bootstrap.servers", "localhost:9092");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");
        props.put("session.timeout.ms", "30000");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //创建KafkaProducer实例
        KafkaProducer producer = new KafkaProducer<String, String>(props);
        //生成消息
        ProducerRecord record = new ProducerRecord(topic,key,value);
        //发送消息
        producer.send(record);
        /*输出消息结果*/
        System.out.println("【time】 " + new Date().toString());
        System.out.println("【kafka】向Kafka的TOPIC【" + topic + "】中发送消息");
        System.out.println("【kafka】消息key  ：" + key);
        System.out.println("【kafka】消息value：" + value);
        System.out.println("【kafka】推送成功");
        //关闭KafkaProducer
        producer.close();
    }
}