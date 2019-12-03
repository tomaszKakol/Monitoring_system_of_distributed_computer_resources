package com.wfiis.pz.project.monitor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 
 * @author Mateusz Papież
 *
 */
public class Metric{
	
	@JsonProperty("metric-id")
	String metricId;
	String type;
	String unit;
	@JsonProperty("host-id")
	String hostId;
	@JsonProperty("user-id")
	String userId;
	@JsonProperty("monitor-id")
	String monitorId;
	
	@JsonProperty("metric-ids")
	String simpleMetricId;
	String kind;
	
	public String getSimpleMetricId() {
		return simpleMetricId;
	}
	public void setSimpleMetricId(String simpleMetricId) {
		this.simpleMetricId = simpleMetricId;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	
	
	@JsonProperty("metric-id")
	public String getMetricId() {
		return metricId;
	}
	public void setMetricId(String metricId) {
		this.metricId = metricId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@JsonProperty("host-id")
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	@JsonProperty("user-id")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("monitor-id")
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	
	
}
