package com.wfiis.pz.project.monitor.utils;

import java.util.List;

import com.wfiis.pz.project.monitor.entity.Metric;

/**
 * 
 * @author Mateusz Papież
 *
 */
public class MetricPresenterReqursive extends MetricAbstractPresenter {
	List<Metric> metrics;
	MetaDataForMetric meta;
	
	
	public List<Metric> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}
	public MetaDataForMetric getMeta() {
		return meta;
	}
	public void setMeta(MetaDataForMetric meta) {
		this.meta = meta;
	}
	
	
}
