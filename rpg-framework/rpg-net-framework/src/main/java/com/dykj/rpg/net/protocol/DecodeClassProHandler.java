package com.dykj.rpg.net.protocol;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecodeClassProHandler {
	private int MAX_LENGTH = 65536;
	private BitArray bitArray;

	public DecodeClassProHandler() {
	}

	public void init(BitArray biteArray){
		bitArray = biteArray;
	}

	public void release(){
		bitArray = null;
	}

	public byte bytesToByte() {
		return bitArray.readByte();
	}

	public short bytesToShort() {
		return bitArray.readShort();
	}

	public int bytesToInt() {
		return bitArray.readInt();
	}

	public long bytesToLong() {
		return bitArray.readLong();
	}

	public String bytesToString() {
		return bitArray.readString();
	}

	public double bytesToDouble() {
		String _double = bitArray.readString();
		return Double.parseDouble(_double);
	}

	public boolean bytesToBool() {
		return bitArray.readBoolean();
	}

	public Protocol bytesToStruct(String paramType) {
		BitArray bitArray2 = bitArray.readBytes();
		if(bitArray2 == null){
			return null;
		}
		try {
			Class clazz = Class.forName(paramType);
			Constructor constructor = clazz.getConstructor(null);
			Protocol protocol = (Protocol) constructor.newInstance(null);
			protocol.decode(bitArray2);
			bitArray2.release();
			return protocol;
		} catch (Exception e) {
			if(bitArray2 != null){
				bitArray2.release();
			}
			e.printStackTrace();
		}
		return null;
	}

	public <T> List<T> bytesToList(String paramType) {
		short _short = bytesToShort();

		List list = new ArrayList();
		for (int i = 0; i < _short; i++) {
			Object t = bytesToParam(paramType);
			list.add(t);
		}

		return list;
	}

	public <K, V> Map<K, V> bytesToMap(String keyType, String valueType) {
		short _short = bytesToShort();

		Map map = new HashMap();
		for (int i = 0; i < _short; i++) {
			Object k = bytesToParam(keyType);
			Object v = bytesToParam(valueType);
			map.put(k, v);
		}

		return map;
	}

	public Object bytesToParam(String paramType) {
		if (paramType.equals("String")) {
			return bytesToString();
		}
		if (paramType.equals("Long")) {
			return Long.valueOf(bytesToLong());
		}
		if (paramType.equals("Integer")) {
			return Integer.valueOf(bytesToInt());
		}
		if (paramType.equals("Short")) {
			return Short.valueOf(bytesToShort());
		}
		if (paramType.equals("Byte")) {
			return Byte.valueOf(bytesToByte());
		}
		if (paramType.equals("Double")) {
			return Double.valueOf(bytesToDouble());
		}
		if (paramType.equals("Boolean")) {
			return Boolean.valueOf(bytesToBool());
		}
		if ((paramType.startsWith("list<")) || (paramType.startsWith("List<"))) {
			int startIndex = paramType.indexOf("<");
			int lastIndex = paramType.lastIndexOf(">");

			paramType = paramType.substring(startIndex + 1, lastIndex).trim();

			return bytesToList(paramType);
		}
		if ((paramType.startsWith("map<")) || (paramType.startsWith("Map<"))) {
			int startIndex = paramType.indexOf("<");
			int lastIndex = paramType.lastIndexOf(">");

			paramType = paramType.substring(startIndex + 1, lastIndex);
			String[] params = paramType.trim().split(",");

			return bytesToMap(params[0], params[1]);
		}
		return bytesToStruct(paramType);
	}
}