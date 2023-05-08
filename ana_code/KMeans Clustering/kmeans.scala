import org.apache.spark.sql.Dataset
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import spark.implicits._

case class TCP_numerical (
	src_port: Double, dst_port: Double, 
	seq_num: Double, ime_delta: Double, 
	time_relative: Double, window_size: Double, 
	length: Double, segments: Double)

spark.sql("use qm2017_nyu_edu")
val df = spark.sql("select * from tcp_numerical")

val dataset: Dataset[TCP_numerical] = df.as[TCP_numerical]
val kmeans = new KMeans().setK(10).setSeed(1L)

val assembler = new VectorAssembler()
	.setInputCols(Array("src_port", "dst_port", "seq_num", "ime_delta", "time_relative", "window_size", "length", "segments"))
	.setOutputCol("features")
	.setHandleInvalid("skip")
val ds_kmeans = assembler.transform(dataset).select("features")

val model = kmeans.fit(ds_kmeans)
val predictions = model.transform(ds_kmeans)

val evaluator = new ClusteringEvaluator()
val silhouette = evaluator.evaluate(predictions)
println("silhouette score: ")
println(silhouette)

println("clustering centers: ")
centers.foreach(println)


