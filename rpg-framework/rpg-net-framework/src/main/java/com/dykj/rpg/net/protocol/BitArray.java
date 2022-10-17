package com.dykj.rpg.net.protocol;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * 编码规则
 * boolean类型，只有一位，0=false,1=true
 * 数字类型 第一个byte(11111111 第1位为是否为空[空=0，非空=1]，第2位为数字符号位[正=0，负=1])，后六位为数字编码长度[0-65],其余为其对应的数字编码
 * 字符串类型 前两个byte(第1位为是否为空[空=0，非空=1]，后十五位为编码长度[0-32767]，最多4096个字节),其余为其对应的字符编码
 * byte[] 类型 前两个byte(第1位为是否为空[空=0，非空=1]，后十五位为编码长度[0-32767]，最多4096个字节),其余为其对应的字符编码
 */

public class BitArray {

    private int size = 0;

    private byte[] buffer;

    private int writeBitIndex;

    private int readBitIndex;

    public boolean using;

    public BitArray(int size){
        if(size > 0){
            this.size = size;
            this.buffer = new byte[size];
            this.writeBitIndex = 0;
        }
    }

    /**
     * 初始化数据
     * @param _bytes
     * @param size
     * @return
     */
    public boolean initBytes(byte[] _bytes,int size){

        int index = 0;
        while(index < size){
            buffer[index] = _bytes[index];
            index ++ ;
        }
        writeBitIndex = size*8;
        return true;

    }

    public void release(){

        //buffer = null;

        //size = 0;

        int size = getWriteSize();
        for(int index = 0;index<size;index++){
            buffer[index] = 0;
        }

        writeBitIndex = 0;

        readBitIndex = 0;

        using = false;
    }

    public boolean writeLong(long _long){
        if(_long == 0){
            if(!writeBit(false)){
                return false;
            }
            return true;
        }else{
            if(!writeBit(true)){
                return false;
            }
        }

        int index = 63;
        byte size = 63;

        if(_long < 0){
            if(!writeBit(true)){
                return false;
            }
            _long = -_long;
        }else{
            if(!writeBit(false)){
                return false;
            }
        }
        index -- ;

        while(index >= 0 && ((_long & 0x7fffffffffffffffL) >> index) == 0){
            size --;
            index --;
        }

        int sizeIndex = 5;
        while(sizeIndex >= 0){
            if(!writeBit((((size & 0x7f) >> sizeIndex) & 0x1) == 1)){
                return false;
            }
            sizeIndex -- ;
        }

        while(index >= 0){
            if(!writeBit((((_long & 0x7fffffffffffffffL) >> index) & 0x1) == 1)){
                return false;
            }
            index -- ;
        }
        return true;

    }

    public long readLong(){
        boolean valid = readBit();
        if(!valid){
            return 0;
        }
        //符号
        boolean symbol = readBit();
        int sizeIndex = 5;
        int size = 0;
        while(sizeIndex >= 0){
            size = (size << 1 | (readBit()?1:0));
            sizeIndex -- ;
        }
        long _long = 0;
        while(size > 0){
            _long = (_long << 1 | (readBit()?1:0));
            size -- ;
        }

        return symbol?-_long:_long;

    }

    public boolean writeInt(int _int){
        if(_int == 0){
            if(!writeBit(false)){
                return false;
            }
            return true;
        }else{
            if(!writeBit(true)){
                return false;
            }
        }

        int index = 31;
        byte size = 31;

        if(_int < 0){
            if(!writeBit(true)){
                return false;
            }
            _int = -_int;
        }else{
            if(!writeBit(false)){
                return false;
            }
        }
        index -- ;

        while(index >= 0 && ((_int & 0x7fffffff) >> index) == 0){
            size --;
            index --;
        }

        int sizeIndex = 4;
        while(sizeIndex >= 0){
            if(!writeBit((((size & 0x7f) >> sizeIndex) & 0x1) == 1)){
                return false;
            }
            sizeIndex -- ;
        }

        while(index >= 0){
            if(!writeBit((((_int & 0x7fffffff) >> index) & 0x1) == 1)){
                return false;
            }
            index -- ;
        }
        return true;

    }

