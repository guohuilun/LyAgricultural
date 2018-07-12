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


import com.lyagricultural.yuanjian.model.CameraInfo;
import com.tongguan.yuanjian.family.Utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IPCListUtils
{
	private static ArrayList<CameraInfo> lci = new ArrayList<CameraInfo>();;
	private static TS ts;

	public static ArrayList<CameraInfo> getCameraList()
	{
		if(lci == null)
		{
			lci = new ArrayList<CameraInfo>();
		}
		return lci;
	}

	public static CameraInfo getCameraInfoById(long id, int cid)
	{
		int position = getCameraPositionById(id,cid);
		if(position != -1)
		{
			return lci.get(position);
		}
		else
		{
			return null;
		}
	}

	public static int getCameraPositionById(long id, int cid)
	{
		for(int i = 0; i < lci.size(); i++)
		{
			if(id == lci.get(i).getId() && cid == lci.get(i).getCid())
			{
				return i;
			}
		}
		return -1;
	}

	public static void parseLoginJson(JSONObject json) {
		lci = new ArrayList<CameraInfo>();
		try {
			JSONArray ipcList = json.optJSONArray("ipc");
			if(ipcList != null && ipcList.length() > 0) {
				for(int i = 0; i < ipcList.length(); i++) {
					JSONObject ipc = ipcList.getJSONObject(i);
					CameraInfo ci = new CameraInfo();
					ci.setId(Long.parseLong(ipc.getString("nid")));
					ci.setCid(ipc.optInt("cid",1));
					ci.setName(ipc.getString("ipcname"));
					ci.setOpen(ipc.getInt("devicestatus") == 0);
					ci.setOnline(ipc.getInt("online") == 1);
					ci.setSupportCloud(ipc.getInt("ptz") == 1);
					ci.setCloudStorageOpen(ipc.getInt("cloudstate") == 1);
					//String strFileName = folder + ProtocolConstant.OBLIQUE + ci.getId() + ci.getCid() + ProtocolConstant.IMAGE_BMP_SUFFIX;
					//ci.setThumbnailPath(strFileName);

					lci.add(ci);
				}
			}

			JSONObject tsObject;
			if(json.has("ts"))
			{
				tsObject = json.getJSONArray("ts").getJSONObject(0);
				ts = new TS();
				ts.tcp = tsObject.optInt("tcp");
				ts.tsp = tsObject.optInt("tsp");
				ts.trp = tsObject.optInt("trp");
				ts.tip = tsObject.optString("tip");
			}

			LogUtil.i("parse login json lci size " + lci.size());
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	public static void setIPCList(ArrayList<CameraInfo> aci)
	{
		lci = aci;
	}

	public static void destroy()
	{
		lci.clear();
		ts = null;
	}
}

class TS
{
	int tsp;
	int trp;
	int tcp;
	String tip;
}
