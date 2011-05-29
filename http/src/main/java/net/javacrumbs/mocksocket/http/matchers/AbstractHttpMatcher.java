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

import net.javacrumbs.mocksocket.connection.HttpData;
import net.javacrumbs.mocksocket.http.HttpProcessor;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public abstract class AbstractHttpMatcher extends BaseMatcher<HttpData> {

	private final String encoding;
	
	private final Matcher<?> wrappedMatcher;

	public AbstractHttpMatcher(Matcher<?> wrappedMatcher, String encoding) {
		this.wrappedMatcher = wrappedMatcher;
		this.encoding = encoding;
	}

	public boolean matches(Object item) {
		if (item instanceof HttpData) {
			return doMatch(createHttpProcessor((HttpData)item));
		}
		return false;
	}
	
	protected boolean doMatch(HttpProcessor tester) throws AssertionError {
		return wrappedMatcher.matches(getValue(tester));
	}

	
	protected Object describeValue(HttpProcessor httpTester) {
		return getValue(httpTester);
	}
	
	protected abstract Object getValue(HttpProcessor httpTester);

	public void describeMismatch(Object item, Description description) {
    	description.appendText("was ").appendValue(describeValue(createHttpProcessor((HttpData)item)));
    }


	protected HttpProcessor createHttpProcessor(HttpData http) {
		HttpProcessor processor = new HttpProcessor(encoding);
		processor.parse(http);
		return processor;

	}

	protected Matcher<?> getWrappedMatcher() {
		return wrappedMatcher;
	}


}