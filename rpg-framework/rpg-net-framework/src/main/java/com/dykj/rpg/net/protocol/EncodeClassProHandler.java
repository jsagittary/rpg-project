package com.dykj.rpg.net.protocol;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EncodeClassProHandler {
	private int MAX_LENGTH = 65536;
	private BitArray bitArray;

	public EncodeClassProHandler() {
	}

	public void init(BitArray bitArray){
		this.bitArray = bitArray;
	}

	public void release(){
		bitArray = null;
	}

	public boolean byteToBytes(byte _byte) {
		return bitArray.writeByte(_byte);
	}

	private boolean shortToBytes(short _short) {
		return bitArray.writeShort(_short);
	}

	private boolean intToBytes(int _int) {
		return bitArray.writeInt(_int);
	}

	private boolean longToBytes(long _long) {
		return bitArray.writeLong(_long);
	}

	private boolean boolToBytes(boolean _bool) {
		return bitArray.writeBoolean(_bool);
	}

	private boolean stringToBytes(String _string) {
		try {
			return bitArray.writeString(_string);
		} catch (Exception e) {
			return false;
		}
	}

	private boolean doubleToBytes(double _double) {
		String _string = String.valueOf(_double);
		return stringToBytes(_string);
	}

	private <T> boolean listToBytes(List<T> list) {
		int listLength = list.size();
		if (!shortToBytes((short) listLength)) {
			return false;
		}
		for (Object t : list) {
			paramToBytes(t);
		}
		return true;
	}

	private <T, V> boolean mapToBytes(Map<T, V> map) {
		int mapLength = map.size();
		if (!shortToBytes((short) mapLength)) {
			return false;
		}
		Set set = map.keySet();
		for (Object t : set) {
			paramToBytes(t);
			paramToBytes(map.get(t));
		}

		return true;
	}

	private boolean structToBytes(Protocol struct) {
		//byte[] bytes = struct.encode();
		BitArray bitArray2 = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
		struct.encode(bitArray2);
		bytesToBytes(bitArray2.getBytes(),bitArray2.getWriteSize());
		bitArray2.release();
		return true;
	}

	private boolean bytesToBytes(byte[] _bytes,int size) {

		return bitArray.writeBytes(_bytes,size);
	}

	public <T> boolean paramToBytes(T t) {
		if(t == null){
			return boolToBytes(false);
		}
		if ((t instanceof String)) {
			return stringToBytes((String) t);
		}
		if ((t instanceof Long)) {
			return longToBytes(((Long) t).longValue());
		}
		if ((t instanceof Integer)) {
			return intToBytes(((Integer) t).intValue());
		}
		if ((t instanceof Short)) {
			return shortToBytes(((Short) t).shortValue());
		}
		if ((t instanceof Byte)) {
			return byteToBytes(((Byte) t).byteValue());
		}
		if ((t instanceof Double)) {
			return doubleToBytes(((Double) t).doubleValue());
		}
		if ((t instanceof Boolean)) {
			return boolToBytes(((Boolean) t).booleanValue());
		}
		if ((t instanceof List)) {
			return listToBytes((List) t);
		}
		if ((t instanceof Map)) {
			return mapToBytes((Map) t);
		}
		if ((t instanceof Protocol)) {
			return structToBytes((Protocol) t);
		}
		return true;
	}

//	public <T> int getSizeByParam(T t) {
//		try{
//			if ((t instanceof String)) {
//				byte[] bytes = ((String)t).getBytes("utf8");
//				return bytes.length+2;
//			}
//			if ((t instanceof Long)) {
//				return 8;
//			}
//			if ((t instanceof Integer)) {
//				return 4;
//			}
//			if ((t instanceof Short)) {
//				return 2;
//			}
//			if ((t instanceof Byte)) {
//				return 1;
//			}
//			if ((t instanceof Double)) {
//				String str = String.valueOf(t);
//				byte[] bytes = str.getBytes("utf8");
//				return bytes.length+2;
//			}
//			if ((t instanceof Boolean)) {
//				return 1;
//			}
//			if ((t instanceof List)) {
//				int size = 2;
//				for (Object obj : (List)t) {
//					size += getSizeByParam(obj);
//				}
//				return size;
//			}
//			if ((t instanceof Map)) {
//				int size = 2;
//				Set set = ((Map)t).keySet();
//				for (Object key : set) {
//					size += getSizeByParam(key);
//					size += getSizeByParam(((Map)t).get(key));
//				}
//				return size;
//			}
//			if ((t instanceof Protocol)) {
//				return ((Protocol)t).getEncodeSize();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			return 0;
//		}
//		return 0;
//	}

	public byte[] complete() {
		return bitArray.getWriteByteArray();
	}
}