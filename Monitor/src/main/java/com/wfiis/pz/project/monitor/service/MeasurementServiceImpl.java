package com.wfiis.pz.project.monitor.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wfiis.pz.project.monitor.dao.MeasurementDao;
import com.wfiis.pz.project.monitor.entity.Measurement;

/**
 * 
 * @author Mateusz Papież
 *
 */
@Component
public class MeasurementServiceImpl implements MeasurementService{
	@Resource 
	MeasurementDao measurementDao;
	@Override
	public List<Measurement> findAll() {
		return measurementDao.findAll();
	}
	@Override
	public List<Measurement> findMeasurementByMetricId(String id) {
		return measurementDao.findAllByMetricId(id);
	}
	@Override
	public void insertMeasurment(Measurement m) {
		
		measurementDao.insertMeasurement(m);
		
	}
	@Override
	public List<Measurement> findTopMeasurementByMetricId(String id, Integer n, Boolean all) {
		return measurementDao.findTopByMetricId(id,n, all);
	}
	@Override
	public List<Measurement> findByDateMeasurementByMetricId(String id, String from, String to, Integer n, Boolean all) {
		return measurementDao.findByDateMeasurementByMetricId(id, from, to,  n, all);
	}
	
	
	
	@Override
	public List<Measurement> findTopMeasurementBySimpleMetricId(String simpleMetricId, Integer n, Boolean all) {
		return measurementDao.findTopMeasurementBySimpleMetricId(simpleMetricId, n, all);
	}
	@Override
	public List<Measurement> findByDateMeasurementBySimpleMetricId(String simpleMetricId, String from, String to, Integer n, Boolean all) {
		return measurementDao.findByDateMeasurementBySimpleMetricId(simpleMetricId, from, to, n, all);
	}
	@Override
	public List<Measurement> findMeasurementBySimpleMetricId(String simpleMetricId) {
		return measurementDao.findMeasurementBySimpleMetricId(simpleMetricId);
	}
	
}