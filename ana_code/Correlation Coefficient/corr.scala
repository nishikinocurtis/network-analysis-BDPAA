import spark.implicits._

val sqlContext = spark.sqlContext

spark.sql("use qm2017_nyu_edu")
val df = spark.sql("select time_relative, window_size from tcp_numerical")

println("Corr time_relative - window_size ")
println(df.stat.corr("time_relative", "window_size"))

val df_full = spark.sql("select * from tcp_numerical")

df_full.stat.corr("seq_num", "window_size")
df_full.stat.corr("seq_num", "time_relative")
df_full.stat.corr("length", "window_size")
df_full.stat.corr("ime_delta", "window_size")
df_full.stat.corr("ime_delta", "seq_num")
df_full.stat.corr("src_port", "dst_port")
