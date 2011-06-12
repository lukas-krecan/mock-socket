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
package net.javacrumbs.mocksocket.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import net.javacrumbs.mocksocket.connection.matcher.MatcherBasedMockConnection;
import net.javacrumbs.mocksocket.connection.matcher.MatcherBasedMockResultRecorder;
import net.javacrumbs.mocksocket.connection.sequential.SequentialMockConnection;
import net.javacrumbs.mocksocket.connection.sequential.SequentialMockRecorder;

import org.hamcrest.Matcher;

/**
 * Mock connection that creates and wraps either {@link SequentialMockConnection} or {@link MatcherBasedMockConnection}.
 * @author Lukas Krecan
 *
 */
public class UniversalMockConnection implements UniversalMockRecorder, MockConnection {
	private AbstractMockConnection wrappedConnection;

	public SequentialMockRecorder andReturn(SocketData data) {
		SequentialMockConnection connection = createSequentialConnection();
		wrappedConnection = connection;
		connection.andReturn(data);
		return connection;
	}

	protected SequentialMockConnection createSequentialConnection() {
		return new SequentialMockConnection();
	}

	public MatcherBasedMockResultRecorder andWhenPayload(Matcher<SocketData> matcher) {
		MatcherBasedMockConnection connection = createMatcherBasedConnection();
		connection.andWhenPayload(matcher);
		wrappedConnection = connection;
		return connection;
	}

	protected MatcherBasedMockConnection createMatcherBasedConnection() {
		return new MatcherBasedMockConnection();
	}

	public void onCreate() {
		wrappedConnection.onCreate();
	}

	public InputStream getInputStream() throws IOException {
		return wrappedConnection.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return wrappedConnection.getOutputStream();
	}

	public List<SocketData> requestData() {
		return wrappedConnection.requestData();
	}
}
