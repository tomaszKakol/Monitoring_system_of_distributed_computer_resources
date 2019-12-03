package com.wfiis.pz.project.monitor.utils;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 
 * @author Mateusz Papież
 *
 */
public class MetricView {
	String type;
	String unit;
	@JsonProperty("metric-id")
	String metricId;
	
	
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
	@JsonProperty("metric-id")
	public String getMetricId() {
		return metricId;
	}
	public void setMetricId(String metricId) {
		this.metricId = metricId;
	}
}
