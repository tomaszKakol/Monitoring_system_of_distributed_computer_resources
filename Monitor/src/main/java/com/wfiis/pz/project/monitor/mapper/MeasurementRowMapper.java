package com.wfiis.pz.project.monitor.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.wfiis.pz.project.monitor.entity.Measurement;
import com.wfiis.pz.project.monitor.utils.Util;


/**
 * 
 * @author Mateusz Papież
 * 
 * Class mapper for measurements
 *
 */
public class MeasurementRowMapper implements RowMapper<Measurement> {

	@Override
	public Measurement mapRow(ResultSet rs, int arg1) throws SQLException {
		Measurement m = new Measurement();
		
		m.setMetricId(rs.getString("metricId"));
		m.setVal(rs.getString("val"));
		
		m.setTs(Util.convertTimestampToString(rs.getTimestamp("ts")) );
 
        return m;
	}


}