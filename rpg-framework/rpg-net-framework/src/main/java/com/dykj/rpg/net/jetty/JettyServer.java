package com.dykj.rpg.net.jetty;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dykj.rpg.util.spring.BeanFactory;
import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Jetty启动类
 *
 * @author jyb
 */
//public class JettyServer implements ApplicationContextAware {
public class JettyServer  implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger("game");
    private Server server;
    private ApplicationContext applicationContext;
    private String host = "0.0.0.0";
    private int port = 8080;
    private int securePort = 8443;
    private int minThread = 4;
    private int maxThread = 8;
    private int idleTimeout = 30000;

    private String name;

    private AtomicBoolean start  = new AtomicBoolean(false);
    /**
     * 注解包路径
     */
    private String packagePath;

    // private String descriptor = "web/WEB-INF/web.xml";
    // private String resourceBase = "web";

    public int getMinThread() {
        return minThread;
    }

    public void setMinThread(int minThread) {
        this.minThread = minThread;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public int getSecurePort() {
        return securePort;
    }

    public void setSecurePort(int securePort) {
        this.securePort = securePort;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		BeanFactory.setApplicationContext(applicationContext);
	}

    protected void init() {
        // Setup Threadpool
        //System.out.println(name);
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(minThread);
        threadPool.setMaxThreads(maxThread);
        threadPool.setName("Jetty-QTP");
        // Setup server
        server = new Server(threadPool);
        server.manage(threadPool);

        // Common HTTP configuration
        HttpConfiguration config = new HttpConfiguration();
        config.setSecurePort(securePort);
        config.addCustomizer(new ForwardedRequestCustomizer());
        config.addCustomizer(new SecureRequestCustomizer());
        config.setSendDateHeader(true);
        config.setSendServerVersion(true);

        // Http Connector
        HttpConnectionFactory http = new HttpConnectionFactory(config);
        ServerConnector httpConnector = new ServerConnector(server, http);
        if (host != null) {
            httpConnector.setHost(host);
        }
        httpConnector.setPort(port);
        httpConnector.setIdleTimeout(idleTimeout);
        server.addConnector(httpConnector);
        WebAppContext webAppContext = new WebAppContext();
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.setContextConfigLocation("classpath:servlet-context.xml");
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder("baseServlet", servlet), "/");

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{context, new DefaultHandler()});
        server.setHandler(handlers);

        XmlWebApplicationContext xmlWebAppContext = new XmlWebApplicationContext();
        xmlWebAppContext.setParent(applicationContext);
        xmlWebAppContext.setConfigLocation("");
        xmlWebAppContext.setServletContext(webAppContext.getServletContext());
        xmlWebAppContext.refresh();

        webAppContext.setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                xmlWebAppContext);

        xmlWebAppContext.refresh();
    }

    public void start() throws Exception {
        if (server == null)
            init();

        Thread thread = new Thread("Jetty-Server") {
            @Override
            public void run() {
                try {
                    logger.info("############################################################");
                    logger.info("#  jettyServer start --> address [{}] , port [{}] #",host,port);
                    logger.info("############################################################");
                    server.start();
                    start.set(true);
                    server.join();
                } catch (Exception e) {
                    logger.error("Jetty server start fail.", e);
                }
            }

        };

        thread.start();
    }

    public AtomicBoolean getStart() {
        return start;
    }

    public void setStart(AtomicBoolean start) {
        this.start = start;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    static class RestartHandler extends HandlerWrapper {
        private Logger logger = LoggerFactory.getLogger(getClass());

		/* ------------------------------------------------------------ */

        /**
         * @see HandlerWrapper#handle(String,
         * Request,
         * HttpServletRequest,
         * HttpServletResponse)
         */
        @Override
        public void handle(String target, Request baseRequest,
                           HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            super.handle(target, baseRequest, request, response);
            if (Boolean.valueOf(request.getParameter("restart"))) {
                final Server server = getServer();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                            server.stop();
                            Thread.sleep(100);
                            server.start();
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }
                }.start();
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
