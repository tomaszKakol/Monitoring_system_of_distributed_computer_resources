package com.wfiis.pz.project.monitor.service;

import java.util.List;

import com.wfiis.pz.project.monitor.entity.Measurement;

/**
 * 
 * @author Mateusz Papież
 *
 */
public interface MeasurementService {

	List<Measurement> findAll();

	List<Measurement> findMeasurementByMetricId(String id);

	void insertMeasurment(Measurement m);

	List<Measurement> findTopMeasurementByMetricId(String id, Integer n, Boolean all);

	List<Measurement> findByDateMeasurementByMetricId(String id, String from, String to, Integer n, Boolean all);
	
	

	List<Measurement> findTopMeasurementBySimpleMetricId(String simpleMetricId, Integer n, Boolean all);

	List<Measurement> findByDateMeasurementBySimpleMetricId(String simpleMetricId, String from, String to, Integer n, Boolean all);

	List<Measurement> findMeasurementBySimpleMetricId(String simpleMetricId);
	
}



