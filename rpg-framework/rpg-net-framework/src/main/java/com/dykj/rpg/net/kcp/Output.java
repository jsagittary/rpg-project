/**
 * out
 */
package com.dykj.rpg.net.kcp;

import com.dykj.rpg.net.core.UdpSession;
import io.netty.buffer.ByteBuf;

/**
 *
 * @author beykery
 */
public interface Output
{

  /**
   * kcp的底层输出
   *
   * @param msg 消息
   * @param session session对象
   */
  void out(ByteBuf msg, UdpSession session);
}
