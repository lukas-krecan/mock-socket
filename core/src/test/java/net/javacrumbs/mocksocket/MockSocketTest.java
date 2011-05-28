package net.javacrumbs.mocksocket;

import static net.javacrumbs.mocksocket.connection.StaticConnectionFactory.expectCallTo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import net.javacrumbs.mocksocket.connection.StaticConnectionFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Test;

public class MockSocketTest {
	static {
		StaticConnectionFactory.bootstrap();
	}
	@After
	public void reset()
	{
		StaticConnectionFactory.reset();
	}

	@Test
	public void testHttpClient() throws ClientProtocolException, IOException {
		expectCallTo("localhost:80").andReturn("HTTP/1.0 200 OK\n\nTest".getBytes());
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://localhost/");
		HttpResponse response = httpclient.execute(httpget);
		System.out.println(printStream(response.getEntity().getContent()));
	}

	@Test
	public void testUrl() throws ClientProtocolException, IOException {
		expectCallTo("localhost:80").andReturn("HTTP/1.0 200 OK\n\nTest".getBytes());
		URL url = new URL("http://localhost/");
		System.out.println(printStream(url.openStream()));
	}

	private String printStream(InputStream stream) throws IOException {
		return new BufferedReader(new InputStreamReader(stream)).readLine();
	}
}
