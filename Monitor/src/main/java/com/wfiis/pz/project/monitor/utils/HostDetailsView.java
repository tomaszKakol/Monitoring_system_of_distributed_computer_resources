package com.wfiis.pz.project.monitor.utils;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wfiis.pz.project.monitor.entity.Metric;


/**
 * 
 * @author Mateusz Papież
 *
 */
public class HostDetailsView extends HostAbstractView {
	@JsonProperty("host-id")
	String hostId;
	String os;
	List<Metric> metrics;
	
	@Override
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
	public List<Metric> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}
	
	
}
