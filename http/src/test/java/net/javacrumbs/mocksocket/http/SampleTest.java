/**
 * Copyright 2009-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.mocksocket.http;

import static net.javacrumbs.mocksocket.MockSocket.address;
import static net.javacrumbs.mocksocket.MockSocket.getConnectionTo;
import static net.javacrumbs.mocksocket.http.HttpMockSocket.expectCallTo;
import static net.javacrumbs.mocksocket.http.HttpMockSocket.header;
import static net.javacrumbs.mocksocket.http.HttpMockSocket.method;
import static net.javacrumbs.mocksocket.http.HttpMockSocket.requestTo;
import static net.javacrumbs.mocksocket.http.HttpMockSocket.response;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.javacrumbs.mocksocket.MockSocketException;
import net.javacrumbs.mocksocket.connection.StaticConnectionFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Test;

public class SampleTest {
	private static final String EXPECTED_ADDRESS = "localhost:80";
	private static final String ADDRESS = "http://localhost/";

	@After
	public void reset()
	{
		StaticConnectionFactory.reset();
	}

	@Test
	public void testHttpClient() throws ClientProtocolException, IOException {
		expectCallTo(EXPECTED_ADDRESS)
			.andWhenRequest(method(is("POST")).and(address(is(EXPECTED_ADDRESS)))).thenReturn(response().withStatus(404))
			.andWhenRequest(method(is("GET"))).thenReturn(response().withStatus(200).withContent("Text"));
		
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpGet httpget = new HttpGet(ADDRESS);
		httpget.addHeader("Accept","text/plain");
		HttpResponse getResponse = httpclient.execute(httpget);
		assertThat(getResponse.getStatusLine().getStatusCode(), is(200));
		
		assertThat(getConnectionTo(EXPECTED_ADDRESS).requestData(), hasItem(header("Accept", is("text/plain"))));
		assertThat(getConnectionTo(EXPECTED_ADDRESS).requestData().get(0), method(is("GET")));
		assertThat(requestTo(EXPECTED_ADDRESS, 0).getMethod(), is("GET"));
		assertThat(requestTo(EXPECTED_ADDRESS, 0).getAddress(), is(EXPECTED_ADDRESS));
						
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		getResponse.getEntity().writeTo(outstream);
		assertThat(new String(outstream.toByteArray()), is("Text"));
		httpget.abort();

		HttpPost httppost = new HttpPost(ADDRESS);
		HttpResponse postResponse = httpclient.execute(httppost);
		assertThat(postResponse.getStatusLine().getStatusCode(), is(404));
		httppost.abort();
	}
	@Test(expected=MockSocketException.class)
	public void testSequential() throws ClientProtocolException, IOException {
		expectCallTo(EXPECTED_ADDRESS).andReturn(response().withStatus(200).withContent("Text"));
		
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpGet httpget = new HttpGet(ADDRESS);
		httpget.addHeader("Accept","text/plain");
		HttpResponse getResponse = httpclient.execute(httpget);
		assertThat(getResponse.getStatusLine().getStatusCode(), is(200));
		
		assertThat(getConnectionTo(EXPECTED_ADDRESS).requestData(), hasItem(header("Accept", is("text/plain"))));
		assertThat(getConnectionTo(EXPECTED_ADDRESS).requestData().get(0), method(is("GET")));
		assertThat(requestTo(EXPECTED_ADDRESS, 0).getMethod(), is("GET"));
		assertThat(requestTo(EXPECTED_ADDRESS, 0).getAddress(), is(EXPECTED_ADDRESS));
		
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		getResponse.getEntity().writeTo(outstream);
		assertThat(new String(outstream.toByteArray()), is("Text"));
		httpget.abort();
		
		HttpPost httppost = new HttpPost(ADDRESS);
		httpclient.execute(httppost);
	}
	@Test
	public void testHttpClientSequential() throws ClientProtocolException, IOException {
		expectCallTo(EXPECTED_ADDRESS)
		.andReturn("HTTP/1.0 200 OK\n\nTest")
		.andReturn("HTTP/1.0 404 Not Found\n");
		
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpGet httpget = new HttpGet(ADDRESS);
		httpget.addHeader("Accept","text/plain");
		HttpResponse getResponse = httpclient.execute(httpget);
		assertThat(getResponse.getStatusLine().getStatusCode(), is(200));
		
		assertThat(getConnectionTo(EXPECTED_ADDRESS).requestData(), hasItem(header("Accept", is("text/plain"))));
		httpget.abort();
		
		HttpPost httppost = new HttpPost(ADDRESS);
		HttpResponse postResponse = httpclient.execute(httppost);
		assertThat(postResponse.getStatusLine().getStatusCode(), is(404));
		httppost.abort();
	}
	@Test
	public void testReuseConnection() throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		testGet(httpclient);
		reset();
		testGet(httpclient);
	}

	private void testGet(HttpClient httpclient) throws IOException, ClientProtocolException {
		expectCallTo(EXPECTED_ADDRESS).andReturn("HTTP/1.0 200 OK\n\nTest");
				
		HttpGet httpget = new HttpGet(ADDRESS);
		httpget.addHeader("Accept","text/plain");
		HttpResponse getResponse = httpclient.execute(httpget);
		assertThat(getResponse.getStatusLine().getStatusCode(), is(200));
		
		assertThat(getConnectionTo(EXPECTED_ADDRESS).requestData(), hasItem(header("Accept", is("text/plain"))));
		httpget.abort();
	}
}
