package dsn.bolt;

/**
 * @author created by WBC
 * @date 2020/12/15
 */

import org.apache.storm.redis.common.mapper.RedisDataTypeDescription;
import org.apache.storm.redis.common.mapper.RedisStoreMapper;
import org.apache.storm.tuple.ITuple;

public class StoreMapper implements RedisStoreMapper {

    private RedisDataTypeDescription description;
    private final String hashKey;
    private final String keyFromTuple;
    private final String valueFromTuple;

    public StoreMapper(String hashKey,String keyFromTuple,String valueFromTuple) {
        this.hashKey = hashKey;
        this.keyFromTuple = keyFromTuple;
        this.valueFromTuple = valueFromTuple;
        description = new RedisDataTypeDescription(RedisDataTypeDescription.RedisDataType.HASH, hashKey);
    }

    @Override
    public RedisDataTypeDescription getDataTypeDescription() {
        return description;
    }

    @Override
    public String getKeyFromTuple(ITuple tuple) {
        return tuple.getStringByField(keyFromTuple);
    }

    @Override
    public String getValueFromTuple(ITuple tuple) {
        return tuple.getStringByField(valueFromTuple);
    }
}
