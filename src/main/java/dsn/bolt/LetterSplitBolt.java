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

public class LetterSplitBolt extends BaseRichBolt {

    private OutputCollector myOutputCollector;


    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.myOutputCollector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            String value = tuple.getStringByField("value");
            myOutputCollector.ack(tuple);
            for(int i=0; i<value.length(); i++) {
                if (Character.isLetter(value.charAt(i))) {
                    String letter = String.valueOf(value.charAt(i));
                    this.myOutputCollector.emit(new Values(letter));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            myOutputCollector.fail(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("splitLetters"));
    }
}
