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

public class SentenceSplitBolt extends BaseRichBolt {

    private OutputCollector myOutputCollector;

    private static String regex_Split = "\\.[ ]|\\?[ ]|![ ]|:[ ]|;[ ]|\\|[ ]|\\{[ ]|\\}[ ]";

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.myOutputCollector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            String value = tuple.getStringByField("value");
            myOutputCollector.ack(tuple);
            String[] splitSentences = value.split(regex_Split);
            for(String splitSentence : splitSentences) {
                if(!splitSentence.trim().isEmpty()) {
                    this.myOutputCollector.emit(new Values(splitSentence));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            myOutputCollector.fail(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("splitSentences"));
    }
}