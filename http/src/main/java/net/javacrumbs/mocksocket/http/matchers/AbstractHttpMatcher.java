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
package net.javacrumbs.mocksocket.http.matchers;

import net.javacrumbs.mocksocket.connection.data.SocketData;
import net.javacrumbs.mocksocket.http.HttpParser;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public abstract class AbstractHttpMatcher extends BaseMatcher<SocketData> {

	
	private final Matcher<?> wrappedMatcher;

	public AbstractHttpMatcher(Matcher<?> wrappedMatcher) {
		this.wrappedMatcher = wrappedMatcher;
	}

	public boolean matches(Object item) {
		if (item instanceof SocketData) {
			return doMatch(createHttpParser((SocketData)item));
		}
		return false;
	}
	
	protected boolean doMatch(HttpParser tester) throws AssertionError {
		return wrappedMatcher.matches(getValue(tester));
	}

	
	protected Object describeValue(HttpParser httpTester) {
		return getValue(httpTester);
	}
	
	protected abstract Object getValue(HttpParser httpParser);

	public void describeMismatch(Object item, Description description) {
    	description.appendText("was ").appendValue(describeValue(createHttpParser((SocketData)item)));
    }


	protected HttpParser createHttpParser(SocketData http) {
		HttpParser parser = new HttpParser(http);
		return parser;

	}

	protected Matcher<?> getWrappedMatcher() {
		return wrappedMatcher;
	}


}