package com.wfiis.pz.project.monitor.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.wfiis.pz.project.monitor.entity.Host;
import com.wfiis.pz.project.monitor.entity.Metric;
import com.wfiis.pz.project.monitor.mapper.HostRowMapper;
import com.wfiis.pz.project.monitor.mapper.MetricRowMapper;

/**
 * 
 * @author Mateusz Papież
 *
 */
@Repository
public class HostDaoImpl implements HostDao{
	
	public HostDaoImpl(NamedParameterJdbcTemplate template) {  
        this.template = template;  
}  
	NamedParameterJdbcTemplate template;  

	@Override
	public List<Host> findAll() {
		return template.query("select * from hosts", new HostRowMapper());
		
	}
	
	public List<Host> findAllDemo() {
		Host h = new Host();
		h.setHostId("tempId");
		h.setMonitorId("v1");
		h.setOs("Windows");
		List<Host> hosts = new ArrayList<Host>();
		hosts.add(h);
		return hosts;
	}
	
	
	
	@Override
	public void insertHost(Host h) {
		 final String sql = "insert into hosts(hostId , os, monitorId) values(:hostId,:os,:monitorId)"; 
		 
	        KeyHolder holder = new GeneratedKeyHolder();
	        SqlParameterSource param = new MapSqlParameterSource()
					.addValue("hostId", h.getHostId())
					.addValue("os", h.getOs())
					.addValue("monitorId", h.getMonitorId());
	        template.update(sql,param, holder);
	 
	}

	@Override
	public Host findHostById(String id) {
		final String sql = "select * from hosts where hostId = :id";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("id", id);
		List<Host> list = template.query(sql, param, new HostRowMapper());
		return list.get(0);
	}

	@Override
	public List<Host> findHostByName(String name) {
		final String sql = "select * from hosts where hostId = :id";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("id", name);
		List<Host> list = template.query(sql, param, new HostRowMapper());
		return list;
	}

	@Override
	public List<Host> findHostByNameLike(String name_like) {
		final String sql = "select * from hosts where hostId like '%'||:id||'%'";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("id", name_like);
		List<Host> list = template.query(sql, param, new HostRowMapper());
		return list;
	}

	@Override
	public List<Host> findTopByMetricType(Integer top, String metric_type) {
		final String sql = "select * from hosts where hostId in (select hostId from metrics where type = :metric_type) limit :top";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("metric_type", metric_type)
				.addValue("top", top);
		List<Host> list = template.query(sql, param, new HostRowMapper());
		return list;
	}

	@Override
	public void deleteHostById(String hostId) {
		final String findAllMetrics = "select * from metrics where hostId = :hostId";
		final String deleteAllMeasurements = "delete from measurements where metricId = :metricId";
		final String deleteAllMetrics = "delete from metrics where hostId = :hostId";
		final String deleteHost = "delete from hosts where hostId = :hostId";
		
		
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("hostId", hostId);
		List<Metric> metrics = template.query(findAllMetrics, param, new MetricRowMapper());
		 
		for (Metric m : metrics){
			Map<String,Object> map=new HashMap<String,Object>();  
			 map.put("metricId", m.getMetricId());
		
			 template.execute(deleteAllMeasurements,map,new PreparedStatementCallback<Object>() {  
				    @Override  
				    public Object doInPreparedStatement(PreparedStatement ps)  
				            throws SQLException, DataAccessException {  
				        return ps.executeUpdate();  
				    }  
				});  
		}
		{
		Map<String,Object> map=new HashMap<String,Object>();  
		map.put("hostId", hostId);
	
		 template.execute(deleteAllMetrics,map,new PreparedStatementCallback<Object>() {  
			    @Override  
			    public Object doInPreparedStatement(PreparedStatement ps)  
			            throws SQLException, DataAccessException {  
			        return ps.executeUpdate();  
			    }  
			});  
		}
		Map<String,Object> map=new HashMap<String,Object>();  
		map.put("hostId", hostId);
			
		 template.execute(deleteHost,map,new PreparedStatementCallback<Object>() {  
			    @Override  
			    public Object doInPreparedStatement(PreparedStatement ps)  
			            throws SQLException, DataAccessException {  
			        return ps.executeUpdate();  
			    }  
			});  
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}