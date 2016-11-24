package com.roshka.raf.route;

import java.net.URL;

import com.impetus.annovention.Discoverer;
import com.impetus.annovention.Filter;

public class RouteDiscoverer extends Discoverer {
	
	private URL theUrl;

	public RouteDiscoverer(URL theUrl) {
		this.theUrl = theUrl;
	}

	@Override
	public URL[] findResources() {
		return new URL[] { theUrl };
	}

	@Override
	public Filter getFilter() {
		return null;
	}

}
