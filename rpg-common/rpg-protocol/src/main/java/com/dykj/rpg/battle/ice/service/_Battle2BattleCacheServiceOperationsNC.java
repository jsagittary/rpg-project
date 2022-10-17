// **********************************************************************
//
// Copyright (c) 2003-2017 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.6.4
//
// <auto-generated>
//
// Generated from file `battle2battleCache.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.dykj.rpg.battle.ice.service;

public interface _Battle2BattleCacheServiceOperationsNC
{
    boolean pingBattleCache(int serverId);

    boolean enterToBattleServerSuccess(int[] playerIds, byte[] data);

    boolean battleFinishResult(byte[] data);
}
