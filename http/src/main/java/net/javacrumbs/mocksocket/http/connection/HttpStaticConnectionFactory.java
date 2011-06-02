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

import net.javacrumbs.mocksocket.connection.StaticConnectionFactory;

/**
 * Special factory for HTTP connections.
 */
public class HttpStaticConnectionFactory extends StaticConnectionFactory{
	
	
	public synchronized static UniversalHttpMockRecorder expectCallTo(String address) {
		HttpUniversalMockConnection mockConnection = new HttpUniversalMockConnection(address);
		if (connection(address)==null)
		{
			addExpectedConnection(address, mockConnection);
		}
		else
		{
			throw new IllegalArgumentException("Can not call expect twice with the same address. You have to call reset before each test. If you need simulate multiple requests to the same address, please call andReturn several times.");
		}
		return mockConnection;
	}
}
