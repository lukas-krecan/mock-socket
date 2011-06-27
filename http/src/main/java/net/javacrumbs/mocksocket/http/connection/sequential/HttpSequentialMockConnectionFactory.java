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

import net.javacrumbs.mocksocket.connection.data.OutputSocketData;
import net.javacrumbs.mocksocket.connection.data.SocketData;
import net.javacrumbs.mocksocket.connection.sequential.SequentialMockConnectionFactory;
import net.javacrumbs.mocksocket.http.connection.HttpData;
import net.javacrumbs.mocksocket.http.connection.HttpOutputData;

public class HttpSequentialMockConnectionFactory extends SequentialMockConnectionFactory implements SequentialHttpMockRecorder{

	@Override
	protected OutputSocketData createRequestSocket(String address) {
		return new HttpOutputData(address);
	}
	@Override
	public SequentialHttpMockRecorder andReturn(SocketData data) {
		return (SequentialHttpMockRecorder) super.andReturn(data);
	}
	
	public SequentialHttpMockRecorder andReturn(String data) {
		return andReturn(new HttpData(data));
	}

}
