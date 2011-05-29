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

import net.javacrumbs.mocksocket.connection.HttpData;
import net.javacrumbs.mocksocket.http.matchers.ContentMatcher;
import net.javacrumbs.mocksocket.http.matchers.HeaderMatcher;
import net.javacrumbs.mocksocket.http.matchers.MethodMatcher;
import net.javacrumbs.mocksocket.http.matchers.StatusMatcher;

import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;

public class HttpMatchers {
	
	private static String encoding = "UTF-8";
	
	private HttpMatchers()
	{
	}
	
	public static CombinableMatcher<HttpData> status(Matcher<Integer> statusMatcher) {
		return new CombinableMatcher<HttpData>(new StatusMatcher(statusMatcher, encoding));
	}

	public static CombinableMatcher<HttpData> header(String header, Matcher<String> headerMatcher) {
		return new CombinableMatcher<HttpData>(new HeaderMatcher(header, headerMatcher, encoding));
	}
	
	public static CombinableMatcher<HttpData> content(Matcher<String> contentMatcher) {
		return new CombinableMatcher<HttpData>(new ContentMatcher(contentMatcher, encoding));
	}

	public static CombinableMatcher<HttpData> method(Matcher<String> methodMatcher) {
		return new CombinableMatcher<HttpData>(new MethodMatcher(methodMatcher, encoding));
	}

	public static synchronized String getEncoding() {
		return encoding;
	}

	public static synchronized void setEncoding(String encoding) {
		HttpMatchers.encoding = encoding;
	}

}
