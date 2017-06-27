package com.rustam.project.config;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.net.InetSocketAddress;
import java.util.EnumSet;

/**
 * Created by Rustam Kadyrov on 25.06.2017.
 */
public class EmbeddedContainer {

    private Server server;

    private final String host;
    private final int port;
    private final String contextPath;

    @Inject
    public EmbeddedContainer(@Named("server.host") String host,
                             @Named("server.port") int port,
                             @Named("server.contextPath") String contextPath) {
        this.host = host;
        this.port = port;
        this.contextPath = contextPath;
    }

    public void start(Injector injector) throws Exception {
        server = new Server(new InetSocketAddress(host, port));
        FilterHolder guiceFilter = new FilterHolder(injector.getInstance(GuiceFilter.class));
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath(contextPath);
        handler.addFilter(guiceFilter, "/*", EnumSet.allOf(DispatcherType.class));
        server.setHandler(handler);
        server.start();
    }

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }
}
