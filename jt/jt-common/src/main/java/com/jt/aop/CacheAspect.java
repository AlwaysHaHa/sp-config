package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Aspect  //标识切面
@Component  //将该类交给spring容器管理
public class CacheAspect {
	
	//当前切面位于common中.必须添加required = false
	@Autowired(required = false)
	//private ShardedJedis jedis;  //分片機制                                                                                                                                                                                                                                                                                                                                                                                                             
	//private Jedis jedis;  //哨兵機制
	private JedisCluster jedis;  
	/**
	 * 通知的的选择:
	 *  	是否需要控制目标方法执行,使用环绕通知
	 *  
	 *  动态生成key  包名.类名.方法名::parentId
	 */
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint joinPoint,Cache_Find cacheFind) {
		String key = getKey(joinPoint,cacheFind);
		String result = jedis.get(key);
		Object data = null;
		try{if(StringUtils.isEmpty(result)) {
			//缓存没数据,目标方法执行查询数据库
			data = joinPoint.proceed();
			System.out.println("数据库");
			String value = ObjectMapperUtil.toJSON(data);
			if(cacheFind.seconds()==0) {
				jedis.set(key, value);
			}else {
				jedis.setex(key, cacheFind.seconds(), value);
			}
		}else {
			//缓存有数据
			Class returnClass = getClass(joinPoint);
			System.out.println("缓存");
			data = ObjectMapperUtil.toObject(result, returnClass);
		}
		}catch(Throwable e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return data;
	}
	private Class getClass(ProceedingJoinPoint joinPoint) {
		MethodSignature signature  = (MethodSignature)joinPoint.getSignature();
		return signature.getReturnType();
	}
	private String getKey(ProceedingJoinPoint joinPoint, Cache_Find cacheFind) {
		//1.判断用户是否定义key值
		String key = cacheFind.key();
		if(!StringUtils.isEmpty(key)) {
			return key; //返回用户自定义的key
		}
		//表示需要自动生成key
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		key = className+"."+methodName+"::"+args[0];
		return key;
		
	}
	
	
	
	
	
	
	
	
	
	
}
