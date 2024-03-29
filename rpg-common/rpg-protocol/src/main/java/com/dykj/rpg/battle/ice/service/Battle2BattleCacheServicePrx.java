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

public interface Battle2BattleCacheServicePrx extends Ice.ObjectPrx
{
    public boolean pingBattleCache(int serverId);

    public boolean pingBattleCache(int serverId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_pingBattleCache(int serverId);

    public Ice.AsyncResult begin_pingBattleCache(int serverId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_pingBattleCache(int serverId, Ice.Callback __cb);

    public Ice.AsyncResult begin_pingBattleCache(int serverId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_pingBattleCache(int serverId, Callback_Battle2BattleCacheService_pingBattleCache __cb);

    public Ice.AsyncResult begin_pingBattleCache(int serverId, java.util.Map<String, String> __ctx, Callback_Battle2BattleCacheService_pingBattleCache __cb);

    public Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                 IceInternal.Functional_BoolCallback __responseCb, 
                                                 IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                 IceInternal.Functional_BoolCallback __responseCb, 
                                                 IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                 IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                 java.util.Map<String, String> __ctx, 
                                                 IceInternal.Functional_BoolCallback __responseCb, 
                                                 IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                 java.util.Map<String, String> __ctx, 
                                                 IceInternal.Functional_BoolCallback __responseCb, 
                                                 IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                 IceInternal.Functional_BoolCallback __sentCb);

    public boolean end_pingBattleCache(Ice.AsyncResult __result);

    public boolean enterToBattleServerSuccess(int[] playerIds, byte[] data);

    public boolean enterToBattleServerSuccess(int[] playerIds, byte[] data, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, byte[] data);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, byte[] data, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, byte[] data, Ice.Callback __cb);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, byte[] data, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, byte[] data, Callback_Battle2BattleCacheService_enterToBattleServerSuccess __cb);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, byte[] data, java.util.Map<String, String> __ctx, Callback_Battle2BattleCacheService_enterToBattleServerSuccess __cb);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, 
                                                            byte[] data, 
                                                            IceInternal.Functional_BoolCallback __responseCb, 
                                                            IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, 
                                                            byte[] data, 
                                                            IceInternal.Functional_BoolCallback __responseCb, 
                                                            IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                            IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, 
                                                            byte[] data, 
                                                            java.util.Map<String, String> __ctx, 
                                                            IceInternal.Functional_BoolCallback __responseCb, 
                                                            IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_enterToBattleServerSuccess(int[] playerIds, 
                                                            byte[] data, 
                                                            java.util.Map<String, String> __ctx, 
                                                            IceInternal.Functional_BoolCallback __responseCb, 
                                                            IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                            IceInternal.Functional_BoolCallback __sentCb);

    public boolean end_enterToBattleServerSuccess(Ice.AsyncResult __result);

    public boolean battleFinishResult(byte[] data);

    public boolean battleFinishResult(byte[] data, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data, Ice.Callback __cb);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data, Callback_Battle2BattleCacheService_battleFinishResult __cb);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data, java.util.Map<String, String> __ctx, Callback_Battle2BattleCacheService_battleFinishResult __cb);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                    IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data, 
                                                    java.util.Map<String, String> __ctx, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_battleFinishResult(byte[] data, 
                                                    java.util.Map<String, String> __ctx, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                    IceInternal.Functional_BoolCallback __sentCb);

    public boolean end_battleFinishResult(Ice.AsyncResult __result);
}
