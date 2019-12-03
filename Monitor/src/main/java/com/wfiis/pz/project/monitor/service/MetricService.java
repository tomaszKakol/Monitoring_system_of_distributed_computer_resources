package com.wfiis.pz.project.monitor.service;

import java.util.List;

import com.wfiis.pz.project.monitor.entity.Metric;


/**
 * 
 * @author Mateusz Papież
 *
 */
public interface MetricService {

	List<Metric> findAll();

	void insertMetric(Metric m);

	List<Metric> findAllByHostId(String id);

	Metric findMetricById(String id);

	void deleteMetricById(String id);

	List<Metric> findAllByNameLike(String name_like);

	List<Metric> findAllByType(String type);
	
}



