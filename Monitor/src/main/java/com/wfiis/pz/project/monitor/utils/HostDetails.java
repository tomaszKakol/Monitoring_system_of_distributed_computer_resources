package com.wfiis.pz.project.monitor.utils;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wfiis.pz.project.monitor.entity.Host;
import com.wfiis.pz.project.monitor.entity.Metric;


/**
 * 
 * @author Mateusz Papież
 * 
 * Class of body object in host adding
 *
 */
public class HostDetails {
	@JsonProperty("host-id")
	String hostId;
	String os;
	List<MetricView> metrics;
	
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
	public List<MetricView> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<MetricView> metrics) {
		this.metrics = metrics;
	}
	
	public Host extractHost() {
		Host result = new Host();
		result.setHostId(this.hostId);
		result.setOs(this.os);
		result.setMonitorId("${MONITORID}");
		
		return result;
	}
	
	public List<Metric> extractMetricList(){
		List<Metric> result = new ArrayList<Metric>();
		
		for (MetricView mv : this.metrics) {
			Metric m = new Metric();
			m.setHostId(this.hostId);
			m.setMetricId(mv.getMetricId());
			m.setType(mv.getType());
			m.setUnit(mv.getUnit());
			//m.setUserId(userId);
			m.setMonitorId("${MONITORID}");
			
			result.add(m);
		}
		
		return result;
	}
	
	
}
