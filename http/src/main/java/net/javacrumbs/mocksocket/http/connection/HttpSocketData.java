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

package net.javacrumbs.mocksocket.http.connection;

import java.io.UnsupportedEncodingException;

import net.javacrumbs.mocksocket.connection.SocketData;

/**
 * Represents socket data.
 * @author Lukas Krecan
 *
 */
public class HttpSocketData extends SocketData {
	private static final String UTF8 = "UTF-8";
	
	private final String charset;

	/**
	 * 
	 * @param data
	 * @param charset for pretty printing
	 */
	public HttpSocketData(byte[] data, String charset) {
		super(data);
		this.charset = charset;
	}

	public HttpSocketData(byte[] data) {
		this(data, UTF8);
	}


	public HttpSocketData(String data, String charset) {
		this(stringToBytes(data, charset));
	}

	public HttpSocketData(String data) {
		this(data, UTF8);
	}

	private static byte[] stringToBytes(String data, String charset)  {
		try {
			return data.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	

	public String getAsString() {
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public String toString() {
		return getAsString();
	}

	public String getCharset() {
		return charset;
	}

}
