package com.jt.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

	//		@Value("${redis.host}")
	//		private String host;
	//		@Value("${redis.port}")
	//		private Integer port;
	//		
	//		@Bean
	//		public Jedis jedis() {
	//			return new Jedis(host,port);
	//		}

	@Value("${redis.nodes}")
	private String nodes;  //node,node,node
	
	/**
	 * 配置redis集群
	 */
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodeSet = new HashSet<>();
		String[] arrayNodes =nodes.split(",");
		for (String node : arrayNodes) {
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			nodeSet.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodeSet);
	}
	
	
	/**
	 * 配置哨兵
	 * @return
	 */
	/*
	 * @Bean
	 * 
	 * @Scope("prototype") //多例对象 public Jedis jedis(JedisSentinelPool sentinelPool)
	 * { return sentinelPool.getResource(); }
	 * 
	 * @Bean public JedisSentinelPool jedisSentinelPool() { Set<String> sentinels =
	 * new HashSet<>(); sentinels.add(nodes); JedisSentinelPool sentinelPool = new
	 * JedisSentinelPool("mymaster",sentinels); return sentinelPool; }
	 */

	//	@Bean
	//	public ShardedJedis shardedJedis() {
	//		List<JedisShardInfo> shards = getList();
	//		return new ShardedJedis(shards);
	//	}
	//	private List<JedisShardInfo> getList() {
	//		List<JedisShardInfo> list = new ArrayList<>();
	//		String[] arrayNodes = nodes.split(",");
	//		for(String node:arrayNodes) {
	//			String host = node.split(":")[0];
	//			int port = Integer.parseInt(node.split(":")[1]);
	//			list.add(new JedisShardInfo(host,port));
	//		}
	//		return list;
	//	}

}
