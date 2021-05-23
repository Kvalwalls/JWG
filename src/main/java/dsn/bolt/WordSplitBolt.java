package dsn.bolt;

/**
 * @author created by WBC
 * @date 2020/12/15
 */

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * @author created by WBC
 * @date 2020/12/8
 */

public class WordSplitBolt extends BaseRichBolt {

    private OutputCollector myOutputCollector;

    private static String regex_replace = "[~·!！@#￥$%^……&*（()）\\-=+【\\\\[\\\\]】｛{}｝\\\\|、\\\\\\\\；;：:“”\\\"，,《<。.》>、/？?]";
    private static String regex_Split = "[\\s]+";

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.myOutputCollector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            String value = tuple.getStringByField("value");
            myOutputCollector.ack(tuple);
            value = value.replaceAll(regex_replace," ");
            String[] splitWords = value.split(regex_Split);
            for(String splitWord : splitWords) {
                if(!splitWord.trim().isEmpty()) {
                    this.myOutputCollector.emit(new Values(splitWord));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            myOutputCollector.fail(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("splitWords"));
    }
}
