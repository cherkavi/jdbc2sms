package com.cherkashyn.vitalii.smscenter.knowledgebase;

import com.cherkashyn.vitalii.smscenter.domain.Message;
import com.cherkashyn.vitalii.smscenter.exception.KnowledgeBaseException;

public interface Repository {

	/**
	 * create one certain message
	 * @param newMessage
	 * @return
	 * @throws KnowledgeBaseException
	 */
	public Message create(Message newMessage) throws KnowledgeBaseException;
	
	/**
	 * remove message by id
	 * @param message
	 * @throws KnowledgeBaseException
	 */
	public void delete(Message message) throws KnowledgeBaseException;

	/**
	 * update single message, mark as Sent 
	 * @param message
	 */
	public void updateAsSent(Message message);

	
	/**
	 * update single message, mark as delivered  
	 * @param message
	 */
	public void updateAsDelivered(Message message);

}
