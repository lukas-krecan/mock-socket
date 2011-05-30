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

import java.io.IOException;
import java.util.Enumeration;

import net.javacrumbs.mocksocket.connection.SocketData;
import net.javacrumbs.mocksocket.http.connection.HttpData;

import org.eclipse.jetty.testing.HttpTester;

public class HttpParser  {
	private final HttpTester httpTester;
	
	public HttpParser(SocketData data) {
		if (data instanceof HttpData)
		{
			httpTester = createTester((HttpData)data);
		}
		else
		{
			httpTester = createTester(new HttpData(data.getBytes()));
		}
	}

	private HttpTester createTester(HttpData httpData) {
		HttpTester httpTester = new HttpTester(httpData.getCharset());	
		try {
			httpTester.parse(httpData.getAsString());
			return httpTester;
		} catch (IOException e) {
			throw new IllegalArgumentException("Can not parse data",e);
		}
	}

	public String getMethod() {
		return httpTester.getMethod();
	}

	public String getReason() {
		return httpTester.getReason();
	}

	public int getStatus() {
		return httpTester.getStatus();
	}

	public String getURI() {
		return httpTester.getURI();
	}

	public String getVersion() {
		return httpTester.getVersion();
	}

	public String getContentType() {
		return httpTester.getContentType();
	}

	public String getCharacterEncoding() {
		return httpTester.getCharacterEncoding();
	}

	public long getDateHeader(String name) {
		return httpTester.getDateHeader(name);
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> getHeaderNames() {
		return httpTester.getHeaderNames();
	}

	public long getLongHeader(String name) throws NumberFormatException {
		return httpTester.getLongHeader(name);
	}

	public String getHeader(String name) {
		return httpTester.getHeader(name);
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> getHeaderValues(String name) {
		return httpTester.getHeaderValues(name);
	}

	public String getContent() {
		return httpTester.getContent();
	}


}
