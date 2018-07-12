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

public class SnapshotTask
{
	private int taskId;
	private String name;
	private long nodeId;
	private int cid;
	private String repeat;
	private String time;
	public int getTaskId()
	{
		return taskId;
	}
	public void setTaskId(int taskId)
	{
		this.taskId = taskId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public long getNodeId()
	{
		return nodeId;
	}
	public void setNodeId(long nodeId)
	{
		this.nodeId = nodeId;
	}
	public int getCid()
	{
		return cid;
	}
	public void setCid(int cid)
	{
		this.cid = cid;
	}
	public String getRepeat()
	{
		return repeat;
	}
	public void setRepeat(String repeat)
	{
		this.repeat = repeat;
	}
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
}
