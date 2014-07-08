/**
 * 
 */
package com.wcc.kafka.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mason_deng
 * @date 2014-7-7下午2:11:59
 * @version 1.0
 */
public class ProducerServlet extends HttpServlet {
	private static final long serialVersionUID = 567432491254080009L;
	private final Logger logger = LoggerFactory
			.getLogger("kafka.http.producer");
	private Producer<String, String> producer;

	public ProducerServlet(Properties properties) {
		ProducerConfig config = new ProducerConfig(properties);
		producer = new Producer<String, String>(config);
	}

	public String getMessage(HttpServletRequest request) throws IOException {
		StringBuilder body = new StringBuilder();
		BufferedReader reader = request.getReader();
		char[] buffer = new char[4096];
		int len = 0;
		while ((len = reader.read(buffer)) != -1) {
			body.append(buffer, 0, len);
		}
		reader.close();
		return body.toString();
	}

	public String getTopic(HttpServletRequest request) throws Exception {
		String[] segments = request.getRequestURI().split("/");
		if (segments.length != 3 || !segments[1].equals("topics")) {
			throw new Exception(
					"Please provide topic /topics/<topic> to post to");
		}
		return segments[2];
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String topic = null;
		try {
			topic = getTopic(request);
			String message = getMessage(request);
			String key = RandomStringUtils.random(5, new char[] { 'a', 'b',
					'c', 'd', 'e', 'f' });// used for partition by random key
			KeyedMessage<String, String> data = new KeyedMessage<String, String>(
					topic, key, message);
			producer.send(data);
			PrintWriter out = response.getWriter();
			out.print("recevied success!");
		} catch (Exception e) {
			logger.error("connect fail!" + e.getMessage());
			e.printStackTrace();
		}
	}
}
