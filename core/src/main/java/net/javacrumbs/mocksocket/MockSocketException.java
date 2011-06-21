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

package net.javacrumbs.mocksocket;

/**
 * Exception thrown when mock-socket fails.
 * @author Lukas Krecan
 *
 */
public class MockSocketException extends RuntimeException {
	private static final long serialVersionUID = -6476984055059688027L;

	public MockSocketException(String message, Throwable cause) {
		super(message, cause);
	}

	public MockSocketException(String message) {
		super(message);
	}

	public MockSocketException(Throwable cause) {
		super(cause);
	}


}
