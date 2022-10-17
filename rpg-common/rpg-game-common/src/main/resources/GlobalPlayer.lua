---
--- Created by Administrator.
--- DateTime: 2019/6/25 11:23
---

-- 功能：操作 global player manager

local onlineKey = KEYS[1]       -- 在线 map key
local offKey = KEYS[2]          -- 离线 key
local op = tonumber(ARGV[1])    -- 1上线，2离线，3更新, 4获得
local data = ARGV[2]            -- GlobalPlayerData 数据
local time = tonumber(ARGV[3])  -- 过期时间，单位毫秒

local gpd = cjson.decode(data)
local playerId = gpd.playerId

local hashAddFun, hashRemoveFun, hashGetFun, hashContain, keyExpireFun, keyDelFun

hashAddFun = function(k, id, d)
    redis.call('HSET', k, id, d)
end

hashRemoveFun = function(k, id)
    redis.call('HDEL', k, id)
end

hashGetFun = function(k, id)
    return redis.call('HGET', k, id)
end

hashContain = function(k, id)
    return redis.call('HEXISTS', k, id)
end

keyExpireFun = function(k, id, d, ms)
    redis.call('SET', k..':'..id, d, 'PX', ms)
end

keyDelFun = function(k, id)
    redis.call('DEL', k..':'..id)
end


if 1 == op then
    gpd.online = true
    hashAddFun(onlineKey, playerId, cjson.encode(gpd))
    keyDelFun(offKey, playerId)
elseif 2 == op then
    gpd.online = false
    hashRemoveFun(onlineKey, playerId)
    keyExpireFun(offKey, playerId, cjson.encode(gpd), time)
elseif 3 == op then
    -- 以 redis 的数据为准
    local exist = hashContain(onlineKey, playerId)
    if 1 == exist then
        gpd.online = true
        hashAddFun(onlineKey, playerId, cjson.encode(gpd))
    else
        gpd.online = false
        keyExpireFun(offKey, playerId, cjson.encode(gpd), time)
    end
elseif 4 == op then
    local exist = hashContain(onlineKey, playerId)
    if 1 == exist then
        return hashGetFun(onlineKey, playerId)
    else
        local sss = redis.call('GET', offKey..":"..playerId)
        return sss
    end
end

return ""