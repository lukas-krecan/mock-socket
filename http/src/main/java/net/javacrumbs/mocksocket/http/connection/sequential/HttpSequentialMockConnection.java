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

package net.javacrumbs.mocksocket.http.connection.sequential;

import net.javacrumbs.mocksocket.connection.SocketData;
import net.javacrumbs.mocksocket.connection.sequential.SequentialMockConnection;
import net.javacrumbs.mocksocket.http.connection.HttpData;

public class HttpSequentialMockConnection extends SequentialMockConnection {

	public HttpSequentialMockConnection(String address) {
		super(address);
	}
	
	@Override
	protected SocketData createSocketData(byte[] data) {
		return new HttpData(data);
	}

}
