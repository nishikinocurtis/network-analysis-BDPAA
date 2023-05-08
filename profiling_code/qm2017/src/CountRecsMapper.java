package org.example;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class CountRecsMapper
    extends Mapper<LongWritable, Text, Text, LongWritable> {
    private final static LongWritable one = new LongWritable(1);
    private Text word = new Text("Total Records: ");
    private Text tcpCount = new Text("TCP: ");
    private Text udpCount = new Text("UDP: ");
    private Text tcpMaxRelTime = new Text("TCP Max Relative Time: ");
    private Text tcpMinRelTime = new Text("TCP Min Relative Time: ");

    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        context.write(word, one);
    }
}
