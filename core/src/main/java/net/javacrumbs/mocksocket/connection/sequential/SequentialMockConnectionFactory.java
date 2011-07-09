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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.javacrumbs.mocksocket.MockSocketException;
import net.javacrumbs.mocksocket.connection.AbstractMockConnectionFactory;
import net.javacrumbs.mocksocket.connection.RequestRecorder;
import net.javacrumbs.mocksocket.connection.data.SocketData;

/**
 * Mock connection. Can simulate multiple requests to the same address.
 * @author Lukas Krecan
 *
 */
public class SequentialMockConnectionFactory extends AbstractMockConnectionFactory implements RequestRecorder, SequentialMockRecorder{
	private final List<SocketData> responseData = new ArrayList<SocketData>();
	public SequentialMockConnectionFactory() {
	}

	public SequentialMockRecorder andReturn(SocketData data) {
		this.responseData.add(data);
		return this;
	}
	
	protected InputStream createInputStream() {
		int actualConnection = getActualConnection();
		if (responseData.size()>actualConnection )
		{
			return responseData.get(actualConnection).getData();
		}
		else
		{
			throw new MockSocketException("No more connections expected. Requests recorded so far are: "+requestData());
		}
	}
}
