package dsn.spout;

/**
 * @author created by WBC
 * @date 2020/12/15
 */

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisReadSpout extends BaseRichSpout {

    private SpoutOutputCollector spoutOutputCollector;

    private static final String HOST = "localhost";

    private final String key;

    private static int count = 0;

    private String myRedisValue;

    public RedisReadSpout(String key) {
        this.key = key;
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        Jedis myJedis = new Jedis(HOST);//创建Jedis实例
        myJedis.connect();//Redis数据库的连接
        myRedisValue = myJedis.get(key);
        this.spoutOutputCollector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        if (count == 0) {
            spoutOutputCollector.emit(new Values(myRedisValue));
            count++;
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("value"));
    }
}