package com.wfiis.pz.project.monitor.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wfiis.pz.project.monitor.dao.MetricDao;
import com.wfiis.pz.project.monitor.entity.Metric;

/**
 * 
 * @author Mateusz Papież
 *
 */
@Component
public class MetricServiceImpl implements MetricService{
	@Resource 
	MetricDao metricDao;
	
	
	@Override
	public List<Metric> findAll() {
		return metricDao.findAll();
	}
	
	@Override
	public void insertMetric(Metric m) {
		metricDao.insertMetric(m);
	}

	@Override
	public List<Metric> findAllByHostId(String id) {
		return metricDao.findAllByHostId(id);
	}

	@Override
	public Metric findMetricById(String id) {
		return metricDao.findById(id);
		
	}

	@Override
	public void deleteMetricById(String id) {
		metricDao.deleteMetric(id);
	}

	@Override
	public List<Metric> findAllByNameLike(String name_like) {
		return metricDao.findAllByNameLike(name_like);
	}

	@Override
	public List<Metric> findAllByType(String type) {
		return metricDao.findAllByType(type);
	}
	
}