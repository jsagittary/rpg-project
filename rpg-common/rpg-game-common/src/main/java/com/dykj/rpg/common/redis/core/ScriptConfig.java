package com.dykj.rpg.common.redis.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author :jianbo.gong
 * @data:2018年9月14日 上午11:10:19
 * @describe:
 */
@Configuration
public class ScriptConfig {

	@Bean
	public RedisScript<Long> ipLimitScript() {
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("IpLimit.lua")));
		redisScript.setResultType(Long.class);
		return redisScript;
	}
	
	@Bean
	public RedisScript<String> hashScan() {
		DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("HashScan.lua")));
		redisScript.setResultType(String.class);
		return redisScript;
	}


	@Bean
	public RedisScript<Boolean> listPop() {
		DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("ListPop.lua")));
		redisScript.setResultType(Boolean.class);
		return redisScript;
	}

	@Bean
	public RedisScript<String> gameServerStartAction() {
		DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("GameServerStartAction.lua")));
		redisScript.setResultType(String.class);
		return redisScript;
	}

	@Bean
	public RedisScript<String> globalPlayer() {
		DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("GlobalPlayer.lua")));
		redisScript.setResultType(String.class);
		return redisScript;
	}

}
