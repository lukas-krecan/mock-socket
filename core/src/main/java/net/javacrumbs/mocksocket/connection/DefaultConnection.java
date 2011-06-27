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

package net.javacrumbs.mocksocket.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Default connection implementation.
 * @author Lukas Krecan
 *
 */
public class DefaultConnection implements Connection {

	private final InputStream inputStream;
	
	private final OutputStream outputStream;
	
	public DefaultConnection(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	public InputStream getInputStream() throws IOException {
		return inputStream;
	}

	public OutputStream getOutputStream() throws IOException {
		return outputStream;
	}
}
