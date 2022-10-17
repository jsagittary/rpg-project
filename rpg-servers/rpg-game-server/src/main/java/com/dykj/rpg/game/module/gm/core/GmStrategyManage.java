package com.dykj.rpg.game.module.gm.core;

import com.dykj.rpg.common.config.BaseManager;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.game.module.gm.consts.GmCommandConsts;
import com.dykj.rpg.game.module.gm.response.GmResponse;
import com.dykj.rpg.game.module.player.logic.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description gm策略执行者
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/24
 */
@Component
public class GmStrategyManage extends BaseManager {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 存储所有GM指令实现类 key - xxxx_xx(每个GM指令实现类的serviceName) value - 指令实现类
	 */
	private final Map<String, GmStrategy> gmStrategyMap = new ConcurrentHashMap<String, GmStrategy>();

	/**
	 * 匹配指定GM服务执行指定方法
	 * 
	 * @param command
	 *            gm指令
	 * @param player
	 *            玩家信息
	 * @return 响应协议体
	 * @throws Exception
	 */
	public GmResponse execute(String command, Player player) throws Exception {
		GmResponse gmResponse = null;
		// 拆分gm指令
		String[] commandPre = command.split(CommonConsts.STR_SPACE);
		// 服务实例 格式:前缀xxx_xxx(服务名_方法名)
		String[] serviceInst = commandPre[0].replace(GmCommandConsts.GM_PRE, "")
				.split(CommonConsts.STR_SYMBOL_UNDERSCORE);
		GmStrategy gmStrategy = gmStrategyMap.get(serviceInst[0]);// 根据服务名获取具体指令实例
		if (null != gmStrategy) {
			gmStrategy.setPlayer(player);// set玩家信息
			// 因多个参数是以空格区分则过滤下标0的元素分别对应服务名和方法则数组后续元素均为参数列表
			List<String> paramsList = new ArrayList<String>();
			for (int i = 0; i < commandPre.length; i++) {
				if (i != 0) {
					paramsList.add(commandPre[i]);
				}
			}

			Object element = null;
			for (Method method : gmStrategy.getClass().getMethods()) {
				if (method.getName().equals(serviceInst[1])) {
					Object obj = null;
					// 无参
					if (paramsList.isEmpty()) {
						obj = method.invoke(gmStrategy);
					} else {
						// 判断如果参数包含冒号则是键值对类型参数 否则是数组类型参数
						if (paramsList.stream().allMatch(e -> e.contains(CommonConsts.STR_COLON))) {
							// 根据&分隔后拿到每队参数列表 key=value
							element = paramsList.stream().map(e -> e.split(CommonConsts.STR_COLON))
									.collect(Collectors.toMap(e -> e[0], e -> e[1]));
						} else {
							// 表示只有一个参数
							if (paramsList.size() == 1) {
								element = paramsList.stream().findFirst().get();
							} else {
								element = paramsList;
							}
						}
						obj = method.invoke(gmStrategy, element);
					}

					if (null != obj) {
						gmResponse = (GmResponse) obj;
					}
					break;
				}
			}
		}
		return gmResponse;
	}

	/**
	 * 初始化所有GmStrategy的实现类
	 * 
	 * @throws Exception
	 */
	public void load() throws Exception {
		// 拿到所有GmStrategy的实现类
		Map<String, GmStrategy> strGmStrategyMap = app.getBeansOfType(GmStrategy.class);
		Field[] fields = GmCommandConsts.class.getDeclaredFields();
		for (Field field : fields) {
			Object fieldObj = field.get(GmCommandConsts.class);
			if (null != fieldObj && !GmCommandConsts.GM_PRE.equals(String.valueOf(fieldObj))) {
				// 拿到每条gm指令(格式:xxxx(具体service)_xxx(方法名))并替换掉前缀然后根据下划线分隔取第一位拿到服务名称
				String serviceName = String.valueOf(fieldObj).split(CommonConsts.STR_SPACE)[0]
						.replace(GmCommandConsts.GM_PRE, "").split(CommonConsts.STR_SYMBOL_UNDERSCORE)[0];
				// 拿到所有符合当前serviceName的map
				Map<String, GmStrategy> achieveMap = strGmStrategyMap.keySet().stream()
						.filter(e -> strGmStrategyMap.get(e).serviceName().equals(serviceName))
						.collect(Collectors.toMap(e -> e, strGmStrategyMap::get));
				if (achieveMap.size() != 1) {
					logger.error("GM服务实现类名重复: {}", serviceName);
					System.exit(-1);
				}
				GmStrategy specificGmService = achieveMap.values().stream().findFirst().get();
				gmStrategyMap.put(serviceName, specificGmService);
			}
		}
		logger.info("初始化GM服务[{}]完毕......", this.toString());
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		gmStrategyMap.values()
				.forEach(e -> stringBuilder.append(e.getClass().getSimpleName()).append(CommonConsts.STR_COMMA));
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
}