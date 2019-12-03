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

import com.wfiis.pz.project.monitor.entity.Measurement;
import com.wfiis.pz.project.monitor.mapper.MeasurementRowMapper;
import com.wfiis.pz.project.monitor.utils.Util;

/**
 * 
 * @author Mateusz Papież
 *
 */
@Repository
public class MeasurementDaoImpl implements MeasurementDao{
	
	public MeasurementDaoImpl(NamedParameterJdbcTemplate template) {  
        this.template = template;  
	}  
	
	
	NamedParameterJdbcTemplate template;  

	@Override
	public List<Measurement> findAll() {
		return template.query("select * from measurements order by ts desc", new MeasurementRowMapper());
	}
	
	
	@Override
	public void insertMeasurement(Measurement m) {
		 final String sql = "insert into measurements(metricId , val, ts) values(:metricId,:val,:ts)"; 
		 
	        KeyHolder holder = new GeneratedKeyHolder();
	        SqlParameterSource param = new MapSqlParameterSource()
					.addValue("metricId", m.getMetricId())
					.addValue("val", m.getVal())
					.addValue("ts", Util.convertStringToTimestamp(m.getTs()));
	        template.update(sql,param, holder);
	 
	}


	@Override
	public List<Measurement> findAllByMetricId(String id) {
		 SqlParameterSource param = new MapSqlParameterSource()
					.addValue("id", id);
		return template.query("select * from measurements where metricId = :id order by ts desc", param, new MeasurementRowMapper());
		
	}


	@Override
	public List<Measurement> findTopByMetricId(String id, Integer n, Boolean all) {
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("id", id)
				.addValue("n", n);
		
		String sql = "select * from measurements where metricId = :id order by ts desc";
		
		if (all.booleanValue() == false){
			sql = sql + " limit :n";
		}
		
		return template.query(sql, param, new MeasurementRowMapper());
	}


	@Override
	public List<Measurement> findByDateMeasurementByMetricId(String id, String from, String to, Integer n, Boolean all) {
		
		String sql = "select * from measurements where ts > :from and ts < :to and metricid = :id order by ts desc";
		
		
		
		SqlParameterSource param;
		
		if (from.isEmpty())
			from = "01/01/1999 00:00:00";
		
		if (to.isEmpty()){
			sql = "select * from measurements where ts > :from and metricid = :id order by ts desc";
			param = new MapSqlParameterSource()
					.addValue("from", Util.convertStringToTimestamp(from))
					.addValue("id", id)
					.addValue("n", n);
		}else{
			param = new MapSqlParameterSource()
					.addValue("from", Util.convertStringToTimestamp(from))
					.addValue("to", Util.convertStringToTimestamp(to))
					.addValue("id", id)
					.addValue("n", n);
		}
		
		if (all.booleanValue() == false){
			sql = sql + " limit :n";
		}
		
		
	return template.query(sql, param, new MeasurementRowMapper());
	}


	
	
	
	@Override
	public List<Measurement> findMeasurementBySimpleMetricId(String simpleMetricId) {
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("metricId", simpleMetricId);
		
		final String query = "" +
			"select metricid, end_time as ts, avg_val as val from ( " +
					" with five_min_intervals as ( " +
					" select " +
					    " (select min(ts)::date from measurements) + ( n    || ' minutes')::interval start_time, " +
					    " (select min(ts)::date from measurements) + ((n+5) || ' minutes')::interval end_time " +
					  " from generate_series(0, (24*60), 1) n " +
					" ) " +
					" select metricid, f.start_time, f.end_time, avg(cast(m.val as float)) avg_val , count(*) " +
					" from measurements m " +
					" right join five_min_intervals f " + 
					        "on m.ts >= f.start_time and m.ts < f.end_time " +
					" group by metricid, f.start_time, f.end_time " +
					" order by f.start_time " +
					" ) as tabela where avg_val is not null and end_time < now() and metricid = :metricId order by end_time desc";
		
		return template.query(query, param, new MeasurementRowMapper());
	}


	@Override
	public List<Measurement> findByDateMeasurementBySimpleMetricId(String simpleMetricId, String from, String to, Integer n, Boolean all) {
		String sql = "" +
				"select metricid, end_time as ts, avg_val as val from ( " +
						" with five_min_intervals as ( " +
						" select " +
						    " (select min(ts)::date from measurements) + ( n    || ' minutes')::interval start_time, " +
						    " (select min(ts)::date from measurements) + ((n+5) || ' minutes')::interval end_time " +
						  " from generate_series(0, (24*60), 1) n " +
						" ) " +
						" select metricid, f.start_time, f.end_time, avg(cast(m.val as float)) avg_val , count(*) " +
						" from measurements m " +
						" right join five_min_intervals f " + 
						        "on m.ts >= f.start_time and m.ts < f.end_time " +
						" group by metricid, f.start_time, f.end_time " +
						" order by f.start_time " +
						" ) as tabela"; 
		
		
		
		
		SqlParameterSource param; 
		
		if (from == null || !from.isEmpty())
			from = "01/01/1999 00:00:00";
		
		if (to == null || !to.isEmpty()){
			sql = sql + " where avg_val is not null and end_time < now() and metricid = :metricId and end_time > :from order by end_time desc";
			
			param = new MapSqlParameterSource()
					.addValue("from", Util.convertStringToTimestamp(from))
					.addValue("metricId", simpleMetricId)
					.addValue("n", n);
		}else{
			
			sql = sql + " where avg_val is not null and end_time < now() and metricid = :metricId and end_time > :from and end_time < :to order by end_time desc";
			param = new MapSqlParameterSource()
					.addValue("from", Util.convertStringToTimestamp(from))
					.addValue("to", Util.convertStringToTimestamp(to))
					.addValue("metricId", simpleMetricId)
					.addValue("n", n);
		}
		
		
		if (all.booleanValue() == false){
			sql = sql + " limit :n";
		}
		
		return template.query(sql, param, new MeasurementRowMapper());
	}


	@Override
	public List<Measurement> findTopMeasurementBySimpleMetricId(String simpleMetricId, Integer n, Boolean all) {
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("metricId", simpleMetricId)
				.addValue("n", n);
		
		String sql = 	"" +
				"select metricid, end_time as ts, avg_val as val from ( " +
				" with five_min_intervals as ( " +
				" select " +
				    " (select min(ts)::date from measurements) + ( n    || ' minutes')::interval start_time, " +
				    " (select min(ts)::date from measurements) + ((n+5) || ' minutes')::interval end_time " +
				  " from generate_series(0, (24*60), 1) n " +
				" ) " +
				" select metricid, f.start_time, f.end_time, avg(cast(m.val as float)) avg_val , count(*) " +
				" from measurements m " +
				" right join five_min_intervals f " + 
				        "on m.ts >= f.start_time and m.ts < f.end_time " +
				" group by metricid, f.start_time, f.end_time " +
				" order by f.start_time " +
				" ) as tabela where avg_val is not null and end_time < now() and metricid = :metricId order by end_time desc";
		
		
		if (all.booleanValue() == false){
			sql = sql + " limit :n";
		}
		
		return template.query(sql, param, new MeasurementRowMapper());
	}
	
	
	
}