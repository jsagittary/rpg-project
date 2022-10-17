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
// Generated from file `battle.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.dykj.rpg.battle.ice.service;

public interface BattleServicePrx extends Ice.ObjectPrx
{
    public String[] registerToBattleServer(int sessionId, int userId);

    public String[] registerToBattleServer(int sessionId, int userId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, int userId);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, int userId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, int userId, Ice.Callback __cb);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, int userId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, int userId, Callback_BattleService_registerToBattleServer __cb);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, int userId, java.util.Map<String, String> __ctx, Callback_BattleService_registerToBattleServer __cb);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, 
                                                        int userId, 
                                                        IceInternal.Functional_GenericCallback1<String[]> __responseCb, 
                                                        IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, 
                                                        int userId, 
                                                        IceInternal.Functional_GenericCallback1<String[]> __responseCb, 
                                                        IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                        IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, 
                                                        int userId, 
                                                        java.util.Map<String, String> __ctx, 
                                                        IceInternal.Functional_GenericCallback1<String[]> __responseCb, 
                                                        IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_registerToBattleServer(int sessionId, 
                                                        int userId, 
                                                        java.util.Map<String, String> __ctx, 
                                                        IceInternal.Functional_GenericCallback1<String[]> __responseCb, 
                                                        IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                        IceInternal.Functional_BoolCallback __sentCb);

    public String[] end_registerToBattleServer(Ice.AsyncResult __result);

    public void kickOut(int sessionId);

    public void kickOut(int sessionId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_kickOut(int sessionId);

    public Ice.AsyncResult begin_kickOut(int sessionId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_kickOut(int sessionId, Ice.Callback __cb);

    public Ice.AsyncResult begin_kickOut(int sessionId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_kickOut(int sessionId, Callback_BattleService_kickOut __cb);

    public Ice.AsyncResult begin_kickOut(int sessionId, java.util.Map<String, String> __ctx, Callback_BattleService_kickOut __cb);

    public Ice.AsyncResult begin_kickOut(int sessionId, 
                                         IceInternal.Functional_VoidCallback __responseCb, 
                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_kickOut(int sessionId, 
                                         IceInternal.Functional_VoidCallback __responseCb, 
                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                         IceInternal.Functional_BoolCallback __sentCb);

    public Ice.AsyncResult begin_kickOut(int sessionId, 
                                         java.util.Map<String, String> __ctx, 
                                         IceInternal.Functional_VoidCallback __responseCb, 
                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb);

    public Ice.AsyncResult begin_kickOut(int sessionId, 
                                         java.util.Map<String, String> __ctx, 
                                         IceInternal.Functional_VoidCallback __responseCb, 
                                         IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                         IceInternal.Functional_BoolCallback __sentCb);

    public void end_kickOut(Ice.AsyncResult __result);
}