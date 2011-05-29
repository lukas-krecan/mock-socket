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

package net.javacrumbs.mocksocket.connection;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class HttpData {
	private final byte[] data;
	private final boolean printAsString;

	public HttpData(byte[] data, boolean printAsString) {
		this.data = data;
		this.printAsString = printAsString;
	}
	
	public HttpData(byte[] data) {
		this(data, true);
	}

	public HttpData(String data) {
		this(data, "UTF-8");
	}

	public HttpData(String data, String charset) {
		this(stringToBytes(data, charset));
	}

	private static byte[] stringToBytes(String data, String charset)  {
		try {
			return data.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	

	public byte[] getBytes() {
		return data;
	}

	@Override
	public String toString() {
		if (printAsString)
		{
			return getAsString("UTF-8");
		}
		else
		{
			return Arrays.toString(data);
		}
	}

	public String getAsString(String encoding) {
		try {
			return new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpData other = (HttpData) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}
	
}
