package com.dykj.rpg.net.kafka;

import com.dykj.rpg.net.kafka.annotation.KafkaCmdListener;
import com.dykj.rpg.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaListenerManager {
	private static KafkaListenerManager DEFAULT = new KafkaListenerManager();

	public static KafkaListenerManager getDefault() {
		return DEFAULT;
	}

	private static Logger logger = LoggerFactory.getLogger(KafkaListenerManager.class);

	/**
	 * Handler的包路径
	 */
	private String packeagePath;
	/**
	 * 指令集合
	 */
	private Map<Short, IListener> listeners = new ConcurrentHashMap<>();

	public void initHandler() {
		int cmd = 0;
		try {
			List<String> list = PackageUtil.getClassName(packeagePath, true);
			List<Class<?>> classes = new ArrayList<Class<?>>();
			for (String str : list) {
				Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(str);
				classes.add(clazz);
			}
			for (Class<?> cl : classes) {
				if (!IListener.class.isAssignableFrom(cl)) {
					continue;
				}
				KafkaCmdListener kfakaListener = cl.getAnnotation(KafkaCmdListener.class);
				if (kfakaListener == null) {
					logger.error(cl + " has no hander Annotation");
					System.exit(-1);
				}
				if (listeners.keySet().contains(kfakaListener.cmd())) {
					logger.error(cl + " cmd is ready exist cmd =" + kfakaListener.cmd());
					System.exit(-1);
				}
				cmd = kfakaListener.cmd();
				IListener listener = (IListener) cl.newInstance();
				listener.setKfkaCmd(kfakaListener.cmd());
				listeners.put(listener.getKfkaCmd(), listener);
			}
		} catch (Exception e) {
			logger.error("HandlerManager initHandler error :" + e + "  ,cmd = " + cmd);
			System.exit(-1);
		}
	}

	public void setPackeagePath(String packeagePath) {
		this.packeagePath = packeagePath;
	}

	public IListener getIListener(short cmd) {
		return listeners.get(cmd);
	}

}
