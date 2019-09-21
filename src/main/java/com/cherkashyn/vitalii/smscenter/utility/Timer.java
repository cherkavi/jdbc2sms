package com.cherkashyn.vitalii.smscenter.utility;

import java.util.concurrent.TimeUnit;

public class Timer {
	
	private final int delay;
	private volatile Boolean finish=true;
	private TimerRunner runner=null;
	
	public Timer(int delaySeconds){
		this.delay=delaySeconds;
	}
	
	
	public void start(){
		if(!isFinished()){
			return; 
		}
		this.runner=new TimerRunner(this.delay, this.finish);
		this.runner.start();
	}
	
	
	public boolean isFinished(){
		return this.finish;
	}

	public void waitingForFinished(){
		if(this.runner!=null){
			this.runner.waitingForFinished();
		}
	}

	
	private class TimerRunner extends Thread{
		private final int timeout;
		private Boolean finishFlag;
		
		public TimerRunner(int countdown, Boolean flag){
			this.timeout=countdown;
			this.finishFlag=flag;
		}
		
		@Override
		public void run() {
			synchronized (finishFlag) {
				this.finishFlag=false;
			}
			
			try {
				TimeUnit.SECONDS.sleep(timeout);
			} catch (InterruptedException e) {
			}

			synchronized (finishFlag) {
				this.finishFlag=true;
			}
		}
		
		public void waitingForFinished(){
			try {
				this.join();
			} catch (InterruptedException e) {
			}
		}
	}
	
	
}


