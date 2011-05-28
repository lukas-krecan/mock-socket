package net.javacrumbs.mocksocket;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

import net.javacrumbs.mocksocket.AbstractMockSocket;
import net.javacrumbs.mocksocket.MockSocketImplFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class MockSockeTest {
	static {
		try {
			Socket.setSocketImplFactory(new MockSocketImplFactory(new MockSocket()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHttpClient() throws ClientProtocolException, IOException {

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://localhost:1111/");
		HttpResponse response = httpclient.execute(httpget);
		System.out.println(printStream(response.getEntity().getContent()));
	}

	@Test
	public void testUrl() throws ClientProtocolException, IOException {
		URL url = new URL("http://localhost:1111/");
		System.out.println(printStream(url.openStream()));
	}

	private String printStream(InputStream stream) throws IOException {
		return new BufferedReader(new InputStreamReader(stream)).readLine();
	}

	private static class MockSocket extends AbstractMockSocket {
		@Override
		public void onConnect(String address) {

		}

		@Override
		public InputStream getInputStream() {
			return new ByteArrayInputStream("HTTP/1.0 200 OK\n\nTest".getBytes());
		}

		@Override
		public OutputStream getOutputStream() {
			return new ByteArrayOutputStream();
		}
	}
}
