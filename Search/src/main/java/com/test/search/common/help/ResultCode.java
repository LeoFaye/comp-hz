/**
 * Kirs
 */


package com.test.search.common.help;

import java.io.Serializable;


/**
 * ClassName:Result <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Aug 1, 2013 12:39:52 PM <br/>
 * 
 * @author wangdayuan
 * @version
 * @see
 */

public enum ResultCode implements Serializable {

	SUCCESS(0),  FAIL_SYS(1), FAIL_BUS(2), INFO(3);

	private ResultCode(int rv) {
		this.value = rv;
	}

	public String toString() {
		return super.toString() + "(" + value + ")";
	}

	private int value;
	public int getValue() {
		return value;
	}
}