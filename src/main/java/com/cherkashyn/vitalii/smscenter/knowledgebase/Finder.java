package com.cherkashyn.vitalii.smscenter.knowledgebase;

import java.util.Collection;

import com.cherkashyn.vitalii.smscenter.domain.Message;

public interface Finder {

	/**
	 * get portion of data for sending
	 * @return
	 */
	public Collection<Message> findNextForSending();

	/**
	 * get portion of data for checking of delivering status
	 * @return
	 */
	public Collection<Message> findNextForChecking();

}
