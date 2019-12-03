package com.wfiis.pz.project.monitor.service;

import java.util.List;

import com.wfiis.pz.project.monitor.entity.Host;

/**
 * 
 * @author Mateusz Papież
 *
 */
public interface HostService {

	List<Host> findAll();
	List<Host> findAllDemo();
	void insertHost(Host host);
	Host findHostById(String id);
	List<Host> findAllByName(String name);
	List<Host> findAllByNameLike(String name_like);
	List<Host> findTopByMetricType(Integer top, String metric_type);
	void deleteHostById(String hostId);
	
}



