package com.dykj.rpg.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jyb
 * @date 2020/11/23 11:04
 * @Description
 */
public class StringUtil {

	/**
	 * 1:100,2:300 转换成map
	 *
	 * @param str
	 * @return
	 */
	public static Map<Integer, Integer> stringConvertMap(String str) {
		if (null == str || str.equals("")) {
			return null;
		}
		Map<Integer, Integer> map = new HashMap<>();
		String[] results = str.split("\\:");
		for (String result : results) {
			String[] rs = result.split("\\,");
			map.put(Integer.valueOf(rs[0]), Integer.valueOf(rs[1]));
		}
		return map;
	}

	/**
	 * @param values
	 * @return 将map转换成 1:100,2:300
	 */
	public static String mapConvertString(Map<Integer, Integer> values) {
		StringBuffer stringBuffer = new StringBuffer();
		if (values == null || values.size() < 1) {
			return null;
		}
		int i = 0;
		for (Map.Entry<Integer, Integer> entry : values.entrySet()) {
			stringBuffer.append(entry.getKey());
			stringBuffer.append(":");
			stringBuffer.append(entry.getValue());
			if (i < values.size() - 1) {
				stringBuffer.append(",");
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * @param query
	 *            查询 String 字符串 Occurrence Number 出现次数
	 * @param Original
	 *            原字符串
	 * @param find
	 *            需要查找的字符串
	 * @return count 返回在原字符串中需要查找的字符串出现的次数
	 */
	public static int queryStringOccurrenceNumber(String original, String find) {
		int count = 0;
		while (original.contains(find)) {
			original = original.substring(original.indexOf(find) + find.length());
			count++;
		}
		return count;
	}
}