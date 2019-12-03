package com.wfiis.pz.project.monitor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Mateusz Papież
 *
 */
public class Host {

	@JsonProperty("host-id")
	String hostId;
	String os;
	@JsonProperty("monitor-id")
	String monitorId;
	
	@JsonProperty("host-id")
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	@JsonProperty("monitor-id")
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	
}

