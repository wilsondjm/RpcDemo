package me.vincent.core;

import java.io.Serializable;

public class RPCResponse implements Serializable{

	private static final long serialVersionUID = 1363045917658105958L;
	
	public enum status_enum {
			success,
			failwithErrors
	};
	private String ackID;
	private status_enum status;
	private String message;
	private Object data;
	
	public status_enum getStatus() {
		return status;
	}
	public void setStatus(status_enum status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getAckID() {
		return ackID;
	}
	public void setAckID(String ackID) {
		this.ackID = ackID;
	}
	
	

}
