package com.wfiis.pz.project.monitor.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.wfiis.pz.project.monitor.entity.Host;


/**
 * 
 * @author Mateusz Papież
 * 
 * Class mapper for hosts
 *
 */
public class HostRowMapper implements RowMapper<Host> {

	@Override
	public Host mapRow(ResultSet rs, int arg1) throws SQLException {
		Host host = new Host();
		host.setHostId(rs.getString("hostId"));
		host.setMonitorId(rs.getString("monitorId"));
		host.setOs(rs.getString("os"));
 
        return host;
	}


}