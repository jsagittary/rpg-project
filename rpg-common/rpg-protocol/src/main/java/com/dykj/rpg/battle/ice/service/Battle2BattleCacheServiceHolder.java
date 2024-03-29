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

public final class Battle2BattleCacheServiceHolder extends Ice.ObjectHolderBase<Battle2BattleCacheService>
{
    public
    Battle2BattleCacheServiceHolder()
    {
    }

    public
    Battle2BattleCacheServiceHolder(Battle2BattleCacheService value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof Battle2BattleCacheService)
        {
            value = (Battle2BattleCacheService)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return _Battle2BattleCacheServiceDisp.ice_staticId();
    }
}
