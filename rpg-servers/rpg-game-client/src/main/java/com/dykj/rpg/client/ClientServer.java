package com.dykj.rpg.client;

import com.dykj.rpg.net.jetty.JettyServer;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ClientServer {


    private static Logger logger = org.slf4j.LoggerFactory.getLogger(ClientServer.class);
    public static void main(String[] args)  throws Exception{
        long now = System.currentTimeMillis();
        new ClassPathXmlApplicationContext("applicationContext.xml");
        JettyServer jettyServer = BeanFactory.getBean(JettyServer.class);
        while (!(jettyServer.getStart().get())){
            // System.out.println(jettyServer.getStart().get()&&nettyServer.getStart().get());
            Thread.sleep(200);
        }
       // showLogo();
        logger.info("ClientServer START COMPLETE %% STARTTIME=" + (System.currentTimeMillis() - now));
    }

    private static void showLogo() {
        String s = "\n                  ___           ___                       ___           ___           ___           ___     \n"
                + "                 /\\  \\         /\\  \\                     /\\  \\         /\\__\\         /\\  \\         /\\  \\    \n"
                + "    ___          \\:\\  \\        \\:\\  \\         ___       /::\\  \\       /:/ _/_        \\:\\  \\       /::\\  \\   \n"
                + "   /\\__\\          \\:\\  \\        \\:\\  \\       /|  |     /:/\\:\\  \\     /:/ /\\  \\        \\:\\  \\     /:/\\:\\  \\  \n"
                + "  /:/__/      _____\\:\\  \\   ___  \\:\\  \\     |:|  |    /:/ /::\\  \\   /:/ /::\\  \\   ___ /::\\  \\   /:/ /::\\  \\ \n"
                + " /::\\  \\     /::::::::\\__\\ /\\  \\  \\:\\__\\    |:|  |   /:/_/:/\\:\\__\\ /:/_/:/\\:\\__\\ /\\  /:/\\:\\__\\ /:/_/:/\\:\\__\\\n"
                + " \\/\\:\\  \\__  \\:\\~~\\~~\\/__/ \\:\\  \\ /:/  /  __|:|__|   \\:\\/:/  \\/__/ \\:\\/:/ /:/  / \\:\\/:/  \\/__/ \\:\\/:/  \\/__/\n"
                + "  ~~\\:\\/\\__\\  \\:\\  \\        \\:\\  /:/  /  /::::\\  \\    \\::/__/       \\::/ /:/  /   \\::/__/       \\::/__/     \n"
                + "     \\::/  /   \\:\\  \\        \\:\\/:/  /   ~~~~\\:\\  \\    \\:\\  \\        \\/_/:/  /     \\:\\  \\        \\:\\  \\     \n"
                + "     /:/  /     \\:\\__\\        \\::/  /         \\:\\__\\    \\:\\__\\         /:/  /       \\:\\__\\        \\:\\__\\    \n"
                + "     \\/__/       \\/__/         \\/__/           \\/__/     \\/__/         \\/__/         \\/__/         \\/__/    ";

        logger.info(s);
    }
}
