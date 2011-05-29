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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.jetty.testing.HttpTester;

public class HttpProcessor extends HttpTester {
	protected final String charset;
	
	public HttpProcessor(String charset) {
		super(charset);
		this.charset = charset;
	}

	public void parse(Object http) {
		try {
			parse(objectToString(http));
		} 
		catch (IOException e) {
			throw new AssertionError("Can not parse http " + objectToString(http));
		}

	}

	private String objectToString(Object http) {
		try {
			if (http instanceof String)
			{
				return (String)http;
			}
			else if (http instanceof byte[])
			{
				return new String((byte[])http, charset);
			}
			else if (http instanceof InputStream)
			{
				return streamToString((InputStream)http);
			}
			else
			{
				throw new UnsupportedOperationException("Can not parse object of type "+http.getClass());
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	private String streamToString(InputStream http)  {
		try {
			java.io.BufferedReader reader = new BufferedReader(new InputStreamReader(http, charset));
			String line;
			StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine())!=null)
			{
				builder.append(line);
			}
			return builder.toString();
		} catch (IOException e) {
			throw new IllegalStateException("Can not parse http "+http);
		}
	}

}
