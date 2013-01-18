/*
 *  Copyright (C) 2009 Jack Park,
 * 	mail : jackpark@gmail.com
 *
 *  Part of IBIS Server, an open source project.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.nex.ibis.client;
import java.util.*;
import java.io.*;
import java.net.*;


import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.ConnectionReuseStrategy;

import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;



//json.jar requires commons.logging.jar, ezmorph.jar, commons.lang.jar
// commons.collections.jar, commons.beanutils.jar, commons.codec.jar
import net.sf.json.JSONSerializer;
import net.sf.json.JSONArray;

import org.nex.util.Base64;
/**
 * <p>Title: IBIS Server Tester</p>
 * <p>Description: Test Webservices with the IBIS Server</p>
 * <p>Copyright: Copyright (c) 2009, Jack Park</p>
 * <p>Company: NexistGroup</p>
 * @author Park
 * @version 1.0
 * <p>For this to work, user MUST list files from the server
 * to set <code>baseURL</code></p>
 */
public class TestHelper {
  private MainFrame host;
  private String serverURL = null;
  private int portNum;


  public TestHelper(MainFrame h) {
    this.host = h;
  }

  /**
   * Initialize server
   * @param server String
   * @param port int
   */
  public void initServer(String server, int port) {
    serverURL = server;
    portNum = port;
  }
  /**
   * Assure server has been initialized
   * @throws Exception
   */
  void assertServer() throws Exception {
    if (serverURL == null) {
      host.tell("Server not initialized");
      throw new Exception("Server not initialized");
    }
  }
  /**
   * Given a server URL, fetch its list of IBIS conversations
   * @return List
   * @throws Exception
   */
  public List<List> listFiles() throws Exception {
    assertServer();
    List<List> result = new ArrayList<List>();
    host.tell("Fetching list from "+serverURL+":"+portNum);
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, "UTF-8");
    HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
    HttpProtocolParams.setUseExpectContinue(params, true);
    BasicHttpProcessor httpproc = new BasicHttpProcessor();
    // Required protocol interceptors
    httpproc.addInterceptor(new RequestContent());
    httpproc.addInterceptor(new RequestTargetHost());
    // Recommended protocol interceptors
    httpproc.addInterceptor(new RequestConnControl());
    httpproc.addInterceptor(new RequestUserAgent());
    httpproc.addInterceptor(new RequestExpectContinue());

    HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

    HttpContext context = new BasicHttpContext(null);
    HttpHost host = new HttpHost(serverURL, portNum);
    String target = "/ws/";
    DefaultHttpClientConnection conn = new DefaultHttpClientConnection();

    context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
    context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
    if (!conn.isOpen()) {
      Socket socket = new Socket(host.getHostName(), host.getPort());
      conn.bind(socket, params);
    }
    BasicHttpRequest request = new BasicHttpRequest("GET", target);
    System.out.println(">> Request URI: " + request.getRequestLine().getUri());

    request.setParams(params);
    httpexecutor.preProcess(request, httpproc, context);
    HttpResponse response = httpexecutor.execute(request, conn, context);
    response.setParams(params);
    httpexecutor.postProcess(response, httpproc, context);
    HttpEntity e = response.getEntity();
      String json = EntityUtils.toString(e);
      System.out.println("JSON "+json);
      JSONArray jo = (JSONArray)JSONSerializer.toJSON(json);
      Collection x = JSONArray.toCollection(jo);
      System.out.println(x);
      result.addAll(x);
    return result;
  }


  /**
   * Post a document and authenticate
   * @param platform String
   * @param fileName String
   * @param description String
   * @param cargo String
   * @param userName String
   * @param password String
   * @throws Exception
   */
  public void postDocument(String platform, String fileName,String description, String cargo,
                            String userName, String password) throws Exception {
    assertServer();
    StringBuilder buf = new StringBuilder("");
    //cleanup description
    String desc = description.replaceAll(" ","%20");
    //encode password
    String pwd = Base64.encodeObject(password);

    buf.append("/ws/put?fileName=");
    buf.append(fileName);
    buf.append("&platform=");
    buf.append(platform);
    buf.append("&description=");
    buf.append(desc);
    buf.append("&username=");
    buf.append(userName);
    buf.append("&password64="); //for base64 passwords
    buf.append(pwd);
    String target = buf.toString();
    host.tell("Uploading to "+target);
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, "UTF-8");
    HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
    HttpProtocolParams.setUseExpectContinue(params, true);
    BasicHttpProcessor httpproc = new BasicHttpProcessor();
