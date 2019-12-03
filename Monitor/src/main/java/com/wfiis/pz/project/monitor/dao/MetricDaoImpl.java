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
public class MetricDaoImpl implements MetricDao{
	
	public MetricDaoImpl(NamedParameterJdbcTemplate template) {  
        this.template = template;  
	}  
	NamedParameterJdbcTemplate template;  

	@Override
	public List<Metric> findAll() {
		return template.query("select * from metrics", new MetricRowMapper());
	}
	
	
	@Override
	public void insertMetric(Metric m) {
		 final String sql = "insert into metrics(metricId, type, unit, hostId, userId, monitorId, kind, simpleMetricIds) "+
				 			"values(:metricId, :type, :unit, :hostId, :userId, :monitorId, :kind, :simpleMetricIds)"; 
		 
	        KeyHolder holder = new GeneratedKeyHolder();
	        SqlParameterSource param = new MapSqlParameterSource()
					.addValue("metricId", m.getMetricId())
					.addValue("type", m.getType())
					.addValue("unit", m.getUnit())
					.addValue("hostId", m.getHostId())
					.addValue("userId", m.getUserId())
					.addValue("monitorId", m.getMonitorId())
					.addValue("kind", m.getKind())
					.addValue("simpleMetricIds", m.getSimpleMetricId());
					
	        template.update(sql,param, holder);
	 
	}


	@Override
	public List<Metric> findAllByHostId(String id) {
		final String sql = "select * from metrics where hostId = :id";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("id", id);
		return template.query(sql, param, new MetricRowMapper());
	}


	@Override
	public Metric findById(String id) {
		final String sql = "select * from metrics where metricId = :id";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("id", id);
		List<Metric> list = template.query(sql, param, new MetricRowMapper());
		
		if (list.isEmpty()){
			return null;
		}
		return list.get(0);
	}


	@Override
	public void deleteMetric(String id) {
		final String sql = "delete from metrics where metricId=:id";
		 

		 Map<String,Object> map=new HashMap<String,Object>();  
		 map.put("id", id);
	
		 template.execute(sql,map,new PreparedStatementCallback<Object>() {  
			    @Override  
			    public Object doInPreparedStatement(PreparedStatement ps)  
			            throws SQLException, DataAccessException {  
			        return ps.executeUpdate();  
			    }  
			});  
	}


	@Override
	public List<Metric> findAllByNameLike(String name_like) {
		final String sql = "select * from metrics where metricId like '%' || :name_like || '%'";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("name_like", name_like);
		List<Metric> list = template.query(sql, param, new MetricRowMapper());
		return list;
	}


	@Override
	public List<Metric> findAllByType(String type) {
		final String sql = "select * from metrics where type = :type";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("type", type);
		List<Metric> list = template.query(sql, param, new MetricRowMapper());
		return list;
	}
	
	
	
}