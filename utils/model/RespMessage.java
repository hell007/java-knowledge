package com.jie.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName: RespMessage
 * @Description: 消息响应对象. <br/>
 * @author jie
 * @date 2017年8月12日
 */
public class RespMessage implements Serializable{
    
    private static final long serialVersionUID = 1L;
    /** 响应状态*/
    private boolean status;
    /** 响应编号 */
    private String respCode;
    /** 响应消息 */
    private String respMsg;
    /** 响应数据 */
    private Map<String,Object> respArgs;
    /** 响应数据 */
    private Object respObj;
    
    
    public RespMessage() {
        super();
    }
	
	public RespMessage(boolean status, String respCode, String respMsg, Map<String, Object> respArgs) {
		this.status = status;
		this.respCode = respCode;
		this.respMsg = respMsg;
		this.respArgs = respArgs;
	}
	
	public RespMessage(boolean status, String respCode, String respMsg, Object respObj) {
		this.status = status;
		this.respCode = respCode;
		this.respMsg = respMsg;
		this.respObj = respObj;
	}
	
	public static RespMessage buildMap(boolean status, String respCode, String respMsg, Map<String, Object> respArgs) {
		return new RespMessage(status, respCode, respMsg, respArgs);
	}
	
	public static RespMessage buildObj(boolean status, String respCode, String respMsg, Object respObj) {
		return new RespMessage(status, respCode, respMsg, respObj);
	}

	public static RespMessage buildMap(boolean status, String respMsg, Map<String, Object> respArgs) {
		return buildMap(status, "200", respMsg, respArgs);
	}
	
	public static RespMessage buildObj(boolean status, String respMsg, Object respObj) {
		return buildObj(status, "200", respMsg, respObj);
	}
	
	public static RespMessage build(boolean status, String respMsg) {
		return buildObj(status, respMsg, null);
	}


	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
    

    public Map<String, Object> getRespArgs() {
        return respArgs;
    }

    public void setRespArgs(Map<String, Object> respArgs) {
        this.respArgs = respArgs;
    }

	public Object getRespObj() {
		return respObj;
	}

	public void setRespObj(Object respObj) {
		this.respObj = respObj;
	}

	@Override
	public String toString() {
		return "RespMessage [status=" + status + ", respCode=" + respCode
				+ ", respMsg=" + respMsg + ", respArgs=" + respArgs
				+ ", respObj=" + respObj + "]";
	}

	
 
}
