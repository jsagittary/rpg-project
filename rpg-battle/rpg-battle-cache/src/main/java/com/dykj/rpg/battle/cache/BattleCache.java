package com.dykj.rpg.battle.cache;

import com.dykj.rpg.mapping.ProtocolMapping;
import com.dykj.rpg.net.protocol.ProtocolPool;

public class BattleCache {

    public static void main(String[] args) {
        IceBox.Server icebox = new IceBox.Server();
        icebox.main(new String[] {"--Ice.Config=icebox.properties"});

    }

}
