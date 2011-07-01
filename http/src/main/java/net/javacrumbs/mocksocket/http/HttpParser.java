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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import net.javacrumbs.mocksocket.connection.data.SocketData;
import net.javacrumbs.mocksocket.http.connection.HttpOutputData;
import net.javacrumbs.mocksocket.util.Utils;

import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.io.View;
import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.util.ByteArrayOutputStream2;

public class HttpParser implements HttpRequest  {
	private final HttpTester httpTester;
	
	private final SocketData socketData;
	
	public HttpParser(SocketData data) {
		httpTester = createTester(data.getData());
		this.socketData = data;
	}

	private HttpTester createTester(InputStream data) {
		ExtendedHttpTester httpTester = new ExtendedHttpTester();	
		try {
			httpTester.parse(data);
			return httpTester;
		} catch (IOException e) {
			throw new IllegalArgumentException("Can not parse data",e);
		}
	}

	public String getMethod() {
		return httpTester.getMethod();
	}

	public String getReason() {
		return httpTester.getReason();
	}

	public int getStatus() {
		return httpTester.getStatus();
	}

	public String getURI() {
		return httpTester.getURI();
	}

	public String getVersion() {
		return httpTester.getVersion();
	}

	public String getContentType() {
		return httpTester.getContentType();
	}

	public String getCharacterEncoding() {
		return httpTester.getCharacterEncoding();
	}

	public long getDateHeader(String name) {
		return httpTester.getDateHeader(name);
	}

	@SuppressWarnings("unchecked")
	public List<String> getHeaderNames() {
		return Collections.list(httpTester.getHeaderNames());
	}

	public long getLongHeader(String name) throws NumberFormatException {
		return httpTester.getLongHeader(name);
	}

	public String getHeader(String name) {
		return httpTester.getHeader(name);
	}

	@SuppressWarnings("unchecked")
	public List<String> getHeaderValues(String name) {
		return  Collections.list(httpTester.getHeaderValues(name));
	}

	public String getContent() {
		return httpTester.getContent();
	}
	
	public InputStream getData() {
		return socketData.getData();
	}
	
	/**
	 * Returns address to which the request was sent to or null.
	 */
	public String getAddress() {
		if (socketData instanceof HttpOutputData)
		{
			return ((HttpOutputData) socketData).getAddress();
		}
		else
		{
			return null;
		}
	}
	/**
	 * HttpTester that can parse byte[].
	 * @author Lukas Krecan
	 *
	 */
	private static class ExtendedHttpTester extends HttpTester
	{
		private String _charset;
		

		public String parse(InputStream data) throws IOException {
	        Buffer buf = new ByteArrayBuffer(Utils.toByteArray(data));
	        View view = new View(buf);
	        org.eclipse.jetty.http.HttpParser parser = new org.eclipse.jetty.http.HttpParser(view,new PH());
	        parser.parse();
	        return getString(view.asArray());
		}
		
		private String getString(Buffer buffer)
	    {
	        return getString(buffer.asArray());
	    }
	    
	    private String getString(byte[] b)
	    {
	        if(_charset==null)
	            return new String(b);
	        try
	        {
	            return new String(b, _charset);
	        }
	        catch(Exception e)
	        {
	            return new String(b);
	        }
	     }
		@Override
		public String getContentType() {
			return getString(_fields.get(HttpHeaders.CONTENT_TYPE_BUFFER));
		}
		
	    public String getContent()
	    {
	        if (_parsedContent!=null)
	            return getString(_parsedContent.toByteArray());
	        if (_genContent!=null)
	            return getString(_genContent);
	        return null;
	    }
		
	    private class PH extends org.eclipse.jetty.http.HttpParser.EventHandler
	    {
	        public void startRequest(Buffer method, Buffer url, Buffer version) throws IOException
	        {
	            reset();
	            _method=getString(method);
	            _uri=getString(url);
	            _version=getString(version);
	        }

	        public void startResponse(Buffer version, int status, Buffer reason) throws IOException
	        {
	            reset();
	            _version=getString(version);
	            _status=status;
	            _reason=getString(reason);
	        }
	        
	        public void parsedHeader(Buffer name, Buffer value) throws IOException
	        {
	            _fields.add(name,value);
	        }

	        public void headerComplete() throws IOException
	        {
	            Buffer contentType = _fields.get(HttpHeaders.CONTENT_TYPE_BUFFER);
	            if(contentType!=null)
	            {
	                String charset = MimeTypes.getCharsetFromContentType(contentType);
	                if(charset!=null)
	                {
	                    _charset = charset;
	                }
	            }
	        }

	        public void messageComplete(long contextLength) throws IOException
	        {
	        }
	        
	        public void content(Buffer ref) throws IOException
	        {
	            if (_parsedContent==null)
	                _parsedContent=new ByteArrayOutputStream2();
	            _parsedContent.write(ref.asArray());
	        }
	    }
		
	}

}
