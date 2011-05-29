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
package net.javacrumbs.mocksocket.connection.sequential;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.javacrumbs.mocksocket.connection.AbstractMockConnection;
import net.javacrumbs.mocksocket.connection.MockConnection;
import net.javacrumbs.mocksocket.connection.SocketData;

/**
 * Mock connection. Can simulate multiple requests to the same address.
 * @author Lukas Krecan
 *
 */
public class SequentialMockConnection extends AbstractMockConnection implements MockConnection, SequentialMockRecorder{
	private final List<SocketData> responseData = new ArrayList<SocketData>();
	private final String address;
	public SequentialMockConnection(String address) {
		this.address = address;
	}

	public SequentialMockConnection andReturn(SocketData data) {
		this.responseData.add(data);
		return this;
	}
	
	public InputStream getInputStream() throws IOException {
		if (responseData.size()>actualConnection)
		{
			return new ByteArrayInputStream(responseData.get(actualConnection).getBytes());
		}
		else
		{
			throw new AssertionError("No more connections expected to \""+address+"\".");
		}
	}
}
