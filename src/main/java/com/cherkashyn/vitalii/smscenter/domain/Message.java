package com.cherkashyn.vitalii.smscenter.domain;

import java.util.Date;

public class Message {
	/**
	 * unique id of the element
	 */
	private int		id;

	/**
	 * text of the message
	 */
	private String	text;

	/**
	 * SMS recipient
	 */
	private String	recipient;

	/**
	 * send time;
	 */
	private Date	sendTime;

	/**
	 * result of the sending
	 * <ul>
	 * <li><b>true</b> - sending successfully</li>
	 * <li><b>false</b> - sending error</li>
	 * </ul>
	 */
	private Boolean	sendResult;

	/**
	 * delivery time
	 */
	private Date	deliveryTime;

	/**
	 * result of the delivering
	 * <ul>
	 * <li><b>true</b> - delivered</li>
	 * <li><b>false</b> - not delivered</li>
	 * </ul>
	 */
	private Boolean	deliveryResult;
	
	
	/**
	 * send status error 
	 */
	
	private Integer sendErrorCode;
	
	/**
	 * status of delivery message:
	 *
	 * <ul>
	 <li>
	-3	Сообщение не найдено	Возникает при множественном запросе статусов, если для указанного номера телефона и ID сообщение не найдено.
	 </li>
	 <li>
	-1	Ожидает отправки	Если при отправке сообщения было задано время получения абонентом, то до этого времени сообщение будет находиться в данном статусе, в других случаях сообщение в этом статусе находится непродолжительное время перед отправкой на SMS-центр.
	 </li>
	 <li>
	0	Передано оператору	Сообщение было передано на SMS-центр оператора для доставки.
	 </li>
	 <li>
	1	Доставлено	Сообщение было успешно доставлено абоненту.
	 </li>
	 <li>
	3	Просрочено	Возникает, если время "жизни" сообщения истекло, а оно так и не было доставлено получателю, например, если абонент не был доступен в течение определенного времени или в его телефоне был переполнен буфер сообщений.
	 </li>
	 <li>
	20	Невозможно доставить	Попытка доставить сообщение закончилась неудачно, это может быть вызвано разными причинами, например, абонент заблокирован, не существует, находится в роуминге без поддержки обмена SMS, или на его телефоне не поддерживается прием SMS-сообщений.
	 </li>
	 <li>
	22	Неверный номер	Неправильный формат номера телефона.
	 </li>
	 <li>
	23	Запрещено	Возникает при срабатывании ограничений на отправку дублей, на частые сообщения на один номер (флуд), на номера из черного списка, на запрещенные спам фильтром тексты или имена отправителей (Sender ID).
	 </li>
	 <li>
	24	Недостаточно средств	На счете Клиента недостаточная сумма для отправки сообщения.
	 </li>
	 <li>
	25	Недоступный номер	Телефонный номер не принимает SMS-сообщения, или на этого оператора нет рабочего маршрута.
	 </li>
	 </ul>
	 */
	private Integer deliveryStatus;

	public Message(){
	}

	public Message(String smsRecipient, String smsText) {
		this.recipient = smsRecipient;
		this.text = smsText;
	}

	public void setAsDelivered(Date deliveryTime) {
		this.setAsDelivered(deliveryTime, true);
	}

	public void setAsDelivered(Date deliveryTime, boolean wasDelivered) {
		this.deliveryTime = deliveryTime;
		this.deliveryResult = wasDelivered;
	}

	public void setAsSended(Date sendTime) {
		this.setAsSended(sendTime, true);
	}

	public void setAsSended(Date sendTime, boolean sendResult) {
		this.sendTime = sendTime;
		this.sendResult = sendResult;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Boolean getSendResult() {
		return sendResult;
	}

	public void setSendResult(Boolean sendResult) {
		this.sendResult = sendResult;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Boolean getDeliveryResult() {
		return deliveryResult;
	}

	public void setDeliveryResult(Boolean deliveryResult) {
		this.deliveryResult = deliveryResult;
	}

	public Integer getSendErrorCode() {
		return sendErrorCode;
	}

	public void setSendErrorCode(Integer sendErrorCode) {
		this.sendErrorCode = sendErrorCode;
	}

	public Integer getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(Integer deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

}
