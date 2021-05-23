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

import java.util.HashMap;
import java.util.Map;

public class LetterCountBolt extends BaseRichBolt {

    private OutputCollector myOutputCollector;
    Map<String,Integer> map = new HashMap<>();
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.myOutputCollector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String splitLetter = tuple.getStringByField("splitLetters");
        if (map.containsKey(splitLetter))
            map.put(splitLetter, map.get(splitLetter) + 1);
        else
            map.put(splitLetter, 1);
        this.myOutputCollector.emit(new Values(splitLetter, String.valueOf(map.get(splitLetter))));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("countLetters", "letterCounts"));
    }
}
