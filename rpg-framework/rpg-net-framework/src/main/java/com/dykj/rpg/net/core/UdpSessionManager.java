package com.dykj.rpg.net.core;

import com.dykj.rpg.net.kcp.GameServerPrx;
import io.netty.util.HashedWheelTimer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public final class UdpSessionManager {
	private int counter;
	private final ReentrantLock counterLock;
	private ConcurrentHashMap<Integer, UdpSession> sessionTable;
	private ConcurrentHashMap<Integer,UdpSession> playerTable;
	private int sessionCheckTimes = 0;

	private long sessionCheckDuration = 0L;

	private long longestDuration = 0L;

	private UdpSessionWheelTimer sessionWheelTimer;

	private static UdpSessionManager instance = null;

	private UdpSessionManager() {
		this.sessionTable = new ConcurrentHashMap(3000);
		this.playerTable = new ConcurrentHashMap(3000);
		this.counterLock = new ReentrantLock();
		this.sessionWheelTimer = new UdpSessionWheelTimer(100,10);
	}

	public static UdpSessionManager getInstance() {
		if (instance == null) {
			instance = new UdpSessionManager();
		}
		return instance;
	}

	public UdpSession getSession(int sessionId){
		return sessionTable.get(sessionId);
	}

	public UdpSession getSessionByPlayerId(int playerId){
		return playerTable.get(playerId);
	}

	public synchronized int register(int _sessionId, int _playerId, GameServerPrx gameServerPrx) {

		UdpSession session = new UdpSession(_sessionId,_playerId,gameServerPrx);

		sessionTable.put(_sessionId,session);
		playerTable.put(_playerId,session);
		this.sessionWheelTimer.addUdpSession(session);

		session.createKcp(_playerId);

		return -1;
	}

	public synchronized int addSession(UdpSession session) {

		sessionTable.put(session.sessionId,session);
		this.sessionWheelTimer.addUdpSession(session);

		return -1;
	}

	public void fireSession(UdpSession session){
		fireSession(session.sessionId);
	}

	public void fireSession(int sessionId){
		UdpSession session = sessionTable.get(sessionId);
		if(session != null){
			int playerId = session.playerId;
			this.sessionWheelTimer.removeUdpSession(session);
			session.releaseKcp();
			sessionTable.remove(sessionId);
			playerTable.remove(playerId);
		}

	}

}