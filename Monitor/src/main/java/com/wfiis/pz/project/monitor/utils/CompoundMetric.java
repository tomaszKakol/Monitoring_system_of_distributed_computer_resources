package com.wfiis.pz.project.monitor.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Mateusz Papież
 * 
 * Compound metric with the set of fields that are required in post procedure on metrics dataset
 *
 */
public class CompoundMetric {
	String kind;
	@JsonProperty("metric-ids")
	String metricIds;
	@JsonProperty("monitor-id")
	String monitorId;
	String type;
	
	@JsonProperty("monitor-id")
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	@JsonProperty("metric-ids")
	public String getMetricIds() {
		return metricIds;
	}
	public void setMetricIds(String metricIds) {
		this.metricIds = metricIds;
	}
	
	
	
}
