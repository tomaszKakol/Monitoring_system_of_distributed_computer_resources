CREATE TABLE hosts
(
  hostId varchar(40) not null,
  os varchar(255),
  monitorId varchar(40) not null,
  PRIMARY KEY (hostId)
);

CREATE TABLE metrics
(
  metricId varchar(40) not null,
  type varchar(40) not null,
  unit varchar(40),
  hostId varchar(40) not null,
  userId varchar(40),
  monitorId varchar(40) not null,
  kind varchar(40),
  simpleMetricIds varchar(120),
  
  PRIMARY KEY (metricId)
);

CREATE TABLE measurements
(
  metricId varchar(40) not null,
  val varchar(48) not null,
  ts timestamp not null
);