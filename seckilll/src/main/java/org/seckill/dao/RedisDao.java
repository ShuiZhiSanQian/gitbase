package org.seckill.dao;

import org.seckill.entity.Seckill;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import ch.qos.logback.classic.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	private final JedisPool jedisPool;

	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}

	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	private RuntimeSchema<Long> schema1 = RuntimeSchema.createFrom(Long.class);
	public Seckill getSeckill(long seckillId) {
		// redis操作逻辑
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				// 并没有实现内部序列化操作
				// get->byte[]->反序列化->Object(Seckill)
				// 采用自定义序列化
				// protostuff:poji.
				byte[] bytes = jedis.get(key.getBytes());
				// 缓存重新取到
				if (bytes != null) {
					// 空对象
					Seckill seckill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					// seckill被反序列化
					return seckill;
				}

			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public String putSeckill(Seckill seckill) {
		// set object(Seckill) ->序列化->byte[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				// 超时缓存
				int timeout = 60 * 60;// 一个小时
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	//向redis根据seckillId存放剩余存量
	public String putCapacity(long seckillId,long capacity){
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "capacity:" + seckillId;
				byte[] bytes=ProtostuffIOUtil.toByteArray(capacity, schema1, 
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				// 超时缓存
				int timeout = 60 * 60;// 一个小时
				String result=jedis.setex(key.getBytes(),timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	public long getCapacity(long seckillId){
		try {
			Jedis jedis = jedisPool.getResource();
			try{
				String key = "capacity:" + seckillId;
				// 并没有实现内部序列化操作
				// get->byte[]->反序列化->Object(Seckill)
				// 采用自定义序列化
				// protostuff:poji.
				byte[] bytes = jedis.get(key.getBytes());
				// 缓存重新取到
				if (bytes != null) {
					// 空对象
					Long capacity = schema1.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, capacity, schema1);
					// seckill被反序列化
					return capacity;
				}
				
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}		
		return 0;
	}
}
