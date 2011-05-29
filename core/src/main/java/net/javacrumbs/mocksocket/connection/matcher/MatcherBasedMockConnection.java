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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.javacrumbs.mocksocket.connection.AbstractMockConnection;
import net.javacrumbs.mocksocket.connection.MockConnection;

import org.hamcrest.Matcher;

/**
 * Mock connection that is based on {@link Matcher}s.
 * @author Lukas Krecan
 *
 */
public class MatcherBasedMockConnection extends AbstractMockConnection implements MockConnection, MatcherBasedMockResultRecorder, MatcherBasedMockRecorder {

	private final List<MatcherWithData> matchers = new ArrayList<MatcherWithData>();
	
	private final String address;
		
	public MatcherBasedMockConnection(String address) {
		this.address = address;
	}

	public InputStream getInputStream() throws IOException {
		return new RedirectingInputStream(getOutputStream());
	}

	public MatcherBasedMockRecorder thenReturn(byte[] data) {
		matchers.get(matchers.size()-1).addData(data);
		return this;
	}


	public MatcherBasedMockResultRecorder andWhenPayload(Matcher<? extends Object> matcher) {
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
		private final ByteArrayOutputStream outputStream;
		private InputStream wrappedInputStream;
		
		public RedirectingInputStream(ByteArrayOutputStream outputStream)
		{
			this.outputStream = outputStream;
		}

		@Override
		public int read() throws IOException {
			if (wrappedInputStream==null)
			{
				wrappedInputStream = findInputStream();
			}
			return wrappedInputStream.read();
		}

		private InputStream findInputStream() throws IOException, AssertionError {
			byte[] request = outputStream.toByteArray();
			for (MatcherWithData matcher : matchers) {
				if (matcher.getMatcher().matches(request))
				{
					return matcher.getResponse();
				}
			}
			throw new AssertionError("No matcher matches request "+Arrays.toString(request)+" for address \""+address+"\". Do not know which response to return.");
		}
	}
	
	class MatcherWithData
	{
		private final Matcher<? extends Object> matcher;
		private final List<byte[]> responseData = new ArrayList<byte[]>();
		private int actualResponse = 0;		
		
		public MatcherWithData(Matcher<? extends Object> matcher) {
			this.matcher = matcher;
		}
		
		public InputStream getResponse() {
			if (responseData.size()>actualResponse)
			{
				return new ByteArrayInputStream(responseData.get(actualResponse++));
			}
			else
			{
				throw new AssertionError("No more connections expected for \""+address+"\" and request matching matcher: "+matcher+".");
			}
		}

		public void addData(byte[] data)
		{
			responseData.add(data);
		}
		
		public Matcher<? extends Object> getMatcher() {
			return matcher;
		}
		
		
	}

}
