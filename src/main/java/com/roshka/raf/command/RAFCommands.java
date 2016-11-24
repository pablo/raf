package com.roshka.raf.command;

import java.util.List;

import com.roshka.raf.annotations.RAFMethod;
import com.roshka.raf.route.RouteInfo;
import com.roshka.raf.route.RouteManager;

public class RAFCommands {

	@RAFMethod(value="/raf/version")
	public String getVersion()
	{
		return "0.3.1";
	}
	
	@RAFMethod(value="/raf/status")
	public List<RouteInfo> getStatus()
	{
		return RouteManager.getRoutesInfo();
	}
	
}
