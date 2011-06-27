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
package net.javacrumbs.mocksocket.connection.matcher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.javacrumbs.mocksocket.MockSocketException;
import net.javacrumbs.mocksocket.connection.AbstractMockConnection;
import net.javacrumbs.mocksocket.connection.MockConnection;
import net.javacrumbs.mocksocket.connection.data.RequestSocketData;
import net.javacrumbs.mocksocket.connection.data.SocketData;

import org.hamcrest.Matcher;

/**
 * Mock connection that is based on {@link Matcher}s.
 * @author Lukas Krecan
 *
 */
public class MatcherBasedMockConnection extends AbstractMockConnection implements MockConnection, MatcherBasedMockResultRecorder, MatcherBasedMockRecorder {

	private final List<MatcherWithData> matchers = new ArrayList<MatcherWithData>();
	
	public InputStream createInputStream() throws IOException {
		return new RedirectingInputStream(getRequestSocketData());
	}

	public MatcherBasedMockRecorder thenReturn(SocketData data) {
		matchers.get(matchers.size()-1).addData(data);
		return this;
	}


	public MatcherBasedMockResultRecorder andWhenRequest(Matcher<RequestSocketData> matcher) {
		matchers.add(new MatcherWithData(matcher));
		return this;
	}
	
	/**
	 * Input stream that redirects to actual InputStread based on OutputStreamData. Has to be done this way since InputStram could be created before
	 * OutputStream data are written.
	 * @author Lukas Krecan
	 *
	 */
	class RedirectingInputStream extends InputStream
	{
		private final RequestSocketData requestSocketData;
		private InputStream wrappedInputStream;
		
		public RedirectingInputStream(RequestSocketData requestSocketData)
		{
			this.requestSocketData = requestSocketData;
		}

		@Override
		public int read() throws IOException {
			if (wrappedInputStream==null)
			{
				wrappedInputStream = findInputStream();
			}
			return wrappedInputStream.read();
		}

		private InputStream findInputStream() throws IOException, MockSocketException {
			for (MatcherWithData matcher : matchers) {
				if (matcher.getMatcher().matches(requestSocketData))
				{
					return matcher.getResponse();
				}
			}
			throw new MockSocketException("No matcher matches request "+requestSocketData+". Do not know which response to return.");
		}
	}
	
	protected class MatcherWithData
	{
		private final Matcher<RequestSocketData> matcher;
		private final List<SocketData> responseData = new ArrayList<SocketData>();
		private int actualResponse = 0;		
		
		public MatcherWithData(Matcher<RequestSocketData> matcher) {
			this.matcher = matcher;
		}
		
		public InputStream getResponse() {
			if (responseData.size()>actualResponse)
			{
				return new ByteArrayInputStream(responseData.get(actualResponse++).getBytes());
			}
			else
			{
				throw new MockSocketException("No more connections expected for request matching matcher: "+matcher+". Requests recorded so far are: "+requestData());
			}
		}

		public void addData(SocketData data)
		{
			responseData.add(data);
		}
		
		public Matcher<RequestSocketData> getMatcher() {
			return matcher;
		}
		
		
	}

}