    public int readInt(){
        if(!readBit()){
            return 0;
        }
        //符号
        boolean symbol = readBit();
        int sizeIndex = 4;
        int size = 0;
        while(sizeIndex >= 0){
            size = (size << 1 | (readBit()?1:0));
            sizeIndex -- ;
        }
        int _int = 0;
        while(size > 0){
            _int = (_int << 1 | (readBit()?1:0));
            size -- ;
        }

        return symbol?-_int:_int;

    }

    public boolean writeShort(short _short){

        if(_short == 0){
            if(!writeBit(false)){
                return false;
            }
            return true;
        }else{
            if(!writeBit(true)){
                return false;
            }
        }

        int index = 15;
        byte size = 15;

        if(_short < 0){
            if(!writeBit(true)){
                return false;
            }
            _short = (short)-_short;
        }else{
            if(!writeBit(false)){
                return false;
            }
        }
        index -- ;

        while(index >= 0 && ((_short & 0x7fff) >> index) == 0){
            size --;
            index --;
        }

        int sizeIndex = 3;
        while(sizeIndex >= 0){
            if(!writeBit((((size & 0x7fff) >> sizeIndex) & 0x1) == 1)){
                return false;
            }
            sizeIndex -- ;
        }

        while(index >= 0){
            if(!writeBit((((_short & 0x7fff) >> index) & 0x1) == 1)){
                return false;
            }
            index -- ;
        }
        return true;

    }

    public short readShort(){
        if(!readBit()){
            return 0;
        }
        //符号
        boolean symbol = readBit();
        int sizeIndex = 3;
        int size = 0;
        while(sizeIndex >= 0){
            size = (size << 1 | (readBit()?1:0));
            sizeIndex -- ;
        }
        int _short = 0;
        while(size > 0){
            _short = (_short << 1 | (readBit()?1:0));
            size -- ;
        }

        short result = (short)_short;

        return (short)(result*(symbol?-1:1));

    }

    public boolean writeByte(byte _byte){

        if(_byte == 0){
            if(!writeBit(false)){
                return false;
            }
            return true;
        }else{
            if(!writeBit(true)){
                return false;
            }
        }

        int index = 7;
        byte size = 7;

        if(_byte < 0){
            if(!writeBit(true)){
                return false;
            }
            _byte = (byte)-_byte;
        }else{
            if(!writeBit(false)){
                return false;
            }
        }
        index -- ;

        while(index >= 0 && ((_byte & 0x7f) >> index) == 0){
            size --;
            index --;
        }

        int sizeIndex = 2;
        while(sizeIndex >= 0){
            if(!writeBit((((size & 0x7f) >> sizeIndex) & 0x1) == 1)){
                return false;
            }
            sizeIndex -- ;
        }

        while(index >= 0){
            if(!writeBit((((_byte & 0x7f) >> index) & 0x1) == 1)){
                return false;
            }
            index -- ;
        }

        return true;
    }

    public byte readByte(){
        if(!readBit()){
            return 0;
        }
        //符号
        boolean symbol = readBit();
        int sizeIndex = 2;
        int size = 0;
        while(sizeIndex >= 0){
            size = (size << 1 | (readBit()?1:0));
            sizeIndex -- ;
        }
        int _byte = 0;
        while(size > 0){
            _byte = (_byte << 1 | (readBit()?1:0));
            size -- ;
        }

        byte result = (byte)_byte;

        return (byte)(result*(symbol?-1:1));

    }

    public boolean writeBoolean(boolean _bool){
        return writeBit(_bool);
    }

    public boolean readBoolean(){
        return readBit();
    }

