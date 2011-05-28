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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock connection. Can simulate multimple requests to the same address.
 * @author Lukas Krecan
 *
 */
public class MockConnection implements Connection {
	private final List<byte[]> responseData = new ArrayList<byte[]>();
	private final List<ByteArrayOutputStream> requestData = new ArrayList<ByteArrayOutputStream>();
	private int actualConnection = -1;

	public MockConnection andReturn(byte[] data) {
		this.responseData.add(data);
		return this;
	}
	
	protected void onCreate()
	{
		actualConnection++;
		requestData.add(new ByteArrayOutputStream());
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(responseData.get(actualConnection));
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return requestData.get(actualConnection);
	}

	public byte[] requestData(int i) {
		return requestData.get(i).toByteArray();
	}

}
