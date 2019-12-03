package com.wfiis.pz.project.monitor.dao;

import java.util.List;

import com.wfiis.pz.project.monitor.entity.Host;


/**
 * 
 * @author Mateusz Papież
 * 
 * Interface of the available operations in database in context of hosts.
 *
 */
public interface HostDao {

	/**
	 * Finding all hosts.
	 * @return list of hosts
	 */
	List<Host> findAll();
	
	/**
	 * Stub of the function findAll().
	 * @return list of tests hosts
	 */
	List<Host> findAllDemo();

	/**
	 * Inserting host
	 * @param host host to insert
	 */
	void insertHost(Host host);
	
	/**
	 * Finding specific host
	 * @param id host's name
	 * @return host
	 */
	Host findHostById(String id);
	
	/**
	 * Returning list of hosts with the same names
	 * @param name name of hosts
	 * @return list of hosts
	 */
	List<Host> findHostByName(String name);
	
	/**
	 * Returning list of hosts with name like name_like
	 * 
	 * @param name_like part of the hosts' names
	 * @return list of hosts
	 */
	List<Host> findHostByNameLike(String name_like);
	
	/**
	 * Finding top n hosts with specific metrics' types
	 * @param top number of returned hosts
	 * @param metric_type part of metric type
	 * @return list of hosts
	 */
	List<Host> findTopByMetricType(Integer top, String metric_type);
	
	
	/**
	 * Deleting host and all its data by his name
	 * @param hostId name of the host
	 */
	void deleteHostById(String hostId);

	
}