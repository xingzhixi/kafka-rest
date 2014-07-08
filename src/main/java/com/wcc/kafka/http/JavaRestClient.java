
package com.wcc.kafka.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;


/**
 * @author mason_deng
 * @date 2014-7-8上午9:58:43
 * @version 1.0
 */
public class JavaRestClient implements Runnable {

	private int index;
	private CountDownLatch downLatch;

	public JavaRestClient(int i, CountDownLatch downLatch) {
		this.downLatch = downLatch;
		this.index = i;
	}

	@Override
	public void run() {
		System.out.println("thread:" + index+" begin send data...");
		StringEntity msg = null;
		try {
			msg = new StringEntity("{\"qty\":100,\"name\":\"iPad 4\"}");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		msg.setContentType("application/json");
		int count = 0;
		HttpClient client =  HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(
				"http://192.168.1.231:8999/topics/price_topic");
		post.setHeader("Accept", "application/json");	
		HttpResponse response;
		for (int i = 0; i < 100000; i++) {
			post.setEntity(msg);
			try {			
				response = client.execute(post);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					count++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("thread " + index + " send finshed...total " + count);
		this.downLatch.countDown();

	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {
		CountDownLatch latch = new CountDownLatch(5);
		long startTime = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++) {
			JavaRestClient client = new JavaRestClient(i, latch);
			service.submit(new Thread(client));
		}
		service.shutdown();
		latch.await();
		System.out.println("time cost: "
				+ (System.currentTimeMillis() - startTime) / 1000 + "s"
				+ " total records:");
	}

}
