package com.cherkashyn.vitalii.smscenter.knowledgebase;

import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cherkashyn.vitalii.smscenter.domain.Message;

public class FinderSql implements Finder {
	
	@Autowired(required=true)
	private DataSource	dataSource;

	private String		sqlSend;
	private String		sqlChecking;

	public FinderSql(String sqlFindForSending, String sqlFindForChecking) {
		this.sqlSend = sqlFindForSending;
		this.sqlChecking = sqlFindForChecking;
	}

	@Override
	public Collection<Message> findNextForSending() {
		return executeSqlQuery(this.sqlSend);
	}

	@Override
	public Collection<Message> findNextForChecking() {
		return executeSqlQuery(this.sqlChecking);
	}

	private Collection<Message> executeSqlQuery(String sqlText) {
		return new JdbcTemplate(this.dataSource).query(sqlText, new BeanPropertyRowMapper<Message>(Message.class));
	}

}

