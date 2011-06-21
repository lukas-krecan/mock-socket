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

package net.javacrumbs.mocksocket.http;

import java.util.List;

import net.javacrumbs.mocksocket.connection.data.SocketData;

/**
 * HTTP request wrapper.
 * @author Lukas Krecan
 *
 */
public interface HttpRequest extends SocketData{

	public abstract String getMethod();

	public abstract String getAddress();

	public abstract String getURI();

	public abstract String getVersion();

	public abstract String getContentType();

	public abstract String getCharacterEncoding();

	public abstract long getDateHeader(String name);

	public abstract List<String> getHeaderNames();

	public abstract long getLongHeader(String name) throws NumberFormatException;

	public abstract String getHeader(String name);

	public abstract List<String> getHeaderValues(String name);

	public abstract String getContent();
}