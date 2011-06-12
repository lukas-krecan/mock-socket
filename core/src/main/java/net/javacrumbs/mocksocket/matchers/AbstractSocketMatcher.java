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
package net.javacrumbs.mocksocket.matchers;

import net.javacrumbs.mocksocket.connection.data.RequestSocketData;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;

public abstract class AbstractSocketMatcher extends BaseMatcher<RequestSocketData> {
	
	private final Matcher<?> wrappedMatcher;

	public AbstractSocketMatcher(Matcher<?> wrappedMatcher) {
		this.wrappedMatcher = wrappedMatcher;
	}

	public boolean matches(Object item) {
		if (item instanceof RequestSocketData) {
			return doMatch((RequestSocketData)item);
		}
		return false;
	}

	protected abstract boolean doMatch(RequestSocketData item);

	protected Matcher<?> getWrappedMatcher() {
		return wrappedMatcher;
	}


}