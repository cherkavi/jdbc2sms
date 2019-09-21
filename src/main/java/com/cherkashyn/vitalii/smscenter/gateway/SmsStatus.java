package com.cherkashyn.vitalii.smscenter.gateway;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

@Root(name="sms", strict=false)
public class SmsStatus {
	public static class DateParser implements Converter<Date>{
		// 02.02.2015 22:37:00
		private final static SimpleDateFormat SDF=new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		@Override
		public Date read(InputNode node) throws Exception {
			try{
				return SDF.parse(node.getValue());
			}catch(ParseException pe){
				return null;
			}
			
		}

		@Override
		public void write(OutputNode node, Date date) throws Exception {
			node.setValue(SDF.format(date));
		}
		
	}
	
	@Element(name="status")
	private Integer status;
	
	@Element(name="status_name")
	private String statusName;
	
//	@Element(name="last_date")
//	@Convert(SmsStatus.DateParser.class)
//	private Date lastDate;

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}


