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

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.junit.Test;


public class HttpResponseGeneratorTest {

	@Test
	public void testGenerateResponse()
	{
		HttpResponseGenerator generator = new HttpResponseGenerator();
		generator
			.withStatus(200)
			.withContent("Some not interesting content")
			.withHeader("Content-type","text/plain")
			.withDateHeader("Expires", new Date(1308384530297L));
		String response = new String(generator.generate());
		assertEquals("HTTP/1.1 200 OK\r\n" + 
				"Content-type: text/plain\r\n" + 
				"Expires: Sat, 18 Jun 2011 08:08:50 GMT\r\n" + 
				"Content-Length: 28\r\n" + 
				"\r\n" + 
				"Some not interesting content", response);
	}
	@Test
	public void testGenerateEncoding() throws UnsupportedEncodingException
	{
		HttpResponseGenerator generator = new HttpResponseGenerator();
		generator
		.withStatus(200)
		.withContent("ěščřžýáíé")
		.withHeader("Content-type","text/plain;charset=utf-8")
		.withDateHeader("Expires", new Date(1308384530297L));
		String response = new String(generator.generate(),"UTF-8");
		assertEquals("HTTP/1.1 200 OK\r\n" + 
				"Content-type: text/plain;charset=utf-8\r\n" + 
				"Expires: Sat, 18 Jun 2011 08:08:50 GMT\r\n" + 
				"Content-Length: 18\r\n" + 
				"\r\n" + 
				"ěščřžýáíé", response);
	}
	@Test
	public void testGenerateEncoding2() throws UnsupportedEncodingException
	{
		HttpResponseGenerator generator = new HttpResponseGenerator();
		generator
		.withStatus(200)
		.withContent("ěščřžýáíé")
		.withHeader("Content-type","text/plain;charset=iso-8859-2")
		.withDateHeader("Expires", new Date(1308384530297L));
		String response = new String(generator.generate(),"iso-8859-2");
		assertEquals("HTTP/1.1 200 OK\r\n" + 
				"Content-type: text/plain;charset=iso-8859-2\r\n" + 
				"Expires: Sat, 18 Jun 2011 08:08:50 GMT\r\n" + 
				"Content-Length: 9\r\n" + 
				"\r\n" + 
				"ěščřžýáíé", response);
	}
}
