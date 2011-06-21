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
import net.javacrumbs.mocksocket.connection.data.DefaultSocketData;

import org.junit.Test;


public class HttpParserTest {

	@Test
	public void testCharset() throws Exception
	{
		String data = "GET http://localhost HTTP/1.1\n" + 
				"Content-type: text/plain;charset=ISO-8859-2\n" +
				"Content-Length: 9\n" + 
				"\n" + 
				"ěščřžýáíé";
		HttpRequest parser = new HttpParser(new DefaultSocketData(data.getBytes("ISO-8859-2")));
		assertEquals("ěščřžýáíé", parser.getContent());
	}
}
