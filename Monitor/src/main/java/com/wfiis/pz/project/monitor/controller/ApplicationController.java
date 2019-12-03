package com.wfiis.pz.project.monitor.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.wfiis.pz.project.monitor.entity.Host;
import com.wfiis.pz.project.monitor.entity.Measurement;
import com.wfiis.pz.project.monitor.entity.Metric;
import com.wfiis.pz.project.monitor.service.HostService;
import com.wfiis.pz.project.monitor.service.MeasurementService;
import com.wfiis.pz.project.monitor.service.MetricService;
import com.wfiis.pz.project.monitor.utils.CompoundMetric;
import com.wfiis.pz.project.monitor.utils.HostAbstractView;
import com.wfiis.pz.project.monitor.utils.HostDetails;
import com.wfiis.pz.project.monitor.utils.HostDetailsView;
import com.wfiis.pz.project.monitor.utils.HostView;
import com.wfiis.pz.project.monitor.utils.MeasurementView;
import com.wfiis.pz.project.monitor.utils.MetaDataForMetric;
import com.wfiis.pz.project.monitor.utils.MetricAbstractPresenter;
import com.wfiis.pz.project.monitor.utils.MetricPresenter;
import com.wfiis.pz.project.monitor.utils.MetricPresenterReqursive;
import com.wfiis.pz.project.monitor.utils.MetricPreview;


@RestController
@RequestMapping("/${MONITORID}")
public class ApplicationController extends WebConfig {

	@Resource
	HostService hostService;

	@Resource
	MetricService metricService;

	@Resource
	MeasurementService measurementService;

	@Autowired
	private Environment env;

	@CrossOrigin
	@GetMapping(value = "/protected")
	public ResponseEntity<Object> getUserNameStub(
			@RequestHeader(value = "Authorization", required = false, defaultValue = "") String header) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		HashMap<String, String> map = new HashMap<>();

