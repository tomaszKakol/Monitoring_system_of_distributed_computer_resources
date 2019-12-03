package com.wfiis.pz.project.monitor.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wfiis.pz.project.monitor.entity.Host;

/**
 * 
 * @author Mateusz Papież
 *
 */
public class HostView extends HostAbstractView {
	@JsonProperty("host-id")
	String hostId;
	@JsonProperty("monitor-id")
	String monitorId;
	
	@Override
	@JsonProperty("host-id")
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	@JsonProperty("monitor-id")
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	
	public HostView() {}
	public HostView(Host h) {
		this.hostId = h.getHostId();
		this.monitorId = h.getMonitorId();
	}
}
