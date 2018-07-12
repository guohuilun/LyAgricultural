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

public class CameraInfo
{
	private String sortLetters;
	private String secondLetters;
	private String pinying;
	private long nid;
	private String name;
	private int cid;
	private boolean isOnline;
	private boolean isOpen;
	private boolean isCloudStorageOpen;
	private boolean isSupportCloud;
	private String thumbnailPath;
	public CameraInfo(){
		thumbnailPath="default";
	}
	public long getId()
	{
		return nid;
	}
	public void setId(long id)
	{
		this.nid = id;
	}
	public int getCid()
	{
		return cid;
	}
	public void setCid(int cid)
	{
		this.cid = cid;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean isOnline()
	{
		return isOnline;
	}
	public void setOnline(boolean isOnline)
	{
		this.isOnline = isOnline;
	}
	public boolean isOpen()
	{
		return isOpen;
	}
	public void setOpen(boolean isOpen)
	{
		this.isOpen = isOpen;
	}
	public boolean isSupportCloud()
	{
		return isSupportCloud;
	}
	public void setSupportCloud(boolean isSupportCloud)
	{
		this.isSupportCloud = isSupportCloud;
	}
	public boolean isCloudStorageOpen()
	{
		return isCloudStorageOpen;
	}
	public void setCloudStorageOpen(boolean isCloudStorageOpen)
	{
		this.isCloudStorageOpen = isCloudStorageOpen;
	}
	public String getThumbnailPath()
	{
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath)
	{
		this.thumbnailPath = thumbnailPath;
	}

	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getSecondLetters() {
		return secondLetters;
	}

	public void setSecondLetters(String secondLetters) {
		this.secondLetters = secondLetters;
	}

	public String getPinying() {
		return pinying;
	}

	public void setPinying(String pinying) {
		this.pinying = pinying;
	}
}
