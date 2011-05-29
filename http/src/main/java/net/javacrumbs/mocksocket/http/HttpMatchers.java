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

import net.javacrumbs.mocksocket.http.matchers.ContentMatcher;
import net.javacrumbs.mocksocket.http.matchers.HeaderMatcher;
import net.javacrumbs.mocksocket.http.matchers.MethodMatcher;
import net.javacrumbs.mocksocket.http.matchers.StatusMatcher;

import org.hamcrest.Matcher;

public class HttpMatchers {
	
	private static String encoding = "UTF-8";
	
	private HttpMatchers()
	{
	}
	
	public static <T> Matcher<T> status(Matcher<Integer> statusMatcher) {
		return new StatusMatcher<T>(statusMatcher, encoding);
	}

	public static <T> Matcher<T> header(String header, Matcher<String> headerMatcher) {
		return new HeaderMatcher<T>(header, headerMatcher, encoding);
	}
	
	public static <T> Matcher<T> content(Matcher<String> contentMatcher) {
		return new ContentMatcher<T>(contentMatcher, encoding);
	}

	public static <T> Matcher<T> method(Matcher<String> methodMatcher) {
		return new MethodMatcher<T>(methodMatcher, encoding);
	}


	public static synchronized String getEncoding() {
		return encoding;
	}

	public static synchronized void setEncoding(String encoding) {
		HttpMatchers.encoding = encoding;
	}

}
