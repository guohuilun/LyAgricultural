/*   
 * Copyright (c) 2013-2020 Founder Ltd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */

package com.lyagricultural.yuanjian.model;


import com.lyagricultural.yuanjian.adapter.OrganizeInfo;

public class CameraNode extends OrganizeInfo
{
	private String nid;
	private int cid;
	private boolean status;
	public String getNid()
	{
		return nid;
	}
	public void setNid(String nid)
	{
		this.nid = nid;
	}
	public int getCid()
	{
		return cid;
	}
	public void setCid(int cid)
	{
		this.cid = cid;
	}
	public boolean isStatus()
	{
		return status;
	}
	public void setStatus(boolean status)
	{
		this.status = status;
	}
}
