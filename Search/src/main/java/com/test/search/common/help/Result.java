package com.test.search.common.help;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class Result implements Serializable {
	private static final long serialVersionUID = 4568592262545444070L;

	public static Result SUCCESS(){
		return new  Result(ResultCode.SUCCESS);
	}
	public static Result INFO(){
		return new  Result(ResultCode.INFO);
	}
	public static Result FAIL_SYS(){
		return new  Result(ResultCode.FAIL_SYS);
	}
	public static Result FAIL_BUS(){
		return new  Result(ResultCode.FAIL_BUS);
	}
	
	public static Result SUCCESS(String msg){
		return new  Result(ResultCode.SUCCESS,msg);
	}
	public static Result INFO(String msg){
		return new  Result(ResultCode.INFO,msg);
	}
	public static Result FAIL_SYS(String msg){
		return new  Result(ResultCode.FAIL_SYS,msg);
	}
	public static Result FAIL_BUS(String msg){
		return new  Result(ResultCode.FAIL_BUS,msg);
	}
	
	
	/**
	 * 状态代码
	 */
	private ResultCode code;
	
	/**
	 * 返回单条消息
	 */
	private String message;
		
	/**
	 * 额外的附属信息
	 */
	public Map<String,Object> extraObj = new LinkedHashMap<String,Object>();
	
	/**
	 * 总记录条数
	 */
	public Long totalRecords = 0L;
	
	public Result() {
		this.code = ResultCode.SUCCESS;
	}

	public Result(ResultCode code) {
		this.code = code;
	}

	public Result(ResultCode code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResultCode getCode() {
		return code;
	}

	public void setCode(ResultCode code) {
		this.code = code;
	}

	public Map<String, Object> getExtraObj() {
		return extraObj;
	}

	public void setExtraObj(Map<String, Object> extraObj) {
		this.extraObj = extraObj;
	}
	
	public void putExtraObj(String name,  Object value){
		this.getExtraObj().put(name, value);
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Override
	public String toString() {
		String result = "";
		result = this.getMessage();
		return "Result Code:"+this.getCode() + ", Result Message"+ result;
	}
	
	public static void handleResult(boolean result, Model model, RedirectAttributes att) {
		if (result) {
			att.addFlashAttribute("result", new Result(ResultCode.SUCCESS, "操作成功"));
		} else {
			model.addAttribute("result", new Result(ResultCode.FAIL_BUS, "操作失败"));
		}
	}
}
