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
// Generated from file `game2battleCache.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.dykj.rpg.battle.ice.service;

/**
 * Provides type-specific helper functions.
 **/
public final class Game2BattleCacheServicePrxHelper extends Ice.ObjectPrxHelperBase implements Game2BattleCacheServicePrx
{
    private static final String __enterToBattleCache_name = "enterToBattleCache";

    public boolean enterToBattleCache(int serverId, int playerId, byte[] data)
    {
        return enterToBattleCache(serverId, playerId, data, null, false);
    }

    public boolean enterToBattleCache(int serverId, int playerId, byte[] data, java.util.Map<String, String> __ctx)
    {
        return enterToBattleCache(serverId, playerId, data, __ctx, true);
    }

    private boolean enterToBattleCache(int serverId, int playerId, byte[] data, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        __checkTwowayOnly(__enterToBattleCache_name);
        return end_enterToBattleCache(begin_enterToBattleCache(serverId, playerId, data, __ctx, __explicitCtx, true, null));
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, int playerId, byte[] data)
    {
        return begin_enterToBattleCache(serverId, playerId, data, null, false, false, null);
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, int playerId, byte[] data, java.util.Map<String, String> __ctx)
    {
        return begin_enterToBattleCache(serverId, playerId, data, __ctx, true, false, null);
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, int playerId, byte[] data, Ice.Callback __cb)
    {
        return begin_enterToBattleCache(serverId, playerId, data, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, int playerId, byte[] data, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_enterToBattleCache(serverId, playerId, data, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, int playerId, byte[] data, Callback_Game2BattleCacheService_enterToBattleCache __cb)
    {
        return begin_enterToBattleCache(serverId, playerId, data, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, int playerId, byte[] data, java.util.Map<String, String> __ctx, Callback_Game2BattleCacheService_enterToBattleCache __cb)
    {
        return begin_enterToBattleCache(serverId, playerId, data, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, 
                                                    int playerId, 
                                                    byte[] data, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_enterToBattleCache(serverId, playerId, data, null, false, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, 
                                                    int playerId, 
                                                    byte[] data, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                    IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_enterToBattleCache(serverId, playerId, data, null, false, false, __responseCb, __exceptionCb, __sentCb);
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, 
                                                    int playerId, 
                                                    byte[] data, 
                                                    java.util.Map<String, String> __ctx, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_enterToBattleCache(serverId, playerId, data, __ctx, true, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_enterToBattleCache(int serverId, 
                                                    int playerId, 
                                                    byte[] data, 
                                                    java.util.Map<String, String> __ctx, 
                                                    IceInternal.Functional_BoolCallback __responseCb, 
                                                    IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                    IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_enterToBattleCache(serverId, playerId, data, __ctx, true, false, __responseCb, __exceptionCb, __sentCb);
    }

    private Ice.AsyncResult begin_enterToBattleCache(int serverId, 
                                                     int playerId, 
                                                     byte[] data, 
                                                     java.util.Map<String, String> __ctx, 
                                                     boolean __explicitCtx, 
                                                     boolean __synchronous, 
                                                     IceInternal.Functional_BoolCallback __responseCb, 
                                                     IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                     IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_enterToBattleCache(serverId, playerId, data, __ctx, __explicitCtx, __synchronous, 
                                        new IceInternal.Functional_TwowayCallbackBool(__responseCb, __exceptionCb, __sentCb)
                                            {
                                                public final void __completed(Ice.AsyncResult __result)
                                                {
                                                    Game2BattleCacheServicePrxHelper.__enterToBattleCache_completed(this, __result);
                                                }
                                            });
    }

    private Ice.AsyncResult begin_enterToBattleCache(int serverId, 
                                                     int playerId, 
                                                     byte[] data, 
                                                     java.util.Map<String, String> __ctx, 
                                                     boolean __explicitCtx, 
                                                     boolean __synchronous, 
                                                     IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__enterToBattleCache_name);
        IceInternal.OutgoingAsync __result = getOutgoingAsync(__enterToBattleCache_name, __cb);
        try
        {
            __result.prepare(__enterToBattleCache_name, Ice.OperationMode.Normal, __ctx, __explicitCtx, __synchronous);
            IceInternal.BasicStream __os = __result.startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(serverId);
            __os.writeInt(playerId);
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

    public boolean end_enterToBattleCache(Ice.AsyncResult __iresult)
    {
        IceInternal.OutgoingAsync __result = IceInternal.OutgoingAsync.check(__iresult, this, __enterToBattleCache_name);
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

    static public void __enterToBattleCache_completed(Ice.TwowayCallbackBool __cb, Ice.AsyncResult __result)
    {
        com.dykj.rpg.battle.ice.service.Game2BattleCacheServicePrx __proxy = (com.dykj.rpg.battle.ice.service.Game2BattleCacheServicePrx)__result.getProxy();
        boolean __ret = false;
        try
        {
            __ret = __proxy.end_enterToBattleCache(__result);
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

    private static final String __pingBattleCache_name = "pingBattleCache";

    public boolean pingBattleCache(int serverId)
    {
        return pingBattleCache(serverId, null, false);
    }

    public boolean pingBattleCache(int serverId, java.util.Map<String, String> __ctx)
    {
        return pingBattleCache(serverId, __ctx, true);
    }

    private boolean pingBattleCache(int serverId, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        __checkTwowayOnly(__pingBattleCache_name);
        return end_pingBattleCache(begin_pingBattleCache(serverId, __ctx, __explicitCtx, true, null));
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId)
    {
        return begin_pingBattleCache(serverId, null, false, false, null);
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId, java.util.Map<String, String> __ctx)
    {
        return begin_pingBattleCache(serverId, __ctx, true, false, null);
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId, Ice.Callback __cb)
    {
        return begin_pingBattleCache(serverId, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_pingBattleCache(serverId, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId, Callback_Game2BattleCacheService_pingBattleCache __cb)
    {
        return begin_pingBattleCache(serverId, null, false, false, __cb);
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId, java.util.Map<String, String> __ctx, Callback_Game2BattleCacheService_pingBattleCache __cb)
    {
        return begin_pingBattleCache(serverId, __ctx, true, false, __cb);
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                 IceInternal.Functional_BoolCallback __responseCb, 
                                                 IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_pingBattleCache(serverId, null, false, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                 IceInternal.Functional_BoolCallback __responseCb, 
                                                 IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                 IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_pingBattleCache(serverId, null, false, false, __responseCb, __exceptionCb, __sentCb);
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                 java.util.Map<String, String> __ctx, 
                                                 IceInternal.Functional_BoolCallback __responseCb, 
                                                 IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb)
    {
        return begin_pingBattleCache(serverId, __ctx, true, false, __responseCb, __exceptionCb, null);
    }

    public Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                 java.util.Map<String, String> __ctx, 
                                                 IceInternal.Functional_BoolCallback __responseCb, 
                                                 IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                 IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_pingBattleCache(serverId, __ctx, true, false, __responseCb, __exceptionCb, __sentCb);
    }

    private Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                  java.util.Map<String, String> __ctx, 
                                                  boolean __explicitCtx, 
                                                  boolean __synchronous, 
                                                  IceInternal.Functional_BoolCallback __responseCb, 
                                                  IceInternal.Functional_GenericCallback1<Ice.Exception> __exceptionCb, 
                                                  IceInternal.Functional_BoolCallback __sentCb)
    {
        return begin_pingBattleCache(serverId, __ctx, __explicitCtx, __synchronous, 
                                     new IceInternal.Functional_TwowayCallbackBool(__responseCb, __exceptionCb, __sentCb)
                                         {
                                             public final void __completed(Ice.AsyncResult __result)
                                             {
                                                 Game2BattleCacheServicePrxHelper.__pingBattleCache_completed(this, __result);
                                             }
                                         });
    }

    private Ice.AsyncResult begin_pingBattleCache(int serverId, 
                                                  java.util.Map<String, String> __ctx, 
                                                  boolean __explicitCtx, 
                                                  boolean __synchronous, 
                                                  IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__pingBattleCache_name);
        IceInternal.OutgoingAsync __result = getOutgoingAsync(__pingBattleCache_name, __cb);
        try
        {
            __result.prepare(__pingBattleCache_name, Ice.OperationMode.Normal, __ctx, __explicitCtx, __synchronous);
            IceInternal.BasicStream __os = __result.startWriteParams(Ice.FormatType.DefaultFormat);
            __os.writeInt(serverId);
            __result.endWriteParams();
            __result.invoke();
        }
        catch(Ice.Exception __ex)
        {
            __result.abort(__ex);
        }
        return __result;
    }

    public boolean end_pingBattleCache(Ice.AsyncResult __iresult)
    {
        IceInternal.OutgoingAsync __result = IceInternal.OutgoingAsync.check(__iresult, this, __pingBattleCache_name);
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

    static public void __pingBattleCache_completed(Ice.TwowayCallbackBool __cb, Ice.AsyncResult __result)
    {
        com.dykj.rpg.battle.ice.service.Game2BattleCacheServicePrx __proxy = (com.dykj.rpg.battle.ice.service.Game2BattleCacheServicePrx)__result.getProxy();
        boolean __ret = false;
        try
        {
            __ret = __proxy.end_pingBattleCache(__result);
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
    public static Game2BattleCacheServicePrx checkedCast(Ice.ObjectPrx __obj)
    {
        return checkedCastImpl(__obj, ice_staticId(), Game2BattleCacheServicePrx.class, Game2BattleCacheServicePrxHelper.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @param __ctx The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static Game2BattleCacheServicePrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        return checkedCastImpl(__obj, __ctx, ice_staticId(), Game2BattleCacheServicePrx.class, Game2BattleCacheServicePrxHelper.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @param __facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static Game2BattleCacheServicePrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        return checkedCastImpl(__obj, __facet, ice_staticId(), Game2BattleCacheServicePrx.class, Game2BattleCacheServicePrxHelper.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param __obj The untyped proxy.
     * @param __facet The name of the desired facet.
     * @param __ctx The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    public static Game2BattleCacheServicePrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        return checkedCastImpl(__obj, __facet, __ctx, ice_staticId(), Game2BattleCacheServicePrx.class, Game2BattleCacheServicePrxHelper.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param __obj The untyped proxy.
     * @return A proxy for this type.
     **/
    public static Game2BattleCacheServicePrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        return uncheckedCastImpl(__obj, Game2BattleCacheServicePrx.class, Game2BattleCacheServicePrxHelper.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param __obj The untyped proxy.
     * @param __facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    public static Game2BattleCacheServicePrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        return uncheckedCastImpl(__obj, __facet, Game2BattleCacheServicePrx.class, Game2BattleCacheServicePrxHelper.class);
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::service::Game2BattleCacheService"
    };

    /**
     * Provides the Slice type ID of this type.
     * @return The Slice type ID.
     **/
    public static String ice_staticId()
    {
        return __ids[1];
    }

    public static void __write(IceInternal.BasicStream __os, Game2BattleCacheServicePrx v)
    {
        __os.writeProxy(v);
    }

    public static Game2BattleCacheServicePrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Game2BattleCacheServicePrxHelper result = new Game2BattleCacheServicePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}