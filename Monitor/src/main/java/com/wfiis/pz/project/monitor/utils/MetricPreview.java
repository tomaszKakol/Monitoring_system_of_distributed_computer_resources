package com.wfiis.pz.project.monitor.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wfiis.pz.project.monitor.entity.Metric;


/**
 * 
 * @author Mateusz Papież
 *
 */
public class MetricPreview {
	@JsonProperty("metric-id")
	String metricId;
	@JsonProperty("monitor-id")
	String monitorId;
	
	@JsonProperty("metric-id")
	public String getMetricId() {
		return metricId;
	}
	public void setMetricId(String metricId) {
		this.metricId = metricId;
	}
	@JsonProperty("monitor-id")
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	
	public MetricPreview(Metric m) {
		this.metricId = m.getMetricId();
		this.monitorId = m.getMonitorId();
	}
	
}
