USE qm2017_nyu_edu;

CREATE EXTERNAL TABLE tcp_numerical (
	src_port DOUBLE, dst_port DOUBLE, seq_num DOUBLE, 
	ime_delta DOUBLE, time_relative DOUBLE, window_size DOUBLE, length DOUBLE, segments DOUBLE)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
STORED AS TEXTFILE
location 'hdfs://nyu-dataproc-m/user/qm2017_nyu_edu/fproj/netcap/numerical_for_learn';

SELECT count(*) FROM tcp_numerical;

SELECT * FROM tcp_numerical LIMIT 5;
