package com.wfiis.pz.project.monitor.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wfiis.pz.project.monitor.dao.HostDao;
import com.wfiis.pz.project.monitor.entity.Host;

/**
 * 
 * @author Mateusz Papież
 *
 */
@Component
public class HostServiceImpl implements HostService{
	@Resource 
	HostDao hostDao;
	
	@Override
	public List<Host> findAll() {
		return hostDao.findAll();
	}

	@Override
	public List<Host> findAllDemo() {
		return hostDao.findAllDemo();
	}

	@Override
	public void insertHost(Host host) {
		hostDao.insertHost(host);
		
	}

	@Override
	public Host findHostById(String id) {
		return hostDao.findHostById(id);
	}

	@Override
	public List<Host> findAllByName(String name) {
		return hostDao.findHostByName(name);
	}

	@Override
	public List<Host> findAllByNameLike(String name_like) {
		return hostDao.findHostByNameLike(name_like);
	}

	@Override
	public List<Host> findTopByMetricType(Integer top, String metric_type) {
		return hostDao.findTopByMetricType(top, metric_type);
	}

	@Override
	public void deleteHostById(String hostId) {
		hostDao.deleteHostById(hostId);
		
	}
	
}