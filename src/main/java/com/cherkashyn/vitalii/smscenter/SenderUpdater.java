package com.cherkashyn.vitalii.smscenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.cherkashyn.vitalii.smscenter.domain.Message;
import com.cherkashyn.vitalii.smscenter.exception.TransportException;
import com.cherkashyn.vitalii.smscenter.gateway.SmsCenter;
import com.cherkashyn.vitalii.smscenter.knowledgebase.DatabaseKnowledgeBase;
import com.cherkashyn.vitalii.smscenter.utility.Timer;

/**
 * enter point to application for sending SMS messages from JDBC table
 */
public class SenderUpdater {

	private final static String	SPRING_CONTEXT_PATH	= "applicationContext.xml";

	final static Logger			LOGGER				= Logger.getLogger(SenderUpdater.class);

	/** Enter point for application */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = null;
		if (args.length > 0) {
			context = new FileSystemXmlApplicationContext(args[0]);
		} else {
			context = new ClassPathXmlApplicationContext(SPRING_CONTEXT_PATH);
		}

		context.getBean(SenderUpdater.class).executeMainLogic();
	}

	
	/**
	 * JMX switcher
	 */
	private boolean					continueToExecute		= true;

	/**
	 * delay before next cycle
	 */
	@Value("${timeout.main_cycle}")
	private int				delay					= 5;


	/**
	 * delay in seconds before checking next delivery
	 */
	@Value("${timeout.for_checking}")
	private int						delayBeforeCheckNextDelivery;

	/**
	 * delay in seconds before next sending
	 */
	@Value("${timeout.for_sending}")
	private int						delayBeforeNextSending;

	private SmsCenter				smsCenter;
	private DatabaseKnowledgeBase	knowledgeBase;
	@Value("${check_delivery_status}")
	private boolean					needToCheckDeliveryStatus;

	/**
	 * 
	 * @param knowledgeBase
	 * @param smsCenter
	 */
	public SenderUpdater(DatabaseKnowledgeBase knowledgeBase,
			SmsCenter smsCenter, boolean needToCheckDeliveryStatus) {
		this.smsCenter = smsCenter;
		this.knowledgeBase = knowledgeBase;
	}

	private void executeMainLogic() {
		LOGGER.info(" - start - ");
		Timer timer = new Timer(delay);

		while (continueToExecute) {
			timer.start();

			LOGGER.info(" - get data for sending" );
			Collection<Message> sendList = knowledgeBase
					.getForSending();
			sendList = sendPackage(smsCenter, sendList);
			LOGGER.info(" - data were sent, size: " + sendList.size());
			knowledgeBase.updateAsSent(sendList);

			if (needToCheckDeliveryStatus) {
				LOGGER.info(" - get data for updating");
				Collection<Message> deliveryList = knowledgeBase
						.getForCheckingDelivery();
				deliveryList = updateDelivery(smsCenter, deliveryList);
				LOGGER.info(" - get data for updating, size: "
						+ sendList.size());
				knowledgeBase.updateAsDelivered(deliveryList);
			}

			LOGGER.info(" - wait - ");
			timer.waitingForFinished();
		}
		LOGGER.info(" - end - ");
	}

	/**
	 * 
	 * @param smsCenter
	 * @param deliveryList
	 * @return list with updated elements <br />
	 *         ()
	 */
	private Collection<Message> updateDelivery(SmsCenter smsCenter,
			Collection<Message> deliveryList) {
		Timer timer = new Timer(this.delayBeforeCheckNextDelivery);

		List<Message> returnValue = new ArrayList<Message>(deliveryList.size());
		for (Message eachMessage : deliveryList) {
			try {
				returnValue.add(smsCenter.checkForDelivery(eachMessage));
			} catch (TransportException e) {
				LOGGER.warn("can't check status of message: "+eachMessage, e);
				eachMessage.setAsDelivered(new Date(), false);
				returnValue.add(eachMessage);
			}

			timer.start();
			timer.waitingForFinished();
		}
		return returnValue;
	}

	/**
	 * send package ( step by step ) via {@link SmsCenter} with some delay
	 * 
	 * @param smsCenter
	 * @param sendList
	 * @return
	 */
	private Collection<Message> sendPackage(SmsCenter smsCenter,
			Collection<Message> sendList) {
		Timer timer = new Timer(this.delayBeforeNextSending);

		List<Message> returnValue = new ArrayList<Message>(sendList.size());
		for (Message eachMessage : sendList) {
			try {
				returnValue.add(smsCenter.send(eachMessage));
			} catch (TransportException e) {
				LOGGER.warn("can't send message: "+eachMessage, e);
				eachMessage.setAsSended(new Date(), false);
				returnValue.add(eachMessage);
			}

			timer.start();
			timer.waitingForFinished();
		}
		return returnValue;
	}

}
