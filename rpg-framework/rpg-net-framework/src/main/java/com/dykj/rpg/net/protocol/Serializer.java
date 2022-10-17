package com.dykj.rpg.net.protocol;

public final class Serializer
{
	public static Protocol deserialize(byte[] array, Class claz) throws Exception
	{
		Protocol content = (Protocol) claz.newInstance();
		BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
		bitArray.initBytes(array, array.length);
		content.decode(bitArray);
		bitArray.release();
		return content;
	}

	public static byte[] serialize(Protocol content) throws Exception
	{
		BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
		content.encode(bitArray);
		byte[] array = bitArray.getWriteByteArray();
		bitArray.release();
		return array;
	}
}
