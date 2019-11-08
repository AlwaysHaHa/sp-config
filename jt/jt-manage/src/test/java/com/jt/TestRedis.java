package com.jt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;

public class TestRedis {
	
	private Jedis jedis;
	@Before
	public void init() {
		jedis = new Jedis("192.168.88.129",6379);
	}
	
	/**
	 * 测试String类型
	 * host:Redis的ip地址
	 * port:Redis端口号
	 * 
	 * 
	 * 错误查询
	 * 1.ip端口检查
	 * 2.linux防火墙是否关闭
	 * 3.Redis的配置文件3处
	 * 4.检查Redis启动方式 redis-server redis.conf
	 * @throws InterruptedException 
	 */
	@Test
	public void testString() throws Exception {
		
		jedis.set("1906", "redis入门案例");
		String value = jedis.get("1906");
		System.out.println(value);
		
		//测试key相同时,value是否覆盖
		jedis.set("1906", "哈哈哈哈哈");
		String value1=jedis.get("1906");
		System.out.println(value1);
		
		//如果值已经存在,则不允许覆盖
		jedis.setnx("1906", "嘿嘿嘿");
		System.out.println(jedis.get("1906"));
		
		//4.为数据添加超时时间
		jedis.set("time", "超时测试");
		jedis.expire("time", 60);
		
		//保证数据操作有效性(原子性)
		jedis.setex("time",100,"超时测试");
		Thread.sleep(3000);
		Long life = jedis.ttl("time");
		System.out.println("当前数据能存活"+life+"秒");
		
		//5..要求key存在不允许覆盖,并且设定超时时间
		//nx:不允许覆盖 xx:可以覆盖
		//ex: 秒     px:毫秒
		jedis.set("ni", "你好", "NX", "EX", 100);
		System.out.println(jedis.get("ni"));
		Thread.sleep(3000);
		Long life1 = jedis.ttl("ni");
		System.out.println("当前数据能存活"+life1+"秒");
		
	}
	
	//测试hash
	@Test
	public void testHash() {
		jedis.hset("user", "name", "超人");
		jedis.hset("user", "age", "11");
		jedis.hset("user", "id", "1");
		System.out.println(jedis.hgetAll("user"));
	}
	
	//测试list集合
	//队列
	@Test
	public void testList() {
		jedis.rpush("list", "1,2,3,4,5");
		System.out.println(jedis.lpop("list"));
		jedis.rpush("list", "1","2","3","4");
		System.out.println(jedis.lpop("list"));
	}
	
	/**控制事务
	 * 
	 */
	@Test
	public void testTx() {
		//事务开启
		Transaction transaction = jedis.multi();
		try {
			transaction.set("a", "aaa");
			transaction.set("b", "bbb");
			transaction.set("c", "ccc");
			int a = 1/0;
			//事务提交
			transaction.exec();
			
		} catch (Exception e) {
			e.printStackTrace();
			//3.事务回滚
			transaction.discard();
		}
	}
	
	@Test
	public void testShards() {
		List<JedisShardInfo> list = new ArrayList<>();
		list.add(new JedisShardInfo("192.168.88.129",6379));
		list.add(new JedisShardInfo("192.168.88.129",6380));
		list.add(new JedisShardInfo("192.168.88.129",6381));
		ShardedJedis jedis = new ShardedJedis(list);
		jedis.set("1906", "jedis分片测试");
		System.out.println(jedis.get("1906"));
	}
	
	
	/**
	 * 测试哨兵
	 * 用户通过哨兵,连接redis的主机
	 * 调用原理:用户
	 */
	@Test
	public void testSentinel() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.88.129:26379");
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster",sentinels);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("1906", "哨兵测试成功");
		System.out.println(jedis.get("1906"));
	}
	
	
	
	
	
}












