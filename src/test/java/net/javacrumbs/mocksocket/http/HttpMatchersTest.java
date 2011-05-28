/*
 * Copyright 2005-2010 the original author or authors.
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

import static net.javacrumbs.mocksocket.http.HttpMatchers.content;
import static net.javacrumbs.mocksocket.http.HttpMatchers.hasStatus;
import static net.javacrumbs.mocksocket.http.HttpMatchers.header;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;


public class HttpMatchersTest {
	private static final String REQUEST = "HTTP/1.0 200 OK\nLast-Modified: Wed, 10 Mar 2010 19:11:49 GMT\n\nTest";

	@Test
	public void testMatchStatus()
	{
		assertThat(REQUEST, hasStatus(200));
	}
	@Test(expected=AssertionError.class)
	public void testDoNotMatchStatus()
	{
		assertThat(REQUEST, hasStatus(300));
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
