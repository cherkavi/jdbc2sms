package com.cherkashyn.vitalii.smscenter.utility;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

public class TimerTest {

	@Test
	public void givenTimerExecuteAfter2seconds(){
		
		// given
		int executeTime=2;
		Timer timer=new Timer(executeTime);
		
		
		// when
		Date startDate=new Date();
		timer.start();
		timer.waitingForFinished();
		
		// then
		Assert.assertTrue(timer.isFinished());
		
		Date finishDate=new Date();
		Assert.assertTrue(finishDate.getTime()-startDate.getTime()>=executeTime*1000);
	}
	
	
	@Test
	public void givenTimerExecuteEvery2seconds(){
		
		// given
		int executeTime=2;
		Timer timer=new Timer(executeTime);
		
		
		for(int counter=0;counter<5;counter++){
			// when
			Date startDate=new Date();
			timer.start();
			
			
			if(new Random().nextBoolean()){
				try {
					TimeUnit.SECONDS.sleep(executeTime+1);
				} catch (InterruptedException e) {
				}
			}
			
			timer.waitingForFinished();
			
			// then
			Assert.assertTrue(timer.isFinished());
			
			Date finishDate=new Date();
			Assert.assertTrue(finishDate.getTime()-startDate.getTime()>=executeTime*1000);
		}
		
	}
	
	
	@Test
	public void givenTimerWithoutStartExecuteEvery2seconds(){
		Date start=new Date();
		
		// given
		int executeTime=2;
		Timer timer=new Timer(executeTime);
		
		// when
		// timer.start();
		timer.waitingForFinished();
		
		// then
		Assert.assertTrue(timer.isFinished());
		
		Date finishDate=new Date();
		Assert.assertFalse(finishDate.after(DateUtils.addSeconds(start, executeTime)));
	}

	
}
