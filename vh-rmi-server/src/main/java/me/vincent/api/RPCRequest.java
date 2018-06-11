package me.vincent.api;

import java.io.Serializable;

public class RPCRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7023871920004681161L;
	
	private String className;
	private String methodName;
	private Object[] args;
	
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	

}
