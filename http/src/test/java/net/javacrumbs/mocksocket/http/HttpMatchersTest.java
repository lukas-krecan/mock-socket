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

import static net.javacrumbs.mocksocket.http.HttpMockSocket.content;
import static net.javacrumbs.mocksocket.http.HttpMockSocket.header;
import static net.javacrumbs.mocksocket.http.HttpMockSocket.status;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.javacrumbs.mocksocket.connection.data.RequestSocketData;

import org.junit.Test;


public class HttpMatchersTest {
	private static final RequestSocketData REQUEST = new RequestSocketData() {
		
		public InputStream getData() {
			return new ByteArrayInputStream("HTTP/1.0 200 OK\nLast-Modified: Wed, 10 Mar 2010 19:11:49 GMT\n\nTest".getBytes());
		}
		
		public String getAddress() {
			return "localhost:1111";
		}
	};

	@Test
	public void testMatchStatus()
	{
		assertThat(REQUEST, status(is(200)));
	}
	@Test(expected=AssertionError.class)
	public void testDoNotMatchStatus()
	{
		assertThat(REQUEST, status(is(300)));
	}
	@Test
	public void testMatchHeader()
	{
		assertThat(REQUEST, header("Last-Modified", is("Wed, 10 Mar 2010 19:11:49 GMT")));
	}
	@Test(expected=AssertionError.class)
	public void testDoMatchHeader()
	{
		assertThat(REQUEST, header("Last-Modified", is("Tue, 10 Mar 2010 19:11:49 GMT")));
	}
	@Test(expected=AssertionError.class)
	public void testDoMatchHeaderNotPresent()
	{
		assertThat(REQUEST, header("Expires", is("Tue, 10 Mar 2010 19:11:49 GMT")));
	}
	@Test
	public void testMatchContent()
	{
		assertThat(REQUEST, content(is("Test")));
	}
	@Test(expected=AssertionError.class)
	public void testDoMatchContent()
	{
		assertThat(REQUEST, content(is("Test2")));
	}
}
