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
// Generated from file `battleBalance.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.dykj.rpg.battle.ice.service;

/**
 * Provides type-specific helper functions.
 **/
public final class BattleCacheServicePrxHelper extends Ice.ObjectPrxHelperBase implements BattleCacheServicePrx
{
    private static final String __askBattleCacheState_name = "askBattleCacheState";

    public boolean askBattleCacheState(int battleId, byte[] data)
    {
        return askBattleCacheState(battleId, data, null, false);
    }

    public boolean askBattleCacheState(int battleId, byte[] data, java.util.Map<String, String> __ctx)
    {
        return askBattleCacheState(battleId, data, __ctx, true);
    }

    private boolean askBattleCacheState(int battleId, byte[] data, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        __checkTwowayOnly(__askBattleCacheState_name);
        return end_askBattleCacheState(begin_askBattleCacheState(battleId, data, __ctx, __explicitCtx, true, null));
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, byte[] data)
    {
        return begin_askBattleCacheState(battleId, data, null, false, false, null);
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, byte[] data, java.util.Map<String, String> __ctx)
    {
        return begin_askBattleCacheState(battleId, data, __ctx, true, false, null);
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, byte[] data, Ice.Callback __cb)
    {
        return begin_askBattleCacheState(battleId, data, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, byte[] data, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_askBattleCacheState(battleId, data, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, byte[] data, Callback_BattleCacheService_askBattleCacheState __cb)
    {
        return begin_askBattleCacheState(battleId, data, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, byte[] data, java.util.Map<String, String> __ctx, Callback_BattleCacheService_askBattleCacheState __cb)
    {
        return begin_askBattleCacheState(battleId, data, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, 
                                                     byte[] data, 
                                                     IceInternal.Functional_BoolCallback __responseCb, 
                                                     IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_askBattleCacheState(battleId, data, null, false, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, 
                                                     byte[] data, 
                                                     IceInternal.Functional_BoolCallback __responseCb, 
                                                     IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                     IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_askBattleCacheState(battleId, data, null, false, false, __responseCb, __exceptionCb, __sentCb);
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, 
                                                     byte[] data, 
                                                     java.util.Map<String, String> __ctx, 
                                                     IceInternal.Functional_BoolCallback __responseCb, 
                                                     IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_askBattleCacheState(battleId, data, __ctx, true, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_askBattleCacheState(int battleId, 
                                                     byte[] data, 
                                                     java.util.Map<String, String> __ctx, 
                                                     IceInternal.Functional_BoolCallback __responseCb, 
                                                     IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                     IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_askBattleCacheState(battleId, data, __ctx, true, false, __responseCb, __exceptionCb, __sentCb);
    }

    private Ice.AsyncResult begin_askBattleCacheState(int battleId, 
                                                      byte[] data, 
                                                      java.util.Map<String, String> __ctx, 
                                                      boolean __explicitCtx, 
                                                      boolean __synchronous, 
                                                      IceInternal.Functional_BoolCallback __responseCb, 
                                                      IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                      IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_askBattleCacheState(battleId, data, __ctx, __explicitCtx, __synchronous, 
                                         new IceInternal.Functional_TwowayCallbackBool(__responseCb, __exceptionCb, __sentCb)
                                             {
                                                 public final void __completed(Ice.AsyncResult __result)
                                                 {
                                                     BattleCacheServicePrxHelper.__askBattleCacheState_completed(this, __result);
                                                 }
                                             });
    }

    private Ice.AsyncResult begin_askBattleCacheState(int battleId, 
                                                      byte[] data, 
                                                      java.util.Map<String, String> __ctx, 
                                                      boolean __explicitCtx, 
                                                      boolean __synchronous, 
                                                      IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__askBattleCacheState_name);
        IceInternal.OutgoingAsync __result = getOutgoingAsync(__askBattleCacheState_name, __cb);
        try
        {
            __result.prepare(__askBattleCacheState_name, Ice.OperationMode.Normal, __ctx, __explicitCtx, __synchronous);
            IceInternal.BasicStream __os = __result.startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(battleId);
            DataHelper.write(__os, data);
            __result.endWriteParams();
            __result.invoke();
        }
        catch(Ice.Exception __ex)
        {
            __result.abort(__ex);
        }
        return __result;
    }

    public boolean end_askBattleCacheState(Ice.AsyncResult __iresult)
    {
        IceInternal.OutgoingAsync __result = IceInternal.OutgoingAsync.check(__iresult, this, __askBattleCacheState_name);
        try
        {
            if(!__result.__wait())
            {
                try
                {
                    __result.throwUserException();
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            IceInternal.BasicStream __is = __result.startReadParams();
            boolean __ret;
            __ret = __is.readBool();
            __result.endReadParams();
            return __ret;
        }
        finally
        {
            if(__result != null)
            {
                __result.cacheMessageBuffers();
            }
        }
    }

    static public void __askBattleCacheState_completed(Ice.TwowayCallbackBool __cb, Ice.AsyncResult __result)
    {
        com.dykj.rpg.battle.ice.service.BattleCacheServicePrx __proxy = (com.dykj.rpg.battle.ice.service.BattleCacheServicePrx)__result.getProxy();
        boolean __ret = false;
        try
        {
            __ret = __proxy.end_askBattleCacheState(__result);
        }
        catch(Ice.LocalException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        catch(Ice.SystemException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        __cb.response(__ret);
    }

    private static final String __getBattleCache_name = "getBattleCache";

    public byte[] getBattleCache(int battleId)
    {
        return getBattleCache(battleId, null, false);
    }

    public byte[] getBattleCache(int battleId, java.util.Map<String, String> __ctx)
    {
        return getBattleCache(battleId, __ctx, true);
    }

    private byte[] getBattleCache(int battleId, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        __checkTwowayOnly(__getBattleCache_name);
        return end_getBattleCache(begin_getBattleCache(battleId, __ctx, __explicitCtx, true, null));
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId)
    {
        return begin_getBattleCache(battleId, null, false, false, null);
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId, java.util.Map<String, String> __ctx)
    {
        return begin_getBattleCache(battleId, __ctx, true, false, null);
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId, Ice.Callback __cb)
    {
        return begin_getBattleCache(battleId, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_getBattleCache(battleId, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId, Callback_BattleCacheService_getBattleCache __cb)
    {
        return begin_getBattleCache(battleId, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId, java.util.Map<String, String> __ctx, Callback_BattleCacheService_getBattleCache __cb)
    {
        return begin_getBattleCache(battleId, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId, 
                                                IceInternal.Functional_GenericCallback1<byte[]> __responseCb, 
                                                IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_getBattleCache(battleId, null, false, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId, 
                                                IceInternal.Functional_GenericCallback1<byte[]> __responseCb, 
                                                IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_getBattleCache(battleId, null, false, false, __responseCb, __exceptionCb, __sentCb);
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId, 
                                                java.util.Map<String, String> __ctx, 
                                                IceInternal.Functional_GenericCallback1<byte[]> __responseCb, 
                                                IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_getBattleCache(battleId, __ctx, true, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_getBattleCache(int battleId, 
                                                java.util.Map<String, String> __ctx, 
                                                IceInternal.Functional_GenericCallback1<byte[]> __responseCb, 
                                                IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_getBattleCache(battleId, __ctx, true, false, __responseCb, __exceptionCb, __sentCb);
    }

    private Ice.AsyncResult begin_getBattleCache(int battleId, 
                                                 java.util.Map<String, String> __ctx, 
                                                 boolean __explicitCtx, 
                                                 boolean __synchronous, 
                                                 IceInternal.Functional_GenericCallback1<byte[]> __responseCb, 
                                                 IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                 IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_getBattleCache(battleId, __ctx, __explicitCtx, __synchronous, 
                                    new IceInternal.Functional_TwowayCallbackArg1<byte[]>(__responseCb, __exceptionCb, __sentCb)
                                        {
                                            public final void __completed(Ice.AsyncResult __result)
                                            {
                                                BattleCacheServicePrxHelper.__getBattleCache_completed(this, __result);
                                            }
                                        });
    }

    private Ice.AsyncResult begin_getBattleCache(int battleId, 
                                                 java.util.Map<String, String> __ctx, 
                                                 boolean __explicitCtx, 
                                                 boolean __synchronous, 
                                                 IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__getBattleCache_name);
        IceInternal.OutgoingAsync __result = getOutgoingAsync(__getBattleCache_name, __cb);
        try
        {
            __result.prepare(__getBattleCache_name, Ice.OperationMode.Normal, __ctx, __explicitCtx, __synchronous);
            IceInternal.BasicStream __os = __result.startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(battleId);
            __result.endWriteParams();
            __result.invoke();
        }
        catch(Ice.Exception __ex)
        {
            __result.abort(__ex);
        }
        return __result;
    }

    public byte[] end_getBattleCache(Ice.AsyncResult __iresult)
    {
        IceInternal.OutgoingAsync __result = IceInternal.OutgoingAsync.check(__iresult, this, __getBattleCache_name);
        try
        {
            if(!__result.__wait())
            {
                try
                {
                    __result.throwUserException();
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            IceInternal.BasicStream __is = __result.startReadParams();
            byte[] __ret;
            __ret = DataHelper.read(__is);
            __result.endReadParams();
            return __ret;
        }
        finally
        {
            if(__result != null)
            {
                __result.cacheMessageBuffers();
            }
        }
    }

    static public void __getBattleCache_completed(Ice.TwowayCallbackArg1<byte[]> __cb, Ice.AsyncResult __result)
    {
        com.dykj.rpg.battle.ice.service.BattleCacheServicePrx __proxy = (com.dykj.rpg.battle.ice.service.BattleCacheServicePrx)__result.getProxy();
        byte[] __ret = null;
        try
        {
            __ret = __proxy.end_getBattleCache(__result);
        }
        catch(Ice.LocalException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        catch(Ice.SystemException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        __cb.response(__ret);
    }

    private static final String __removeBattleCache_name = "removeBattleCache";

    public boolean removeBattleCache(int battleId)
    {
        return removeBattleCache(battleId, null, false);
    }

    public boolean removeBattleCache(int battleId, java.util.Map<String, String> __ctx)
    {
        return removeBattleCache(battleId, __ctx, true);
    }

    private boolean removeBattleCache(int battleId, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        __checkTwowayOnly(__removeBattleCache_name);
        return end_removeBattleCache(begin_removeBattleCache(battleId, __ctx, __explicitCtx, true, null));
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId)
    {
        return begin_removeBattleCache(battleId, null, false, false, null);
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId, java.util.Map<String, String> __ctx)
    {
        return begin_removeBattleCache(battleId, __ctx, true, false, null);
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId, Ice.Callback __cb)
    {
        return begin_removeBattleCache(battleId, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_removeBattleCache(battleId, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId, Callback_BattleCacheService_removeBattleCache __cb)
    {
        return begin_removeBattleCache(battleId, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId, java.util.Map<String, String> __ctx, Callback_BattleCacheService_removeBattleCache __cb)
    {
        return begin_removeBattleCache(battleId, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId, 
                                                   IceInternal.Functional_BoolCallback __responseCb, 
                                                   IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_removeBattleCache(battleId, null, false, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId, 
                                                   IceInternal.Functional_BoolCallback __responseCb, 
                                                   IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                   IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_removeBattleCache(battleId, null, false, false, __responseCb, __exceptionCb, __sentCb);
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId, 
                                                   java.util.Map<String, String> __ctx, 
                                                   IceInternal.Functional_BoolCallback __responseCb, 
                                                   IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_removeBattleCache(battleId, __ctx, true, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_removeBattleCache(int battleId, 
                                                   java.util.Map<String, String> __ctx, 
                                                   IceInternal.Functional_BoolCallback __responseCb, 
                                                   IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                   IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_removeBattleCache(battleId, __ctx, true, false, __responseCb, __exceptionCb, __sentCb);
    }

    private Ice.AsyncResult begin_removeBattleCache(int battleId, 
                                                    java.util.Map<String, String> __ctx, 
                                                    boolean __explicitCtx, 
                                                    boolean __synchronous, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                    IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_removeBattleCache(battleId, __ctx, __explicitCtx, __synchronous, 
                                       new IceInternal.Functional_TwowayCallbackBool(__responseCb, __exceptionCb, __sentCb)
                                           {
                                               public final void __completed(Ice.AsyncResult __result)
                                               {
                                                   BattleCacheServicePrxHelper.__removeBattleCache_completed(this, __result);
                                               }
                                           });
    }

    private Ice.AsyncResult begin_removeBattleCache(int battleId, 
                                                    java.util.Map<String, String> __ctx, 
                                                    boolean __explicitCtx, 
                                                    boolean __synchronous, 
                                                    IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__removeBattleCache_name);
        IceInternal.OutgoingAsync __result = getOutgoingAsync(__removeBattleCache_name, __cb);
        try
        {
            __result.prepare(__removeBattleCache_name, Ice.OperationMode.Normal, __ctx, __explicitCtx, __synchronous);
            IceInternal.BasicStream __os = __result.startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(battleId);
            __result.endWriteParams();
            __result.invoke();
        }
        catch(Ice.Exception __ex)
        {
            __result.abort(__ex);
        }
        return __result;
    }

    public boolean end_removeBattleCache(Ice.AsyncResult __iresult)
    {
        IceInternal.OutgoingAsync __result = IceInternal.OutgoingAsync.check(__iresult, this, __removeBattleCache_name);
        try
        {
            if(!__result.__wait())
            {
                try
                {
                    __result.throwUserException();
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            IceInternal.BasicStream __is = __result.startReadParams();
            boolean __ret;
            __ret = __is.readBool();
            __result.endReadParams();
            return __ret;
        }
        finally
        {
            if(__result != null)
            {
                __result.cacheMessageBuffers();
            }
        }
    }

    static public void __removeBattleCache_completed(Ice.TwowayCallbackBool __cb, Ice.AsyncResult __result)
    {
        com.dykj.rpg.battle.ice.service.BattleCacheServicePrx __proxy = (com.dykj.rpg.battle.ice.service.BattleCacheServicePrx)__result.getProxy();
        boolean __ret = false;
        try
        {
            __ret = __proxy.end_removeBattleCache(__result);
        }
        catch(Ice.LocalException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        catch(Ice.SystemException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        __cb.response(__ret);
    }

    private static final String __updateBattleCache_name = "updateBattleCache";

    public boolean updateBattleCache(int battleId, byte[] data)
    {
        return updateBattleCache(battleId, data, null, false);
    }

    public boolean updateBattleCache(int battleId, byte[] data, java.util.Map<String, String> __ctx)
    {
        return updateBattleCache(battleId, data, __ctx, true);
    }

    private boolean updateBattleCache(int battleId, byte[] data, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        __checkTwowayOnly(__updateBattleCache_name);
        return end_updateBattleCache(begin_updateBattleCache(battleId, data, __ctx, __explicitCtx, true, null));
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, byte[] data)
    {
        return begin_updateBattleCache(battleId, data, null, false, false, null);
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, byte[] data, java.util.Map<String, String> __ctx)
    {
        return begin_updateBattleCache(battleId, data, __ctx, true, false, null);
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, byte[] data, Ice.Callback __cb)
    {
        return begin_updateBattleCache(battleId, data, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, byte[] data, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_updateBattleCache(battleId, data, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, byte[] data, Callback_BattleCacheService_updateBattleCache __cb)
    {
        return begin_updateBattleCache(battleId, data, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, byte[] data, java.util.Map<String, String> __ctx, Callback_BattleCacheService_updateBattleCache __cb)
    {
        return begin_updateBattleCache(battleId, data, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, 
                                                   byte[] data, 
                                                   IceInternal.Functional_BoolCallback __responseCb, 
                                                   IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_updateBattleCache(battleId, data, null, false, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, 
                                                   byte[] data, 
                                                   IceInternal.Functional_BoolCallback __responseCb, 
                                                   IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                   IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_updateBattleCache(battleId, data, null, false, false, __responseCb, __exceptionCb, __sentCb);
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, 
                                                   byte[] data, 
                                                   java.util.Map<String, String> __ctx, 
                                                   IceInternal.Functional_BoolCallback __responseCb, 
                                                   IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_updateBattleCache(battleId, data, __ctx, true, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_updateBattleCache(int battleId, 
                                                   byte[] data, 
                                                   java.util.Map<String, String> __ctx, 
                                                   IceInternal.Functional_BoolCallback __responseCb, 
                                                   IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                   IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_updateBattleCache(battleId, data, __ctx, true, false, __responseCb, __exceptionCb, __sentCb);
    }

    private Ice.AsyncResult begin_updateBattleCache(int battleId, 
                                                    byte[] data, 
                                                    java.util.Map<String, String> __ctx, 
                                                    boolean __explicitCtx, 
                                                    boolean __synchronous, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                    IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_updateBattleCache(battleId, data, __ctx, __explicitCtx, __synchronous, 
                                       new IceInternal.Functional_TwowayCallbackBool(__responseCb, __exceptionCb, __sentCb)
                                           {
                                               public final void __completed(Ice.AsyncResult __result)
                                               {
                                                   BattleCacheServicePrxHelper.__updateBattleCache_completed(this, __result);
                                               }
                                           });
    }

    private Ice.AsyncResult begin_updateBattleCache(int battleId, 
                                                    byte[] data, 
                                                    java.util.Map<String, String> __ctx, 
                                                    boolean __explicitCtx, 
                                                    boolean __synchronous, 
                                                    IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__updateBattleCache_name);
        IceInternal.OutgoingAsync __result = getOutgoingAsync(__updateBattleCache_name, __cb);
        try
        {
            __result.prepare(__updateBattleCache_name, Ice.OperationMode.Normal, __ctx, __explicitCtx, __synchronous);
            IceInternal.BasicStream __os = __result.startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(battleId);
            DataHelper.write(__os, data);
            __result.endWriteParams();
            __result.invoke();
        }
        catch(Ice.Exception __ex)
        {
            __result.abort(__ex);
        }
        return __result;
    }

    public boolean end_updateBattleCache(Ice.AsyncResult __iresult)
    {
        IceInternal.OutgoingAsync __result = IceInternal.OutgoingAsync.check(__iresult, this, __updateBattleCache_name);
        try
        {
            if(!__result.__wait())
            {
                try
                {
                    __result.throwUserException();
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            IceInternal.BasicStream __is = __result.startReadParams();
            boolean __ret;
            __ret = __is.readBool();
            __result.endReadParams();
            return __ret;
        }
        finally
        {
            if(__result != null)
            {
                __result.cacheMessageBuffers();
            }
        }
    }

    static public void __updateBattleCache_completed(Ice.TwowayCallbackBool __cb, Ice.AsyncResult __result)
    {
        com.dykj.rpg.battle.ice.service.BattleCacheServicePrx __proxy = (com.dykj.rpg.battle.ice.service.BattleCacheServicePrx)__result.getProxy();
        boolean __ret = false;
        try
        {
            __ret = __proxy.end_updateBattleCache(__result);
        }
        catch(Ice.LocalException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        catch(Ice.SystemException __ex)
        {
            __cb.exception(__ex);
            return;
        }
        __cb.response(__ret);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static BattleCacheServicePrx checkedCast(Ice.ObjectPrx __obj)
    {
        return checkedCastImpl(__obj, ice_staticId(), BattleCacheServicePrx.class, BattleCacheServicePrxHelper.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @param __ctx The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static BattleCacheServicePrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        return checkedCastImpl(__obj, __ctx, ice_staticId(), BattleCacheServicePrx.class, BattleCacheServicePrxHelper.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @param __facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static BattleCacheServicePrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        return checkedCastImpl(__obj, __facet, ice_staticId(), BattleCacheServicePrx.class, BattleCacheServicePrxHelper.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @param __facet The name of the desired facet.
     * @param __ctx The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static BattleCacheServicePrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        return checkedCastImpl(__obj, __facet, __ctx, ice_staticId(), BattleCacheServicePrx.class, BattleCacheServicePrxHelper.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param __obj The untyped proxy.
     * @return A proxy for this type.
     **/
    public static BattleCacheServicePrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        return uncheckedCastImpl(__obj, BattleCacheServicePrx.class, BattleCacheServicePrxHelper.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param __obj The untyped proxy.
     * @param __facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    public static BattleCacheServicePrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        return uncheckedCastImpl(__obj, __facet, BattleCacheServicePrx.class, BattleCacheServicePrxHelper.class);
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::service::BattleCacheService"
    };

    /**
     * Provides the Slice type ID of this type.
     * @return The Slice type ID.
     **/
    public static String ice_staticId()
    {
        return __ids[1];
    }

    public static void __write(IceInternal.BasicStream __os, BattleCacheServicePrx v)
    {
        __os.writeProxy(v);
    }

    public static BattleCacheServicePrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            BattleCacheServicePrxHelper result = new BattleCacheServicePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}