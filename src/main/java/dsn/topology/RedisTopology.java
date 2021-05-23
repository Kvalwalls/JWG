package dsn.topology;

import dsn.bolt.*;
import dsn.spout.RedisReadSpout;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff;
import org.apache.storm.kafka.spout.KafkaSpoutRetryService;
import org.apache.storm.redis.bolt.RedisStoreBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.topology.TopologyBuilder;

public class RedisTopology {

    private static final String REDIS_HOST = "localhost";

    private static final int REDIS_PORT = 6379;

    public static void main(String[] args) throws Exception {
        final TopologyBuilder builder = new TopologyBuilder();

        JedisPoolConfig poolConfig = new JedisPoolConfig
                .Builder()
                .setHost(REDIS_HOST)
                .setPort(REDIS_PORT)
                .build();

        builder.setSpout("myRedisSpout", new RedisReadSpout("http://english.sina.com/world/as/2020-12-08/detail-iiznezxs5838915.shtml"), 1);

        builder.setBolt("myWordSplitBolt", new WordSplitBolt()).shuffleGrouping("myRedisSpout");
        builder.setBolt("myWordCountBolt", new WordCountBolt()).shuffleGrouping("myWordSplitBolt");
        builder.setBolt(
                "myWordStoreBolt",
                new RedisStoreBolt(
                        poolConfig,
                        new StoreMapper("myWordStore", "countWords", "wordCounts")
                )
        ).shuffleGrouping("myWordCountBolt");

        builder.setBolt("myLetterSplitBolt", new LetterSplitBolt()).shuffleGrouping("myRedisSpout");
        builder.setBolt("myLetterCountBolt", new LetterCountBolt()).shuffleGrouping("myLetterSplitBolt");
        builder.setBolt(
                "myLetterStoreBolt",
                new RedisStoreBolt(
                        poolConfig,
                        new StoreMapper("myLetterStore", "countLetters", "letterCounts")
                )
        ).shuffleGrouping("myLetterCountBolt");

        builder.setBolt("mySentenceSplitBolt", new SentenceSplitBolt()).shuffleGrouping("myRedisSpout");
        builder.setBolt("mySentenceCountBolt", new SentenceCountBolt()).shuffleGrouping("mySentenceSplitBolt");
        builder.setBolt(
                "mySentenceStoreBolt",
                new RedisStoreBolt(
                        poolConfig,
                        new StoreMapper("mySentenceStore", "countSentences", "sentenceCounts")
                )
        ).shuffleGrouping("mySentenceCountBolt");

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("myKafkaTopology", new Config(), builder.createTopology());
        Thread.sleep(20000);
        cluster.shutdown();
    }
    private static KafkaSpoutConfig<String, String> getKafkaSpoutConfig(String bootstrapServers, String topic) {
        return KafkaSpoutConfig.builder(bootstrapServers, topic)
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, "kafkaSpoutTestGroup")
                .setRetry(getRetryService())
                .setOffsetCommitPeriodMs(10_000)
                .build();
    }

    private static KafkaSpoutRetryService getRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(KafkaSpoutRetryExponentialBackoff.TimeInterval.microSeconds(500),
                KafkaSpoutRetryExponentialBackoff.TimeInterval.milliSeconds(2), Integer.MAX_VALUE, KafkaSpoutRetryExponentialBackoff.TimeInterval.seconds(10));
    }
}
