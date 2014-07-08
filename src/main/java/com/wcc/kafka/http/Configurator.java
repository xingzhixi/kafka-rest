/**
 * 
 */
package com.wcc.kafka.http;

import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * @author mason_deng
 * @date 2014-7-7下午2:11:49
 * @version 1.0
 */
public class Configurator {
	private static final Logger logger = LoggerFactory.getLogger("kafka.http.config");
	public static HttpServlet getServlet(String[] args) throws Exception {
		if (args.length < 1) {
			throw new Exception(
					"Provide first parameter as 'consumer' or 'producer'");
		}
		String endpoint = args[0];
		if (endpoint.equals("consumer")) {
			return null;
		} else if (endpoint.equals("producer")) {
			return getProducerServlet(args);
		} else {
			throw new Exception(
					"Provide first parameter as 'consumer' or 'producer'");
		}
	}

	public static ProducerServlet getProducerServlet(String[] args) {
		OptionParser parser = new OptionParser();
		parser.accepts("broker-list", "Comma separated of kafka nodes")
				.withRequiredArg().describedAs("brokers").ofType(String.class);
		OptionSet options = parser.parse(args);

		Properties props = new Properties();	
	    props.put("metadata.broker.list", options.valueOf("broker-list"));
	    props.put("request.required.acks", "0");
	    props.put("serializer.class", "kafka.serializer.StringEncoder");
	    logger.info(String.format(props.toString()));
		return new ProducerServlet(props);
	}
}
