package com.wfiis.pz.project.monitor.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.wfiis.pz.project.monitor.entity.Metric;


/**
 * 
 * @author Mateusz Papież
 * 
 * Class mapper for metrisc
 *
 */
public class MetricRowMapper implements RowMapper<Metric> {

	@Override
	public Metric mapRow(ResultSet rs, int arg1) throws SQLException {
		Metric m = new Metric();
		
		m.setMetricId(rs.getString("metricId"));
		m.setType(rs.getString("type"));
		m.setUnit(rs.getString("unit"));
		m.setHostId(rs.getString("hostId"));
		m.setUserId(rs.getString("userId"));
		m.setMonitorId(rs.getString("monitorId"));
		m.setKind(rs.getString("kind"));
		m.setSimpleMetricId(rs.getString("simpleMetricIds"));
 
        return m;
	}


}