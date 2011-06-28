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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import net.javacrumbs.mocksocket.connection.data.SocketData;

import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.io.SimpleBuffers;
import org.eclipse.jetty.io.bio.StreamEndPoint;

/**
 * Genrates HTTP response.
 * @author Lukas Krecan
 *
 */
public class HttpResponseGenerator implements SocketData{
	private static final String DEFAULT_ENCODING = "UTF-8";

	private final HttpFields httpFields = new HttpFields();
	private int status = 200;
	private String content = "";
	
	
	public HttpResponseGenerator withStatus(int status) {
		this.status = status;
		return this;
	}
	
	public HttpResponseGenerator withContent(String content) {
		this.content = content;
		return this;
	}
	
	public HttpResponseGenerator withHeader(String name, String value) {
		httpFields.add(name, value);
		return this;
	}

	public HttpResponseGenerator withDateHeader(String name, Date value) {
		httpFields.addDateField(name, value.getTime());	
		return this;
	}

	public byte[] generate() {
		try {
		    String charset = getCharset();
		    byte[] byteContent = content.getBytes(charset);
			Buffer bb=new ByteArrayBuffer(32*1024 + byteContent.length);
	        Buffer sb=new ByteArrayBuffer(4*1024);
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
			StreamEndPoint endpoint = new StreamEndPoint(new ByteArrayInputStream(new byte[0]), out);
			org.eclipse.jetty.http.HttpGenerator generator = new org.eclipse.jetty.http.HttpGenerator(new SimpleBuffers(sb,bb),endpoint);
			generator.setResponse(status, HttpStatus.getMessage(status));
			generator.addContent(new ByteArrayBuffer(byteContent), true);
			generator.completeHeader(httpFields, true);
			generator.complete();
			generator.flushBuffer();
			return out.toByteArray();
		} catch (IOException e) {
			throw new HttpGeneratorException(e);
		}
	}
	
	public InputStream getData() {
		return new ByteArrayInputStream(generate());
	}

	private String getCharset() {
		Buffer contentType = httpFields.get(HttpHeaders.CONTENT_TYPE_BUFFER);
		if(contentType!=null)
		{
		    String charset = MimeTypes.getCharsetFromContentType(contentType);
		    if(charset!=null)
		    {
		    	return charset; 
		    }
		}
		return DEFAULT_ENCODING;
	}
}
