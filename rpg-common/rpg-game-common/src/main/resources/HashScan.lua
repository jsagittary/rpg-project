---
--- Created by Administrator.
--- DateTime: 2019/5/21 14:34
---


-- 功能： 哈希迭代器
-- KEYS[1] : map key
-- ARGV[2] : 游标
-- ARGV[3] : 数量

local key = KEYS[1]
local index = tonumber(ARGV[1])
local count = tonumber(ARGV[2])

local s = redis.call('HSCAN', key, index, 'COUNT', count)
return s
