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

public final class BattleCacheServiceHolder extends Ice.ObjectHolderBase<BattleCacheService>
{
    public
    BattleCacheServiceHolder()
    {
    }

    public
    BattleCacheServiceHolder(BattleCacheService value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof BattleCacheService)
        {
            value = (BattleCacheService)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return _BattleCacheServiceDisp.ice_staticId();
    }
}
