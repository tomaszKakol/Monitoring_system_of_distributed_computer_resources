package com.wfiis.pz.project.monitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.wfiis.pz.project.monitor.controller.ApplicationController;
import com.wfiis.pz.project.monitor.entity.Metric;
import com.wfiis.pz.project.monitor.service.HostService;
import com.wfiis.pz.project.monitor.service.MeasurementService;
import com.wfiis.pz.project.monitor.service.MetricService;
import com.wfiis.pz.project.monitor.utils.HostAbstractView;
import com.wfiis.pz.project.monitor.utils.HostDetails;
import com.wfiis.pz.project.monitor.utils.HostDetailsView;
import com.wfiis.pz.project.monitor.utils.HostView;
import com.wfiis.pz.project.monitor.utils.MeasurementView;
import com.wfiis.pz.project.monitor.utils.MetricAbstractPresenter;
import com.wfiis.pz.project.monitor.utils.MetricPresenter;
import com.wfiis.pz.project.monitor.utils.MetricPreview;
import com.wfiis.pz.project.monitor.utils.MetricView;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorApplicationTests {
	
	
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ApplicationController app;
   
	@Resource 
	private HostService hostService;
   
	@Resource 
	MetricService metricService;
	
	@Resource 
	MeasurementService measurementService;
   
	final String hostIdW = "testHostForTests01";
	final String hostIdL = "testHostForTests02";
	
	
	@Before
	public void initDatabase(){
		//create host for windows
		   HostDetails hd = new HostDetails();
			
		   hd.setHostId(hostIdW);
		   hd.setOs("Windows");
			
		   List<MetricView> metrics = new ArrayList<MetricView>();
		   MetricView mv1 = new MetricView();
		   mv1.setMetricId("CPU_"+hostIdW);
		   mv1.setType("CPU");
		   mv1.setUnit("%");
			
		   MetricView mv2 = new MetricView();
		   mv2.setMetricId("RAM"+hostIdW);
		   mv2.setType("RAM");
		   mv2.setUnit("%");
			
		   metrics.add(mv1);
		   metrics.add(mv2);

		   hd.setMetrics(metrics);
		   
		   app.postHost(hd);
		   
		   try{
			   app.postHost(hd);
		   }catch (Exception e){
		   }
	}
	
	@After
	public void clearDatabase(){
		try{
			hostService.deleteHostById(hostIdW);
		}catch(Exception e){
			
		}
	}
	
	
	@Test
	public void createHost(){
	   //create host for Linux
	   HostDetails hd2 = new HostDetails();
		
	   hd2.setHostId(hostIdL);
	   hd2.setOs("Linux");
		
	   List<MetricView> metrics2 = new ArrayList<MetricView>();
	   MetricView mv12 = new MetricView();
	   mv12.setMetricId("CPU_"+hostIdL);
	   mv12.setType("CPU");
	   mv12.setUnit("%");
		
	   MetricView mv22 = new MetricView();
	   mv22.setMetricId("RAM"+hostIdL);
	   mv22.setType("RAM");
	   mv22.setUnit("%");
		
	   metrics2.add(mv12);
	   metrics2.add(mv22);

	   hd2.setMetrics(metrics2);
	   
      
	   try{
		   app.postHost(hd2);
		   assertEquals(1, 1);
	   }catch (Exception e){
		   assertEquals(1, 0);
	   }
      
   }
	
	
	@Test
	public void getHosts(){
		List<HostAbstractView> hosts = app.getHosts(false);
		assertNotEquals(0, hosts.size());
	}
	
	
	@Test
	public void getHostsWithName(){
		List<HostAbstractView> hosts = app.getHostsWithName(hostIdW, false);
		assertEquals(1, hosts.size());
		assertEquals(hostIdW, hosts.get(0).getHostId());
	}
	
	@Test
	public void getHostsWithWrongName(){
		List<HostAbstractView> hosts = app.getHostsWithName("SomeWrongHostId", false);
		assertEquals(0, hosts.size());
	}
	
	@Test
	public void getHostsWithNameLike(){
		List<HostAbstractView> hosts = app.getHostsWithNameLike(hostIdW, false);
		assertEquals(1, hosts.size());
	}
	
	@Test
	public void getHostsByMetricType(){
		
		List<HostAbstractView> hosts = app.getHostsWithMetricType(1, "CPU", false);
		assertEquals(1, hosts.size());
	}
	
	@Test
	public void getHost(){
		
		HostDetailsView host = app.getHost(hostIdW);
		
		String hostId = host.getHostId();
		String os = host.getOs();
		List<Metric> metrics = host.getMetrics();
		
		assertEquals(hostId, hostIdW);
		assertEquals(os, "Windows");
		assertEquals(2, metrics.size());
	}
	
	
	@Test
	public void getMetrics(){
		MetricPresenter metrics = app.getMetrics();
		List<MetricPreview> mp = metrics.getMetrics();
		boolean result = (2 <= mp.size());
		assertEquals(true, result);
	}
	
	@Test
	public void getMetricsByNameLike(){
		MetricPresenter metrics = app.getMetrics("CPU");
		List<MetricPreview> mp = metrics.getMetrics();
		boolean result = (1 <= mp.size());
		assertEquals(true, result);
	}
	
   
	@Test 
	public void getMetricDetails(){
		Metric metric = app.getMetricDetails("CPU_"+ hostIdW);
		assertEquals(hostIdW, metric.getHostId());
	}
	
	
	@Test
	public void postMeasurementsForMetric(){
		
		List<MeasurementView> measurements = new ArrayList<MeasurementView>();
		measurements.add(new MeasurementView());
		measurements.get(0).setVal("10");
		measurements.get(0).setTs("01/05/2019 12:12:12");
		
		measurements.add(new MeasurementView());
		measurements.get(1).setVal("44");
		measurements.get(1).setTs("01/05/2019 12:12:22");
		
		measurements.add(new MeasurementView());
		measurements.get(2).setVal("36");
		measurements.get(2).setTs("01/05/2019 12:12:32");
		app.postMeasurementsForMetric("CPU_"+ hostIdW, measurements);
		
		
	}
	
	
   @Test 
   public void deleteHost(){
	   try{
		   hostService.deleteHostById(hostIdL);
		   assertEquals(1, 1);
	   }catch(Exception e){
		   assertEquals(1, 0);
	   }
   }

}