    /**
     * 写入字节数组 [数组长度+字节数组]
     * @param _bytes
     * @param size
     * @return
     */
    public boolean writeBytes(byte[] _bytes,int size){

        if(_bytes == null){
            if(!writeBit(false)){
                return false;
            }
            return true;
        }else{
            if(!writeBit(true)){
                return false;
            }
        }

        size = size*8;
        short sizeIndex = 14;
        while(sizeIndex >= 0){
            if(!writeBit((((size & 0x7fff) >> sizeIndex) & 0x1) == 1)){
                return false;
            }
            sizeIndex -- ;
        }

        int index = 0;
        while(index < size){
            if(!writeBit(((_bytes[index/8] >> (7-index%8)) & 0x1) == 1)){
                return false;
            }
            index ++ ;
        }
        return true;

    }

    public BitArray readBytes(){

        if(!readBit()){
            return null;
        }

        int size = 0;
        short sizeIndex = 14;
        while(sizeIndex >= 0){
            size |= ((readBit()?1:0) << sizeIndex);
            sizeIndex -- ;
        }

        int index = 0;

        //byte[] bytes = new byte[size >> 3];

        BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
        byte[] bytes = bitArray.getBytes();

        while(index < size){
            bytes[index >> 3] |= ((readBit()?1:0) << (7-index%8));
            index ++ ;
        }
        bitArray.setWriteIndex(size >> 3);
        return bitArray;

    }

    public boolean writeString(String _string){
        if(_string == null){
            writeBytes(null,0);
        }else{
            try {
                byte[] bytes = _string.getBytes("utf8");
                return writeBytes(bytes,bytes.length);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String readString() {
        BitArray bitArray = null;
        try {
            //byte[] bytes = readBytes();
            bitArray = readBytes();
            if(bitArray == null){
                return null;
            }
            String result = new String(bitArray.getBytes(),0,bitArray.getWriteSize(),"utf8");
            bitArray.release();
            return result;
        } catch (UnsupportedEncodingException e) {
            if(bitArray != null){
                bitArray.release();
            }
            e.printStackTrace();
        }
        return null;
    }

    //有效的bit
    private boolean writeBit(boolean valid){
        if(writeBitIndex >> 3 >= size){
            return false;
        }

        //用于清除buffer上次使用的痕迹
        if(writeBitIndex%8==0){
            buffer[writeBitIndex >> 3] = 0;
        }


        if(valid) {
            switch (writeBitIndex%8){
                case 0 : buffer[writeBitIndex >> 3] |= 0x80 ; break;
                case 1 : buffer[writeBitIndex >> 3] |= 0x40 ; break;
                case 2 : buffer[writeBitIndex >> 3] |= 0x20 ; break;
                case 3 : buffer[writeBitIndex >> 3] |= 0x10 ; break;
                case 4 : buffer[writeBitIndex >> 3] |= 0x08 ; break;
                case 5 : buffer[writeBitIndex >> 3] |= 0x04 ; break;
                case 6 : buffer[writeBitIndex >> 3] |= 0x02 ; break;
                case 7 : buffer[writeBitIndex >> 3] |= 0x01 ; break;
            }
        }

        writeBitIndex ++ ;
        return true;
    }

    //有效的bit
    private boolean readBit(){
        if(readBitIndex >= writeBitIndex){
            return false;
        }
        boolean valid = (((buffer[readBitIndex >> 3] >> (7-readBitIndex%8)) & 0x1) == 1);
        readBitIndex ++ ;
        return valid;
    }

    public void setWriteIndex(int writeIndex){
        writeBitIndex = writeIndex*8;
    }

    public int getWriteSize(){
        if(writeBitIndex%8 == 0){
            return writeBitIndex/8;
        }else{
            return writeBitIndex/8+1;
        }
    }

    public byte[] getBytes(){
        return buffer;
    }

    public byte[] getWriteByteArray(){
        return Arrays.copyOfRange(buffer, 0, getWriteSize());
    }

}
