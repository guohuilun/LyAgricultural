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

public class AlarmInfo
{
	private String id; //消息ID
	private String nid;//设备ID
	private int cid;	//通道号
	private int type;	//报警类型


	private boolean isCheck;
	private String title;
	private String time;
	private boolean isExpand;
	private boolean isRead;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
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
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public boolean isCheck()
	{
		return isCheck;
	}
	public void setCheck(boolean isCheck)
	{
		this.isCheck = isCheck;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public boolean isExpand()
	{
		return isExpand;
	}
	public void setExpand(boolean isExpand)
	{
		this.isExpand = isExpand;
	}
	public boolean isRead()
	{
		return isRead;
	}
	public void setRead(boolean isRead)
	{
		this.isRead = isRead;
	}
}
