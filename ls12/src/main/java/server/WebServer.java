package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import orm.service.DBService;
import orm.service.hibernate.DBServiceHibernateImpl;
import servlets.AdminServlet;
import servlets.AuthFilterImpl;
import servlets.UserServlet;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WebServer {
    private final static int PORT = 8080;
    private final static String STATIC_FOLDER = "/static";

    private static DBService hibernate;

    private static void resourcesInit() {
        hibernate = new DBServiceHibernateImpl();
        URL url = WebServer.class.getResource(STATIC_FOLDER + "/index.html");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start() throws Exception {
        resourcesInit();
        ResourceHandler resourceHandler = new ResourceHandler();
        Resource resource = Resource.newClassPathResource(STATIC_FOLDER);
        resourceHandler.setBaseResource(resource);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new AdminServlet()), "/admin");
        context.addServlet(new ServletHolder(new UserServlet(hibernate)), "/admin/user");
        context.addFilter(new FilterHolder(new AuthFilterImpl()), "/admin/*", null);

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }
}
