package com.cherkashyn.vitalii.smscenter.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.cherkashyn.vitalii.smscenter.domain.Message;
import com.cherkashyn.vitalii.smscenter.exception.TransportException;

public class SmsCenter {

	private static Logger LOGGER=Logger.getLogger(SmsCenter.class);
	
	Map<String, String> defaultParameters=new HashMap<String, String>();
	private String sender;
	
	private Serializer serializer=new Persister();
	
	public SmsCenter(String gateWayLogin, String gateWayPassword, String senderName){
		defaultParameters.put("login", gateWayLogin);
		defaultParameters.put("psw", gateWayPassword);
		defaultParameters.put("fmt", "2");
		
		this.sender=senderName;
	}
	
	/**
	 * 
	 * @param message
	 *            - message for send
	 * @return same message with updated flags <br />
	 *         <ul>
	 *         <li> {@link Message#getSendTime()} - execute time</li>
	 *         <li> {@link Message#isSended()} - sending result</li>
	 *         </ul>
	 * @throws TransportException 
	 */
	public Message send(Message message) throws TransportException {
		Map<String, String> parameters=new HashMap<String, String>();
		parameters.putAll(defaultParameters);

		// default for send process 
		//    send immediately
		parameters.put("time", "0");
		parameters.put("charset", ENCODING);
		parameters.put("sender", this.sender);
		
		// unique for each message 
		parameters.put("phones", message.getRecipient());
		parameters.put("mes", message.getText());
		parameters.put("id", Integer.toString(message.getId()));
		
		// send package 
		String resultText=sendPackage(URL_SEND, parameters);
		
		Result result=null;
		try {
			result=serializer.read(Result.class, resultText);
		} catch (Exception e) {
			LOGGER.error("can't read response: "+resultText);
		}
		if(result.getErrorCode()!=null && result.getErrorCode()>0){
			message.setAsSended(new Date(), false);
			message.setSendErrorCode(result.getErrorCode());
		}else{
			message.setAsSended(new Date(), true);
		}
		
		return message;
	}

	/**
	 * @param eachMessage
	 *            - message for checking it for delivery status
	 * @return same message with updated fields: <br />
	 *         <ul>
	 *         <li> {@link Message#getDeliverTime()} - time of executing (time of
	 *         local program )</li>
	 *         <li> {@link Message#isDelivered()} - delivery result</li>
	 *         </ul>
	 * @throws TransportException 
	 */
	public Message checkForDelivery(Message message) throws TransportException {
		Map<String, String> parameters=new HashMap<String, String>();
		parameters.putAll(defaultParameters);

		parameters.put("charset", ENCODING);
		parameters.put("all", "1");
		
		parameters.put("phone", message.getRecipient());
		parameters.put("id", Integer.toString(message.getId()));
		
		String resultText=sendPackage(URL_STATUS, parameters);
		SmsStatus result=null;
		try {
			result=serializer.read(SmsStatus.class, resultText);
		} catch (Exception e) {
			LOGGER.error("can't read response: "+resultText);
		}
		
		// when status is null or message not found 
		if(result.getStatus()==null || result.getStatus().intValue()==(-3)){
			LOGGER.info("was not delivered: "+message.getId());
			message.setAsDelivered(new Date(), false);
			return message;
		}
		
		if(result.getStatus()<=0){ // 0, -1
			LOGGER.debug("need to wait for message: "+message.getId());
			return message;
		}
		if(result.getStatus()==1){
			message.setAsDelivered(new Date(), true);
			message.setDeliveryStatus(null);
			return message;
		}
		message.setAsDelivered(new Date(), false);
		message.setDeliveryStatus(result.getStatus());
		
		return message;
	}

	static String URL_SEND = "http://smsc.ru/sys/send.php?";
	static String URL_STATUS = "http://smsc.ru/sys/status.php?";
	static String ENCODING = "utf-8";
	private static String REQUEST_PARAM_EQUALS = "=";
	private static String REQUEST_PARAM_DELIMITER = "&";

	private String sendPackage(String preambula, Map<String, String> parameters)
			throws TransportException {
		String urlRequest = createHttpRequestString(preambula, parameters);
		return sendGetRequest(urlRequest);
	}

	protected String sendGetRequest(String url) throws TransportException {
		LOGGER.info(">>> request : "+url);		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		try {
			LOGGER.debug("execute request");
			HttpResponse httpResponse;
			try {
				httpResponse = client.execute(request);
			} catch (IOException e) {
				throw new TransportException("can't execute URL " + url, e);
			}
			
			LOGGER.debug("check response for POSITIVE status");
			if (HttpStatus.SC_OK != httpResponse.getStatusLine()
					.getStatusCode()) {
				throw new TransportException("Bad request: " + url);
			}

			LOGGER.debug("read data from socket");
			StringBuilder returnValue = new StringBuilder();
			BufferedReader reader=null;
			HttpEntity httpEntity=httpResponse.getEntity();
			if(httpEntity==null){
				throw new TransportException("response is empty: " + url);
			}
			try {
				reader = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					returnValue.append(line);
				}
			} catch (IOException e) {
				throw new TransportException("can't read response from url: "+ url);
			}

			String response=returnValue.toString();
			LOGGER.info("<<< response : "+response);		
			return response;
		} finally {
			if (request != null) {
				request.releaseConnection();
			}
		}
	}

	/**
	 * create URL for get request
	 * 
	 * @param parameters
	 * @return
	 * @throws TransportException
	 */
	String createHttpRequestString(String preambula, Map<String, String> parameters)
			throws TransportException {
		StringBuilder requestString = new StringBuilder();
		for (Map.Entry<String, String> eachEntry : parameters.entrySet()) {
			if (requestString.length() > 0) {
				requestString.append(REQUEST_PARAM_DELIMITER);
			}
			requestString.append(eachEntry.getKey());
			requestString.append(REQUEST_PARAM_EQUALS);
			try {
				requestString.append(URLEncoder.encode(eachEntry.getValue(),
						ENCODING));
			} catch (UnsupportedEncodingException e) {
				throw new TransportException("can't create message: "
						+ parameters, e);
			}
		}
		return preambula + requestString.toString();
	}

}
