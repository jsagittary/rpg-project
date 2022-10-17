---
--- Created by Administrator.
--- DateTime: 2019/1/5 17:01
---

-- 功能 ：出 n 个元素
-- KEYS[1] : 列表的 key
-- ARGV[1] : n
-- 返回 ：成功 true / 失败 false

local key = KEYS[1]
local n = tonumber(ARGV[1])

for i = 1, n do
    local e = redis.call('LPOP', key)
    if not e then
        return false
    end
end
return true