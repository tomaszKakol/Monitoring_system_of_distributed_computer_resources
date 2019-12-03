package com.wfiis.pz.project.monitor.dao;

import java.util.List;

import com.wfiis.pz.project.monitor.entity.Measurement;

/**
 * 
 * @author Mateusz Papież
 * 
 * Interface of the available operations in database in context of measuremants.
 *
 */
public interface MeasurementDao {

	/**
	 * Finding all measurements
	 * @return list of all measurements in database 
	 */
	List<Measurement> findAll();

	/**
	 * Inserting measurement m.
	 * @param m measurement to insert
	 */
	void insertMeasurement(Measurement m);

	/**
	 * Finding all measurements by their metric name
	 * @param id metric name
	 * @return list of measurements
	 */
	List<Measurement> findAllByMetricId(String id);

	/**
	 * Finding n newest measurements of specific metric
	 * 
	 * @param id metric name
	 * @param n number of returned records
	 * @param all parameter that specifies if it should be returned all measurements
	 * @return list of measurements
	 */
	List<Measurement> findTopByMetricId(String id, Integer n, Boolean all);

	/**
	 * Finding measurements from date interval
	 * @param id name of measurement
	 * @param from oldest date
	 * @param to newest date
	 * @param n number of returned records
	 * @param all specifies if there should be all returned
	 * @return list of measurements
	 */
	List<Measurement> findByDateMeasurementByMetricId(String id, String from, String to, Integer n, Boolean all);
	
	
	
	/**
	 * Getting measurements for compound metric (moving-avg) by its simple metric name 
	 * @param simpleMetricId simple metric's name of specific compound metric 
	 * @return list of measurements
	 */
	List<Measurement> findMeasurementBySimpleMetricId(String simpleMetricId);

	/**
	 * Getting measurements for compound metric (moving-avg) by its simple metric name 
	 * 
	 * 
	 * @param simpleMetricId simple metric's name
	 * @param from oldest date
	 * @param to newest date
	 * @param n number of returned records (limit)
	 * @param all checks if returns all records
	 * @return list of measurements
	 */
	List<Measurement> findByDateMeasurementBySimpleMetricId(String simpleMetricId, String from, String to, Integer n, Boolean all);

	/**
	 * Getting top n measurements for compound metric (moving-avg) by its simple metric name 
	 * @param simpleMetricId simpleMetricId simple metric's name
	 * @param n number of records (limit)
	 * @param all all checks if returns all records
	 * @return list of measurements
	 */
	List<Measurement> findTopMeasurementBySimpleMetricId(String simpleMetricId, Integer n, Boolean all);

	
}