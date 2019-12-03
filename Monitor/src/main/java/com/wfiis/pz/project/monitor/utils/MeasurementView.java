package com.wfiis.pz.project.monitor.utils;

import com.wfiis.pz.project.monitor.entity.Measurement;

/**
 * 
 * @author Mateusz Papież
 *
 */
public class MeasurementView {
	String val;
	String ts;
	
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	
	public MeasurementView(Measurement m){
		this.val = m.getVal();
		this.ts = m.getTs();
	}
	public MeasurementView(){}
}
