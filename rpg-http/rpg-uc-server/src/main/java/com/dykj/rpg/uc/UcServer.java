package com.dykj.rpg.uc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UcServer {
    public static Logger logger = LoggerFactory.getLogger(UcServer.class);
    public static void main(String[] args) throws Exception {
        long now = System.currentTimeMillis();
        new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.info("UcServer START SUCCESS %% STARTTIME=" + (System.currentTimeMillis() - now));
        logger.info("UcServer START COMPLETE");
        logger.info("LINUX LOG SUCCESS");
    }


    private static void showLogo() {
        String s = "oooooooooo.                                                                           ooooooooooooo                     oooo                              oooo                                   \n" + "`888'   `Y8b                                                                          8'   888   `8                     `888                              `888                                   \n" + " 888      888  .ooooo.  ooo. .oo.    .oooooooo oooo    ooo  .ooooo.  oooo  oooo            888       .ooooo.   .ooooo.   888 .oo.   ooo. .oo.    .ooooo.   888   .ooooo.   .oooooooo oooo    ooo \n" + " 888      888 d88' `88b `888P\"Y88b  888' `88b   `88.  .8'  d88' `88b `888  `888            888      d88' `88b d88' `\"Y8  888P\"Y88b  `888P\"Y88b  d88' `88b  888  d88' `88b 888' `88b   `88.  .8'  \n" + " 888      888 888   888  888   888  888   888    `88..8'   888   888  888   888            888      888ooo888 888        888   888   888   888  888   888  888  888   888 888   888    `88..8'   \n" + " 888     d88' 888   888  888   888  `88bod8P'     `888'    888   888  888   888            888      888    .o 888   .o8  888   888   888   888  888   888  888  888   888 `88bod8P'     `888'    \n" + "o888bood8P'   `Y8bod8P' o888o o888o `8oooooo.      .8'     `Y8bod8P'  `V88V\"V8P'          o888o     `Y8bod8P' `Y8bod8P' o888o o888o o888o o888o `Y8bod8P' o888o `Y8bod8P' `8oooooo.      .8'     \n" + "                                    d\"     YD  .o..P'                                                                                                                     d\"     YD  .o..P'      \n" + "                                    \"Y88888P'  `Y8P'                                                                                                                      \"Y88888P'  `Y8P'  ";
        logger.info(s);
    }
}