// Required protocol interceptors
    httpproc.addInterceptor(new RequestContent());
    httpproc.addInterceptor(new RequestTargetHost());
// Recommended protocol interceptors
    httpproc.addInterceptor(new RequestConnControl());
    httpproc.addInterceptor(new RequestUserAgent());
    httpproc.addInterceptor(new RequestExpectContinue());

    HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

    HttpContext context = new BasicHttpContext(null);
    HttpHost host = new HttpHost(serverURL, portNum);
    DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
    ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

    context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
    context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);

    if (!conn.isOpen()) {
      Socket socket = new Socket(host.getHostName(), host.getPort());
      conn.bind(socket, params);
    }
    HttpEntity requestBody = new StringEntity(cargo, "UTF-8");
    BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", target);
    request.setEntity(requestBody);
    System.out.println(">> Request URI: " + request.getRequestLine().getUri());

    request.setParams(params);
    httpexecutor.preProcess(request, httpproc, context);
    HttpResponse response = httpexecutor.execute(request, conn, context);
    response.setParams(params);
    httpexecutor.postProcess(response, httpproc, context);
    HttpEntity e = response.getEntity();
    System.out.println("POSTED " + EntityUtils.toString(e));
  }

  /**
   * Fetch a selected document
   * @param docType String
   * @param docName String
   * @return String
   * @throws Exception
   */
  public String getDocument(String docType, String docName) throws Exception {
    assertServer();
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, "UTF-8");
    HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
    HttpProtocolParams.setUseExpectContinue(params, true);
    BasicHttpProcessor httpproc = new BasicHttpProcessor();
    // Required protocol interceptors
    httpproc.addInterceptor(new RequestContent());
    httpproc.addInterceptor(new RequestTargetHost());
    // Recommended protocol interceptors
    httpproc.addInterceptor(new RequestConnControl());
    httpproc.addInterceptor(new RequestUserAgent());
    httpproc.addInterceptor(new RequestExpectContinue());

    HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

    HttpContext context = new BasicHttpContext(null);
    HttpHost host = new HttpHost(serverURL, portNum);
    String target = "/ws/get/"+docType+"/"+docName;
    DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
    ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

    context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
    context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
    if (!conn.isOpen()) {
      Socket socket = new Socket(host.getHostName(), host.getPort());
      conn.bind(socket, params);
    }
    BasicHttpRequest request = new BasicHttpRequest("GET", target);
    System.out.println(">> Request URI: " + request.getRequestLine().getUri());

    request.setParams(params);
    httpexecutor.preProcess(request, httpproc, context);
    HttpResponse response = httpexecutor.execute(request, conn, context);
    response.setParams(params);
    httpexecutor.postProcess(response, httpproc, context);
    HttpEntity e = response.getEntity();
    return EntityUtils.toString(e);
  }

  public void removeDocument(String platform, String fileName, String userName, String password) throws Exception {
    assertServer();
    StringBuilder buf = new StringBuilder("");
    //encode password
    String pwd = Base64.encodeObject(password);

    buf.append("/ws/remove?fileName=");
    buf.append(fileName);
    buf.append("&platform=");
    buf.append(platform);
    buf.append("&username=");
    buf.append(userName);
    buf.append("&password64="); //for base64 passwords
    buf.append(pwd);
    String target = buf.toString();
    host.tell("Uploading to "+target);
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, "UTF-8");
    HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
    HttpProtocolParams.setUseExpectContinue(params, true);
    BasicHttpProcessor httpproc = new BasicHttpProcessor();
// Required protocol interceptors
    httpproc.addInterceptor(new RequestContent());
    httpproc.addInterceptor(new RequestTargetHost());
// Recommended protocol interceptors
    httpproc.addInterceptor(new RequestConnControl());
    httpproc.addInterceptor(new RequestUserAgent());
    httpproc.addInterceptor(new RequestExpectContinue());

    HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

    HttpContext context = new BasicHttpContext(null);
    HttpHost host = new HttpHost(serverURL, portNum);
    DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
    ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

    context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
    context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);

    if (!conn.isOpen()) {
      Socket socket = new Socket(host.getHostName(), host.getPort());
      conn.bind(socket, params);
    }
    BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", target);
    System.out.println(">> Request URI: " + request.getRequestLine().getUri());

    request.setParams(params);
    httpexecutor.preProcess(request, httpproc, context);
    HttpResponse response = httpexecutor.execute(request, conn, context);
    response.setParams(params);
    httpexecutor.postProcess(response, httpproc, context);
    HttpEntity e = response.getEntity();
    System.out.println("Removed " + EntityUtils.toString(e));  }
}


