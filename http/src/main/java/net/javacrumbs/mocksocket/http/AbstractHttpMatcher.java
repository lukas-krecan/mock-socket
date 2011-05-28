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
package net.javacrumbs.mocksocket.http;

import java.io.IOException;

import org.eclipse.jetty.testing.HttpTester;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public abstract class AbstractHttpMatcher extends BaseMatcher<String> {

	private final String encoding;
	
	private final Matcher<?> wrappedMatcher;

	public AbstractHttpMatcher(Matcher<?> wrappedMatcher, String encoding) {
		this.wrappedMatcher = wrappedMatcher;
		this.encoding = encoding;
	}

	public boolean matches(Object item) {
		if (item instanceof String) {
			return doMatch(getHttpTester((String) item));
		}
		return false;
	}
	
	protected boolean doMatch(HttpTester tester) throws AssertionError {
		return wrappedMatcher.matches(getValue(tester));
	}

	
	protected Object describeValue(HttpTester httpTester) {
		return getValue(httpTester);
	}
	
	protected abstract Object getValue(HttpTester httpTester);

	public void describeMismatch(Object item, Description description) {
    	description.appendText("was ").appendValue(describeValue(getHttpTester((String)item)));
    }


	protected HttpTester getHttpTester(String rawHttp) {
		try {
			HttpTester tester = new HttpTester(encoding);
			tester.parse(rawHttp);
			return tester;
		} catch (IOException e) {
			throw new AssertionError("Can not parse response " + rawHttp);
		}
	}

	protected Matcher<?> getWrappedMatcher() {
		return wrappedMatcher;
	}


}