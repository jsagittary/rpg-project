/**
 *
 */
package com.dykj.rpg.net.kcp;

import com.dykj.rpg.net.core.UdpSession;
import io.netty.buffer.ByteBuf;

/**
 *
 * @author beykery
 */
public interface KcpListerner
{

  /**
   * kcp message
   *
   * @param bb the data
   * @param kcp
   */
  public void handleReceive(ByteBuf bb, Kcp kcp);

  /**
   *
   * kcp异常，之后此kcp就会被关闭
   *
   * @param ex 异常
   * @param kcp 发生异常的kcp，null表示非kcp错误
   */
  public void handleException(Throwable ex, Kcp kcp);

  /**
   * 关闭
   *
   * @param kcp
   */
  public void handleClose(Kcp kcp);

  /**
   * kcp的底层输出
   *
   * @param msg 消息
   * @param session session对象
   */
  public void out(ByteBuf msg, UdpSession session);
}