		if (!header.isEmpty() && header.split(" ").length == 2) {
			map.put("logged_in_as", "<username>");
		} else {
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.OK);
	}

	/**
	 * Function checking if the operation is authorized
	 * 
	 * @param token encoded token value
	 * @return returns true if operation is allowed
	 */
	public boolean checkIfProtected(String token) {

		try {

			String httpurl = env.getProperty("AUTH_SERVICE_URL")+"protected";

			URL url = new URL(httpurl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", "Bearer " + token);
			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				return true;
			}

		} catch (Exception e) {
			return false;
		}

		return false;
	}
	
	@GetMapping(value = "/hosts")
	public ResponseEntity<Object> getHostsWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@RequestParam(value = "recursive", required = false, defaultValue = "false") Boolean recursive) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(getHosts(recursive), responseHeaders, HttpStatus.OK);
	}

	/** 
	 * Function that get all hosts
	 * 
	 * @param recursive If true the result list is recursive
	 * @return Required list of views of hosts
	 */
	public List<HostAbstractView> getHosts(Boolean recursive) {
		List<Host> hosts;
		hosts = hostService.findAll();
		List<HostAbstractView> views = new ArrayList<HostAbstractView>();

		if (recursive.booleanValue() == false) {
			for (Host h : hosts) {
				views.add(new HostView(h));
			}
			return views;
		} else {
			for (Host h : hosts) {
				views.add(getHost(h.getHostId()));
			}
			return views;
		}
	}

	@GetMapping(value = "/hosts", params = { "name" })
	public ResponseEntity<Object> getHostsWithNameWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@RequestParam("name") String name,
			@RequestParam(value = "recursive", required = false, defaultValue = "false") Boolean recursive) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(getHostsWithName(name, recursive), responseHeaders, HttpStatus.OK);

	}

	/**
	 * Returns list of hosts with specific name
	 * @param name Name of host (it is also host id)
	 * @param recursive Changing the presented view
	 * @return Required list of views of hosts
	 */
	public List<HostAbstractView> getHostsWithName(String name, Boolean recursive) {
		List<Host> hosts;
		hosts = hostService.findAllByName(name);

		List<HostAbstractView> views = new ArrayList<HostAbstractView>();

		if (recursive.booleanValue() == false) {
			for (Host h : hosts) {
				views.add(new HostView(h));
			}
			return views;
		} else {
			for (Host h : hosts) {
				views.add(getHost(h.getHostId()));
			}
			return views;
		}
	}

	@GetMapping(value = "/hosts", params = { "name_like" })
	public ResponseEntity<Object> getHostsWithNameLikeWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@RequestParam("name_like") String name_like,
			@RequestParam(value = "recursive", required = false, defaultValue = "false") Boolean recursive) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(getHostsWithNameLike(name_like, recursive), responseHeaders, HttpStatus.OK);

	}

	/**
	 * Returns list of hosts with name_like pattern
	 * 
	 * @param name_like Part of name of host (it is also host id)
	 * @param recursive Changing the presented view
	 * @return Required list of views of hosts
	 */
	public List<HostAbstractView> getHostsWithNameLike(String name_like, Boolean recursive) {
		List<Host> hosts;
		hosts = hostService.findAllByNameLike(name_like);

		List<HostAbstractView> views = new ArrayList<HostAbstractView>();

		if (recursive.booleanValue() == false) {
			for (Host h : hosts) {
				views.add(new HostView(h));
			}
			return views;
		} else {
			for (Host h : hosts) {
				views.add(getHost(h.getHostId()));
			}
			return views;
		}
	}

	@GetMapping(value = "/hosts", params = { "top", "metric_type" })
	public ResponseEntity<Object> getHostsWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@RequestParam("top") Integer top, @RequestParam("metric_type") String metric_type,
			@RequestParam(value = "recursive", required = false, defaultValue = "false") Boolean recursive) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(getHostsWithMetricType(top, metric_type, recursive), responseHeaders,
				HttpStatus.OK);

	}

	/**
	 * Returns list of hosts (views) with specific type.
	 * 
	 * @param top number of returned hosts
	 * @param metric_type type of metrics
	 * @param recursive change view
	 * @return list of required views of hosts 
	 */
	public List<HostAbstractView> getHostsWithMetricType(Integer top, String metric_type, Boolean recursive) {
		List<Host> hosts;
		hosts = hostService.findTopByMetricType(top, metric_type);

		List<HostAbstractView> views = new ArrayList<HostAbstractView>();

		if (recursive.booleanValue() == false) {
			for (Host h : hosts) {
				views.add(new HostView(h));
			}
			return views;
		} else {
			for (Host h : hosts) {
				views.add(getHost(h.getHostId()));
			}
			return views;
		}
	}

	@PostMapping(value = "/hosts")
	public ResponseEntity<Object> postHostWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@RequestBody HostDetails hd) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		try {
			postHost(hd);
			return new ResponseEntity<Object>(null, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			deleteHost(hd.getHostId());
			return new ResponseEntity<Object>(null, responseHeaders, HttpStatus.CONFLICT);
		}

	}

	/**
	 * Adding new host
	 * 
	 * @param hd host details passed in body
	 */
	public void postHost(HostDetails hd) {
		Host h = hd.extractHost();
		h.setMonitorId(env.getProperty("MONITORID"));
		List<Metric> metrics = hd.extractMetricList();
		hostService.insertHost(h);
		for (Metric m : metrics) {
			m.setMonitorId(env.getProperty("MONITORID"));
			metricService.insertMetric(m);
		}
	}

	@GetMapping(value = "/hosts/{id}")
	public ResponseEntity<Object> getHostWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@PathVariable String id) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(getHost(id), responseHeaders, HttpStatus.OK);
	}

	/**
	 *  Getting specific host
	 * @param id host name (id)
	 * @return view of this host
	 */
	public HostDetailsView getHost(String id) {
		HostDetailsView hdv = new HostDetailsView();

		Host h = hostService.findHostById(id);

		List<Metric> metrics = metricService.findAllByHostId(id);

		hdv.setHostId(h.getHostId());
		hdv.setOs(h.getOs());
		hdv.setMetrics(metrics);

		return hdv;
	}

	@DeleteMapping(value = "/hosts/{id}")
	public ResponseEntity<Object> deleteHostWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@PathVariable String id) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		deleteHost(id);

		return new ResponseEntity<Object>(null, responseHeaders, HttpStatus.OK);
	}

	/**
	 * Deleting specific host
	 * 
	 * @param id host id (name)
	 */
	public void deleteHost(String id) {
		hostService.deleteHostById(id);
	}

	@CrossOrigin
	@GetMapping(value = "/metrics")
	public ResponseEntity<Object> getMetricsWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@RequestParam(value = "name_like", required = false, defaultValue = "") String name_like,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "meta", required = false, defaultValue = "false") String meta,
			@RequestParam(value = "recursive", required = false, defaultValue = "false") Boolean recursive) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(getMetrics(name_like, type, meta, recursive), responseHeaders, HttpStatus.OK);

	}

	/**
	 * Getting all metrics depending of the param values. 
	 * 
	 * @param name_like part of metric name
	 * @param type type of metrics
	 * @param meta parameter that defines if meta data should be presented
	 * @param recursive parameter that defines how to present metrics
	 * @return wrapped list of metrics
	 */
	public MetricAbstractPresenter getMetrics(String name_like, String type, String meta, Boolean recursive) {

		List<Metric> metrics = new ArrayList<Metric>();

		if (!name_like.isEmpty() && !type.isEmpty()) {
			List<Metric> metricsByNameLike = metricService.findAllByNameLike(name_like);
			List<Metric> metricsByType = metricService.findAllByType(type);

			HashSet<Metric> joinedLists = new HashSet<Metric>(metricsByNameLike);
			joinedLists.addAll(metricsByType);

			metrics = new ArrayList<>(joinedLists);
		} else if (!name_like.isEmpty()) {
			metrics = metricService.findAllByNameLike(name_like);
		} else if (!type.isEmpty()) {
			metrics = metricService.findAllByType(type);
		} else {
			metrics = metricService.findAll();
		}

		if (meta != null && meta.equals("only")) {
			MetricPresenter mp = new MetricPresenter();
			List<String> types = new ArrayList<String>();
			for (Metric m : metrics) {
				types.add(m.getType());
			}
			MetaDataForMetric metas = new MetaDataForMetric();
			metas.setTypes(types);
			mp.setMeta(metas);
			return mp;

		} else if (recursive.booleanValue() == false) {
			MetricPresenter mp = new MetricPresenter();
			List<MetricPreview> previews = new ArrayList<MetricPreview>();
			List<String> types = new ArrayList<String>();
			for (Metric m : metrics) {
				previews.add(new MetricPreview(m));
				types.add(m.getType());
			}

			if (meta != null && meta.equals("true")) {
				MetaDataForMetric metas = new MetaDataForMetric();
				metas.setTypes(types);
				mp.setMeta(metas);
			}
			mp.setMetrics(previews);

			return mp;

		} else {
			MetricPresenterReqursive mp = new MetricPresenterReqursive();
			List<String> types = new ArrayList<String>();
			for (Metric m : metrics) {
				types.add(m.getType());
			}

			if (meta != null && meta.equals("true")) {
				MetaDataForMetric metas = new MetaDataForMetric();
				metas.setTypes(types);
				mp.setMeta(metas);
			}
			mp.setMetrics(metrics);

			return mp;
		}

	}

	/**
	 * helping function that gains all metrics
	 * @return wrapped list of metrics
	 */
	public MetricPresenter getMetrics() {
		try {
			MetricPresenter mp = (MetricPresenter) getMetrics("", "", "", false);
			return mp;
		} catch (Exception e) {
			return new MetricPresenter();
		}
	}

	/**
	 * helping function that gains all metrics by part of their name
	 * @param name_like part of the metric's name
	 * @return wrapped list of metrics
	 */
	public MetricPresenter getMetrics(String name_like) {
		try {
			MetricPresenter mp = (MetricPresenter) getMetrics(name_like, "", "", false);
			return mp;
		} catch (Exception e) {
			return new MetricPresenter();
		}
	}

	/**
	 * helping function that gains all metrics
	 * @param meta check if there sould be meta data printed
	 * @return wrapped list of metrics
	 */
	public MetricPresenter getMetricsWithMetas(String meta) {
		try {
			MetricPresenter mp = (MetricPresenter) getMetrics("", "", "true", false);
			return mp;
		} catch (Exception e) {
			return new MetricPresenter();
		}
	}

	public MetricAbstractPresenter getMetrics(Boolean recursive) {
		return getMetrics("", "", "", recursive);
	}

	@CrossOrigin
	@PostMapping(value = "/metrics")
	public ResponseEntity<Object> postMetricWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@RequestBody CompoundMetric cm) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}
		
		Metric m = metricService.findMetricById(cm.getMetricIds());
		
		if (m == null){
			return new ResponseEntity<Object>(new HashMap<>(), responseHeaders, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Object>(postMetric(cm), responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * Adding new compound metric to database
	 * @param cm Compound metric to insert
	 * @return preview of created metric
	 */
	public MetricPreview postMetric(CompoundMetric cm) {

		Metric simple = metricService.findAllByNameLike(cm.getMetricIds()).get(0);

		Metric m = new Metric();
		Date date = new Date();
		long time = date.getTime();
		Timestamp tstp = new Timestamp(time);
		m.setMetricId("CM " + tstp.toString());
		m.setType(cm.getType());
		m.setUnit(simple.getUnit());
		m.setHostId(simple.getHostId());
		m.setUserId(simple.getUserId());

		m.setMonitorId(env.getProperty("MONITORID"));

		m.setKind(cm.getKind());
		m.setSimpleMetricId(cm.getMetricIds());

		MetricPreview mp = new MetricPreview(m);

		m.setMonitorId(env.getProperty("MONITORID"));
		metricService.insertMetric(m);

		return mp;
	}

	@CrossOrigin
	@GetMapping(value = "/metrics/{id}")
	public ResponseEntity<Object> getMetricDetailsWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@PathVariable String id) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(getMetricDetails(id), responseHeaders, HttpStatus.OK);
	}

	/**
	 * Getting metric details by it's id
	 * @param id id of the metric (also name)
	 * @return metric 
	 */
	public Metric getMetricDetails(String id) {
		Metric m = metricService.findMetricById(id);
		return m;
	}

	@CrossOrigin
	@DeleteMapping(value = "/metrics/{id}")
	public ResponseEntity<Object> deleteMetricWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@PathVariable String id) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		deleteMetric(id);

		return new ResponseEntity<Object>(null, responseHeaders, HttpStatus.OK);

	}

	/**
	 * Deleting metric by it's id (name)
	 * @param id name of metric
	 */
	public void deleteMetric(String id) {
		metricService.deleteMetricById(id);
		return;
	}

	/*
	 * @GetMapping(value = "/metrics/{id}/measurements") public
	 * ResponseEntity<Object>
	 * getMeasurementsForMetricWrapper( @RequestHeader(value="access-token",
	 * required = false, defaultValue = "") String header,
	 * 
	 * @PathVariable String id) {
	 * 
	 * HttpHeaders responseHeaders = new HttpHeaders();
	 * responseHeaders.set("access-token", header);
	 * System.out.print("Wykonuje dla wszystkich");
	 * 
	 * return new
	 * ResponseEntity<Object>(getMeasurementsForMetric(id),responseHeaders,
	 * HttpStatus.OK); }
	 * 
	 * 
	 * 
	 * public List<MeasurementView> getMeasurementsForMetric(String id) {
	 * 
	 * List<Measurement> measurements; Metric metric =
	 * metricService.findMetricById(id);
	 * 
	 * if (metric.getKind()!=null && metric.getKind().equals("moving-avg")){
	 * measurements=
	 * measurementService.findTopMeasurementBySimpleMetricId(metric.
	 * getSimpleMetricId(), 25); }else{ measurements=
	 * measurementService.findTopMeasurementByMetricId(id, 25); }
	 * 
	 * List<MeasurementView> views = new ArrayList<MeasurementView>(); for
	 * (Measurement m : measurements){ views.add(new MeasurementView(m)); }
	 * 
	 * return views; }
	 * 
	 * 
	 * 
	 * 
	 * @GetMapping(value = "/metrics/{id}/measurements", params = {"n"}) public
	 * ResponseEntity<Object>
	 * getNMeasurementsForMetric( @RequestHeader(value="access-token", required
	 * = false, defaultValue = "") String header,
	 * 
	 * @PathVariable String id,
	 * 
	 * @RequestParam(value = "n", required = false) Integer n ) {
	 * 
	 * System.out.print("wykonuje dla n"); HttpHeaders responseHeaders = new
	 * HttpHeaders(); responseHeaders.set("access-token", header);
	 * 
	 * return new
	 * ResponseEntity<Object>(getNMeasurementsForMetric(id,n),responseHeaders,
	 * HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * public List<MeasurementView> getNMeasurementsForMetric( String id,
	 * Integer n ) { List<Measurement> measurements;
	 * 
	 * Metric metric = metricService.findMetricById(id);
	 * 
	 * if (metric.getKind()!=null && metric.getKind().equals("moving-avg")){ if
	 * (n != null){ measurements=
	 * measurementService.findTopMeasurementBySimpleMetricId(metric.
	 * getSimpleMetricId(), n); }else{ measurements=
	 * measurementService.findTopMeasurementBySimpleMetricId(metric.
	 * getSimpleMetricId(), 25); } }else{ if (n != null){ measurements=
	 * measurementService.findTopMeasurementByMetricId(id, n); }else{
	 * measurements= measurementService.findTopMeasurementByMetricId(id, 25); }
	 * }
	 * 
	 * 
	 * 
	 * 
	 * List<MeasurementView> views = new ArrayList<MeasurementView>(); for
	 * (Measurement m : measurements){ views.add(new MeasurementView(m)); }
	 * 
	 * return views; }
	 * 
	 */

	@CrossOrigin
	@GetMapping(value = "/metrics/{id}/measurements")
	public ResponseEntity<Object> getMeasurementsFromRangeForMetricWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@PathVariable String id, @RequestParam(value = "from", required = false, defaultValue = "") String from,
			@RequestParam(value = "to", required = false, defaultValue = "") String to,
			@RequestParam(value = "n", required = false, defaultValue = "25") Integer n,
			@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all

	) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(getMeasurementsFromRangeForMetric(id, from, to, n, all), responseHeaders,
				HttpStatus.OK);

	}

	/**
	 * Return list of measurements of specific metric
	 * @param id name of metric
	 * @param from date in format string (oldest) 
	 * @param to date in format string (newest)
	 * @param n number of returned measurements
	 * @param all parameter that set if all measurements should be returned
	 * @return list of measurements
	 */
	public List<MeasurementView> getMeasurementsFromRangeForMetric(String id, String from, String to, Integer n,
			Boolean all) {
		List<Measurement> measurements;

		Metric metric = metricService.findMetricById(id);

		if (metric.getKind() != null && metric.getKind().equals("moving-avg")) {
			if (!from.isEmpty() || !to.isEmpty()) {
				measurements = measurementService.findByDateMeasurementBySimpleMetricId(metric.getSimpleMetricId(),
						from, to, n, all);
			} else {
				measurements = measurementService.findTopMeasurementBySimpleMetricId(metric.getSimpleMetricId(), n,
						all);
			}
		} else {
			if ((!from.isEmpty()) || (!to.isEmpty())) {
				measurements = measurementService.findByDateMeasurementByMetricId(id, from, to, n, all);
				// System.out.print("Wszedłem do froma");
			} else {
				measurements = measurementService.findTopMeasurementByMetricId(id, n, all);
				// System.out.print("Nie Wszedłem do froma");
			}
		}

		List<MeasurementView> views = new ArrayList<MeasurementView>();
		for (Measurement m : measurements) {
			views.add(new MeasurementView(m));
		}

		return views;
	}

	/*
	 * @GetMapping(value = "/metrics/{id}/measurements", params = {"all"})
	 * public ResponseEntity<Object>
	 * getMeasurementsForMetric( @RequestHeader(value="access-token", required =
	 * false, defaultValue = "") String header,
	 * 
	 * @PathVariable String id,
	 * 
	 * @RequestParam(value = "all", required = false) Boolean all ) {
	 * HttpHeaders responseHeaders = new HttpHeaders();
	 * responseHeaders.set("access-token", header);
	 * 
	 * return new
	 * ResponseEntity<Object>(getMeasurementsForMetric(id,all),responseHeaders,
	 * HttpStatus.OK); }
	 * 
	 * 
	 * public List<MeasurementView> getMeasurementsForMetric( String id, Boolean
	 * all ) { List<Measurement> measurements; Metric metric =
	 * metricService.findMetricById(id);
	 * 
	 * if (metric.getKind()!=null && metric.getKind().equals("moving-avg")){ if
	 * (all != null && all.booleanValue() == true){ measurements=
	 * measurementService.findMeasurementBySimpleMetricId(metric.
	 * getSimpleMetricId());
	 * 
	 * }else{ measurements=
	 * measurementService.findTopMeasurementBySimpleMetricId(metric.
	 * getSimpleMetricId(), 25); } }else{ if (all != null && all.booleanValue()
	 * == true){ measurements= measurementService.findMeasurementByMetricId(id);
	 * 
	 * }else{ measurements= measurementService.findTopMeasurementByMetricId(id,
	 * 25); } }
	 * 
	 * 
	 * 
	 * 
	 * List<MeasurementView> views = new ArrayList<MeasurementView>(); for
	 * (Measurement m : measurements){ views.add(new MeasurementView(m)); }
	 * 
	 * return views; }
	 */

	@CrossOrigin
	@PostMapping(value = "/metrics/{id}/measurements")
	public ResponseEntity<Object> postMeasurementsForMetricWrapper(
			@RequestHeader(value = "access-token", required = false, defaultValue = "") String header,
			@PathVariable String id, @RequestBody List<MeasurementView> views) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("access-token", header);

		boolean isProtected = checkIfProtected(header);
		if (!isProtected) {
			HashMap<String, String> map = new HashMap<>();
			return new ResponseEntity<Object>(map, responseHeaders, HttpStatus.UNAUTHORIZED);
		}

		postMeasurementsForMetric(id, views);
		return new ResponseEntity<Object>(null, responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * Adding new measurements to specific metric
	 * @param id metric name
	 * @param views measurements to add
	 */
	public void postMeasurementsForMetric(String id, List<MeasurementView> views) {

		for (MeasurementView mv : views) {
			Measurement m = new Measurement();
			m.setMetricId(id);
			m.setVal(mv.getVal());
			m.setTs(mv.getTs());

			measurementService.insertMeasurment(m);

		}

		return;
	}

}