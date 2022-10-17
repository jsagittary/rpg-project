ALTER TABLE `player_equip`
ADD COLUMN `base_score`  int(11) NULL DEFAULT 0 COMMENT '基础评分' AFTER `equip_pos`


ALTER TABLE `player_skill`
ADD COLUMN `soul_change_time`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP AFTER `skill_type`