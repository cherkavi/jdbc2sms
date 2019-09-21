package com.cherkashyn.vitalii.smscenter.knowledgebase;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.cherkashyn.vitalii.smscenter.domain.Message;
import com.cherkashyn.vitalii.smscenter.exception.KnowledgeBaseException;

public class RepositorySql implements Repository {
	@Autowired
	private DataSource	dataSource;
	private String		sqlInsert;
	private String		sqlUpdateAsSended;
	private String		sqlUpdateAsDelivered;
	private String		sqlDelete;

	/**
	 * 
	 * @param sqlInsertParam
	 *            <code>
	 *            - insert into sms_center(text, recipient) values (?, ?)
	 *            </code>
	 * @param sqlUpdateSendTimeParam
	 *            <code>
	 *            - update sms_center set sendtime=?, sendresult=? where id=?
	 *            </code>
	 * @param sqlUpdateDeliveryTimeParam
	 *            <code>
	 *            - update sms_center set deliverytime=?, deliveryresult=? where
	 *            id=?
	 *            </code>
	 * @param sqlDeleteParam
	 *            <code>
	 *            - delete sms_center where id=?
	 *            </code>
	 */
	public RepositorySql(String sqlInsertParam, String sqlUpdateSendTimeParam,
			String sqlUpdateDeliveryTimeParam, String sqlDeleteParam) {
		this.sqlInsert = sqlInsertParam;
		this.sqlUpdateAsSended= sqlUpdateSendTimeParam;
		this.sqlUpdateAsDelivered = sqlUpdateDeliveryTimeParam;
		this.sqlDelete = sqlDeleteParam;
	}

	@Override
	public Message create(final Message newMessage) throws KnowledgeBaseException {
		// Statement.RETURN_GENERATED_KEYS);
		KeyHolder keyHolder=new GeneratedKeyHolder();
		
		new NamedParameterJdbcTemplate(this.dataSource)
		.update(sqlInsert, new BeanPropertySqlParameterSource(newMessage), keyHolder);
				
		newMessage.setId(keyHolder.getKey().intValue());
		return newMessage;
		
	}

	@Override
	public void delete(Message message) throws KnowledgeBaseException {
		new NamedParameterJdbcTemplate(dataSource).update(sqlDelete, new BeanPropertySqlParameterSource(message));
	}

	@Override
	public void updateAsSent(Message message) {
		new NamedParameterJdbcTemplate(this.dataSource)
		.update(sqlUpdateAsSended, new BeanPropertySqlParameterSource(message));
	}

	@Override
	public void updateAsDelivered(Message message) {
		new NamedParameterJdbcTemplate(this.dataSource)
		.update(sqlUpdateAsDelivered, new BeanPropertySqlParameterSource(message));
	}

}
