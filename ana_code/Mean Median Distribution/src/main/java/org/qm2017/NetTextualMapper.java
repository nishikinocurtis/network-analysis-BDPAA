package org.qm2017;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;

public class NetTextualMapper
    extends Mapper<LongWritable, Text, Text, LongWritable> {
    private final static LongWritable one = new LongWritable(1);
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        try {
            CSVParser parser = CSVParser.parse(value.toString(), CSVFormat.DEFAULT);
            CSVRecord record = parser.getRecords().get(0);
            String protocol = record.get(6);
            if (protocol.equals("TCP")) { // TCP Metrics
                String src_ip = record.get(2);
                String dst_ip = record.get(3);
                String src_port = record.get(7);
                String dst_port = record.get(8);
                String tcp_seq = record.get(9);
                String http_method = record.get(19);
                String http_resp_code = record.get(20);

                context.write(new Text("TCP Source IP " + src_ip), one);
                context.write(new Text("TCP Destination IP " + dst_ip), one);
                context.write(new Text("TCP Source Port " + src_port), one);
                context.write(new Text("TCP Destination Port " + dst_port), one);
                context.write(new Text("TCP Sequence Number " + tcp_seq), one);

                if (!http_method.equals("")) {
                    context.write(new Text("HTTP Method " + http_method), one);
                    context.write(new Text("HTTP Response Code " + http_resp_code), one);
                }
            } else { // UDP Metrics
                String src_ip = record.get(2);
                String dst_ip = record.get(3);
                String src_port = record.get(16);
                String dst_port = record.get(17);

                context.write(new Text("UDP Source IP " + src_ip), one);
                context.write(new Text("UDP Destination IP " + dst_ip), one);
                context.write(new Text("UDP Source Port " + src_port), one);
                context.write(new Text("UDP Destination Port " + dst_port), one);
            }
        } catch (Exception e) {
            System.out.println("Error parsing CSV: " + e.getMessage());
        }
    }
}
