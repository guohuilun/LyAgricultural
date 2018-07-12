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

public class VideoInfo
{
	private long fileId;
	private String filename;
	private int filesize;
	private int recordType;
	private String startTime;
	private String endTime;
	public long getFileId()
	{
		return fileId;
	}
	public void setFileId(long fileId)
	{
		this.fileId = fileId;
	}
	public String getFilename()
	{
		return filename;
	}
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	public int getFilesize()
	{
		return filesize;
	}
	public void setFilesize(int filesize)
	{
		this.filesize = filesize;
	}
	public int getRecordType()
	{
		return recordType;
	}
	public void setRecordType(int recordType)
	{
		this.recordType = recordType;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
}
