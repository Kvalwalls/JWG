package cur.redis;

import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author created by WBC
 * @date 2020/12/4
 */

public class RedisHandler {
    //Jedis实例
    private static final Jedis myJedis = new Jedis("localhost");
    //读写锁
    private static ReadWriteLock myRWL = new ReentrantReadWriteLock();
    //写锁
    private static Lock myWL = myRWL.writeLock();
    //读锁
    private static Lock myRL = myRWL.readLock();
    /**
     * Redis数据库写入方法
     */
    public static void RedisWrite(String key, String value) {
        try {
            myJedis.connect();//Redis数据库的连接
            myWL.lock();//写上锁
            myJedis.set(key, value);//Redis数据库的写入
            myRL.lock();//读上锁
            /*输出结果*/
            System.out.println("【time】 " + new Date().toString());
            System.out.println("【redis】存储key：  " + key);
            System.out.println("【redis】存储value: " + myJedis.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myRL.unlock();//读开锁
            myWL.unlock();//写开锁
            myJedis.close();//Redis数据库的关闭
        }
    }
}
