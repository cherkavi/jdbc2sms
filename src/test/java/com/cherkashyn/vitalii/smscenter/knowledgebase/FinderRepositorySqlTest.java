package com.cherkashyn.vitalii.smscenter.knowledgebase;

import java.util.Collection;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cherkashyn.vitalii.smscenter.domain.Message;
import com.cherkashyn.vitalii.smscenter.exception.KnowledgeBaseException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class FinderRepositorySqlTest {

	@Autowired
	Finder	finder;
	
	@Autowired
	Repository repository;

	private Message testMessage;
	
	@Before
	public void insertTestData() throws KnowledgeBaseException{
		// repository.removeAll(); - !!! test schema should be empty !!!
		this.testMessage=repository.create(new Message("+380954671400","test message"));
	}
	
	@After
	public void afterTestComplete() throws KnowledgeBaseException{
		repository.delete(testMessage);
	}
	
	
	@Test
	public void emptyReadCheckConfiguration() throws KnowledgeBaseException {
		// given 
		repository.delete(testMessage);
		// when 
		Collection<Message> values = finder.findNextForChecking();
		// then 
		Assert.assertNotNull(values);
	}
	
	@Test
	public void readRowForCheckingConfiguration(){
		// given
		Collection<Message> listForSending = finder.findNextForSending();
		Message message=listForSending.iterator().next();
		message.setAsSended(new Date(), true);
		repository.updateAsSent(message);
		
		// when 
		Collection<Message> listForChecking = finder.findNextForChecking();
		Assert.assertNotNull(listForChecking);
		Assert.assertTrue(listForChecking.size()>0);

		
		// given
		message=listForChecking.iterator().next();
		message.setAsDelivered(new Date(), true);
		repository.updateAsDelivered(message);
		
		// when 
		listForSending = finder.findNextForSending();
		Assert.assertNotNull(listForSending);
		Assert.assertTrue(listForSending.size()==0);
		
		listForChecking = finder.findNextForChecking();
		Assert.assertNotNull(listForChecking);
		Assert.assertTrue(listForChecking.size()==0);
	}
	
	@Test
	public void emptyReadForSending() throws KnowledgeBaseException {
		// given
		repository.delete(testMessage);
		
		// when 
		Collection<Message> values = finder.findNextForSending();
		
		// then 
		Assert.assertNotNull(values);
	}

	@Test
	public void readRowForSending() {
		// when
		Collection<Message> values = finder.findNextForSending();
		
		// then 
		Assert.assertNotNull(values);
		Assert.assertTrue(values.size()>0);
	}
	
}
