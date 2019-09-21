package com.cherkashyn.vitalii.smscenter.gateway;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.cherkashyn.vitalii.smscenter.exception.TransportException;

public class SmsCenterTest {

	@Test
	public void readDataFromRemoteSocket() throws TransportException{
		// given
		String url="http://www.cs.virginia.edu/~zaher/classes/CS851/Web_lecture/tsld011.htm";
		
		// when 
		String response=new SmsCenter("","","").sendGetRequest(url);
		
		// then 
		Assert.assertNotNull(response);
		Assert.assertTrue(response.length()>0);
		Assert.assertTrue(response.indexOf("<HTML>")>=0);
		Assert.assertTrue(response.indexOf("<HEAD>")>0);
		Assert.assertTrue(response.indexOf("</HEAD>")>0);
		Assert.assertTrue(response.indexOf("</HTML>")>0);
	}
	
	
	@Test(expected=TransportException.class)
	public void cantReadFromSocket() throws TransportException{
		// given
		String url="http://www.cs.virginia.edu/~zaher/classes/CS851/Web_lecture/tsld011.htm_222222";
		
		// when 
		String response=new SmsCenter("","","").sendGetRequest(url);
		
		// then 
		Assert.assertNotNull(response);
	}

	
	@Test
	public void checkSendUrl() throws TransportException{
		// given 
		SmsCenter smsCenter=new SmsCenter("technik", "technik", "myself");
		
		Map<String, String> parameters=new HashMap<String, String>();
		parameters.putAll(smsCenter.defaultParameters);
		parameters.put("phones", "+380954670000");
		parameters.put("mes", "hello:"+new SimpleDateFormat("HH:mm:SS").format(new Date()));
		parameters.put("id", Integer.toString(new Random().nextInt(999999999)));
		
		// when 
		String url=smsCenter.createHttpRequestString(SmsCenter.URL_SEND, parameters);
		
		// then 
		Assert.assertNotNull(url);
		Assert.assertTrue(url.length()>0);
		System.out.println(url);
	}
	
	
	@Test
	public void checkDeliveryStatusUrl() throws TransportException{
		// given 
		SmsCenter smsCenter=new SmsCenter("technik", "technik", "myself");
		
		Map<String, String> parameters=new HashMap<String, String>();
		parameters.putAll(smsCenter.defaultParameters);
		
		parameters.put("charset", SmsCenter.ENCODING);
		parameters.put("all", "1");

		parameters.put("phone", "+380954670000");
		parameters.put("id", Integer.toString(new Random().nextInt(999999999)));
		
		// when 
		String url=smsCenter.createHttpRequestString(SmsCenter.URL_STATUS, parameters);
		
		// then 
		Assert.assertNotNull(url);
		Assert.assertTrue(url.length()>0);
		System.out.println(url);
	}
	
	private Serializer serializer=new Persister();
	
	@Test
	public void parsePositiveResult() throws Exception{
		// given 
		String text="<result><id>634761894</id><cnt>1</cnt></result>";

		// when
		Result result=serializer.read(Result.class, text);
		
		// then 
		Assert.assertNotNull(result);
		Assert.assertNull(result.getError());
		Assert.assertNull(result.getErrorCode());
		Assert.assertEquals((Integer)634761894, result.getId());
	}

	@Test
	public void parseNegativeResult() throws Exception{
		// given 
		String text="<result><error>invalid number</error><error_code>7</error_code><id>113581144</id></result>";

		// when
		Result result=serializer.read(Result.class, text);
		
		// then 
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer)113581144, result.getId());
		Assert.assertNotNull(result.getError());
		Assert.assertNotNull(result.getErrorCode());
		Assert.assertEquals((Integer)7, result.getErrorCode());
	}

	@Test
	public void parseAuthorizeErrorResult() throws Exception{
		// given 
		String text="<?xml version=\"1.0\" encoding=\"UTF-8\"?><result><error>authorise error</error><error_code>2</error_code></result>";

		// when
		Result result=serializer.read(Result.class, text);
		
		// then 
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getError());
		Assert.assertNotNull(result.getErrorCode());
		Assert.assertEquals((Integer)2, result.getErrorCode());
	}
	
	
	
	@Test
	public void parseSmsStatus() throws Exception{
		// given 
		String text="<sms><status>-1</status><last_date>02.02.2015 22:37:00</last_date><last_timestamp>1422909420</last_timestamp><send_date>02.02.2015 22:36:56</send_date><send_timestamp>1422909416</send_timestamp><phone>380954670000</phone><cost>0.18</cost><sender_id>SMSC.UA~myself</sender_id><status_name>Доставлено</status_name><message>hello:22:35:64</message></sms>";

		// when
		SmsStatus result=serializer.read(SmsStatus.class, text);
		
		// then 
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer)(-1), result.getStatus());
		
		// Assert.assertNotNull(result.getLastDate());
		// Assert.assertEquals(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse("02.02.2015 22:37:00"), result.getLastDate());
	}
	
	
}
