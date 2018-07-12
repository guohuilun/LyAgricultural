/*   
 * Copyright (c) 2013-2020 Founder Ltd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */

package com.lyagricultural.yuanjian.adapter;

import org.json.JSONObject;

public class OrganizeInfo
{
	private int id;
	private String name;
	private int type;
	private JSONObject pro;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public JSONObject getPro()
	{
		return pro;
	}
	public void setPro(JSONObject pro)
	{
		this.pro = pro;
	}
}
