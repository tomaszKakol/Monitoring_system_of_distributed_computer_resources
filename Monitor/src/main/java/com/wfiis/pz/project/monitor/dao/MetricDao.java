package com.wfiis.pz.project.monitor.dao;

import java.util.List;

import com.wfiis.pz.project.monitor.entity.Metric;


/**
 * 
 * @author Mateusz Papież
 * 
 * Interface of the available operations in database in context of metrics.
 *
 */
public interface MetricDao {

	
	/**
	 * Returning all metrics from database
	 * @return list of metrics
	 */
	List<Metric> findAll();

	/**
	 * Inserting metric m to database
	 * @param m metric to insert
	 */
	void insertMetric(Metric m);

	/**
	 * Finding all metrics of host
	 * @param id host name
	 * @return list of metrics
	 */
	List<Metric> findAllByHostId(String id);

	/**
	 * Finding specific metric by it's name
	 * @param id name of metric
	 * @return  metric
	 */
	Metric findById(String id);

	/**
	 * Deleting metric
	 * 
	 * @param id metric name
	 */
	void deleteMetric(String id);

	/**
	 * Finding all metrics by part of name
	 * @param name_like part of name
	 * @return list of metrics
	 */
	List<Metric> findAllByNameLike(String name_like);

	/**
	 * Finding all metrics by their type
	 * @param type type of metric
	 * @return list of metrics
	 */
	List<Metric> findAllByType(String type);

	
}