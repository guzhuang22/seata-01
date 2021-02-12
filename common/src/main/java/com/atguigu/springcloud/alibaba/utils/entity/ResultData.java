package com.atguigu.springcloud.alibaba.utils.entity;

/**
 * 
 * @author XHT
 *
 */
public class ResultData {
	private Integer code;
	private String msg;
	private Object data;

	public ResultData(ResultCode resultCode) {
		this.code = resultCode.getCode();
		this.msg = resultCode.getMsg();
	}

	public ResultData(ResultCode resultCode, Object... data) {
		this.code = resultCode.getCode();
		this.msg = resultCode.getMsg();
		if (data != null && data.length == 1) {
			this.data = data[0];
		} else {
			this.data = data;
		}
	}

	public Integer getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}

	public Object getData() {
		return data;
	}

}
