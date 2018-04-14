package de.zalando.ep.zalenium.aspect;


import org.openqa.grid.web.Hub;
import org.seleniumhq.jetty9.servlet.ServletContextHandler;
import org.seleniumhq.jetty9.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.zalando.ep.zalenium.cometd.CometDInitializer;


public aspect HubAspect {
    private static final Logger log = LoggerFactory.getLogger(HubAspect.class.getName());

    pointcut callAddDefaultServlets(ServletContextHandler handler, Hub hub) :
        call (private void Hub.addDefaultServlets(ServletContextHandler)) && args(handler) && target(hub);
    
    before(ServletContextHandler handler, Hub hub) : callAddDefaultServlets(handler, hub) {
        log.info("Registering custom Zalenium servlets");
        
        ServletHolder cometdHolder = new ServletHolder("cometd", org.cometd.server.CometDServlet.class);
        cometdHolder.setAsyncSupported(true);
        cometdHolder.setInitOrder(1); // equivalent of <load-on-startup />
        String cometdPathSpec = "/cometd/*";
        cometdHolder.setInitParameter("ws.cometdURLMapping", cometdPathSpec);
        
        handler.addServlet(cometdHolder, cometdPathSpec);
        
        ServletHolder cometdInitHolder = new ServletHolder("cometdInitialiser", CometDInitializer.class);
        cometdInitHolder.setInitOrder(2);
        
        handler.addServlet(cometdInitHolder, "/ignoreThisServlet");
    }
}
