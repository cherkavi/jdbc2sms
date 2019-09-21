package com.cherkashyn.vitalii.smscenter.knowledgebase;

import java.util.Collection;

import com.cherkashyn.vitalii.smscenter.domain.Message;

public class DatabaseKnowledgeBase {
	private FinderSql	finder;
	private Repository	repository;

	public DatabaseKnowledgeBase(FinderSql finder, Repository repository) {
		this.finder = finder;
		this.repository = repository;
	}

	/**
	 * retrieve messages for sending
	 * 
	 * @param limitForSending
	 * @return
	 */
	public Collection<Message> getForSending() {
		return this.finder.findNextForSending();
	}

	/**
	 * retrieve information about sending data
	 * 
	 * @param limitForSending
	 * @return
	 */
	public Collection<Message> getForCheckingDelivery() {
		return this.finder.findNextForChecking();
	}

	/**
	 * update message into storage - set as Delivered
	 * @param sendList
	 */
	public void updateAsDelivered(Message message) {
		this.repository.updateAsDelivered(message); ;
	}

	/**
	 * update message into storage - set as Sent
	 * @param sendList
	 */
	public void updateAsDelivered(Collection<Message> messages) {
		for(Message eachMessage:messages) {
			this.repository.updateAsDelivered(eachMessage);
		}
	}
	
	
	/**
	 * update message into storage - set as Sent
	 * @param sendList
	 */
	public void updateAsSent(Message message) {
		this.repository.updateAsSent(message); ;
	}

	/**
	 * update message into storage - set as Sent
	 * @param sendList
	 */
	public void updateAsSent(Collection<Message> messages) {
		for(Message eachMessage:messages) {
			this.repository.updateAsSent(eachMessage);
		}
	}
	
}
