---
--- Created by Administrator.
--- DateTime: 2019/5/25 11:27
---

-- 功能 : 在游戏服启动时，做一些检查（这样稳点，防止意外关服）
-- 如：1. 把 redis global online 本服玩家，全都搬到 offline

local onlineKey = KEYS[1]            -- online key
local offlineKey = KEYS[2]           -- offline key
local serverId = tonumber(ARGV[1])             -- 服务器 id
local maxServerPlayerNum = tonumber(ARGV[2])   -- 一个服最多多少人，用于计算玩家的服 id
local expireTime = tonumber(ARGV[3])           -- offline 过期时间，单位毫秒

local idList = redis.call('HKEYS', onlineKey)
if idList and 0 < #idList then
    for i = 1, #idList do
        local pid = idList[i]
        -- 计算方法，和 GameServerService.calculatePlayerServerId 保持一致
        local pSid = math.floor(pid / maxServerPlayerNum)
        if 0 >= pSid then
            -- 走这里，就是运维又设置错 id 了
            pSid = 1
        end
        if pSid == serverId then
            local gd = redis.call('HGET', onlineKey, pid)
            if gd then
                local p = cjson.decode(gd)
                if p then
                    p.online = false
                    redis.call('HDEL', onlineKey, pid)
                    redis.call('SET', offlineKey..':'..pid, cjson.encode(p), 'PX', expireTime)
                end
            end
        end
    end
end

return ""

