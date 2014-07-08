/**
 * 
 */
package com.wcc.kafka.http;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mason_deng
 * @date 2014-7-7下午2:12:11
 * @version 1.0
 */
public class RestServer {
	private static final Logger logger = LoggerFactory
			.getLogger("kafka.rest.server");

	public static void main(String[] args) throws Exception {
		HttpServlet servlet = Configurator.getServlet(args);
		Server server = new Server(8999);
		server.setAttribute(
				"org.eclipse.jetty.server.Request.maxFormContentSize",
				35 * 1024 * 1024);
		for (Connector connector : server.getConnectors()) {
			connector.setRequestBufferSize(35 * 1024 * 1024);
		}
		QueuedThreadPool pool = new QueuedThreadPool(40);
		server.setThreadPool(pool);
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);

		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(new ServletHolder(servlet), "/*");
		logger.info("start the rest server,run port:8081");
		server.start();
		server.join();
	}
}
