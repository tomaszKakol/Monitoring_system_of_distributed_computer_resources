package com.wfiis.pz.project.monitor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Mateusz Papież
 *
 */
public class Measurement {
	@JsonProperty("metric-id")
	String metricId;
	String val;
	String ts;
	
	@JsonProperty("metric-id")
	public String getMetricId() {
		return metricId;
	}
	@JsonProperty("metric-id")
	public void setMetricId(String metricId) {
		this.metricId = metricId;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	
}
