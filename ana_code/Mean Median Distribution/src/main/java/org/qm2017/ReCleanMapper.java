package org.qm2017;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Mapper;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;

public class ReCleanMapper
    extends Mapper<LongWritable, Text, Text, Text> {
    private final double window_size_outlier_threshold = 2.1474508E7;
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        try {
            CSVParser parser = CSVParser.parse(value.toString(), CSVFormat.DEFAULT);
            CSVRecord record = parser.getRecords().get(0);
            String protocol = record.get(6);
            if (protocol.equals("TCP")) {
                String http_request = record.get(19);
                double window_size = Double.parseDouble(record.get(12));
                int tcp_length = Integer.parseInt(record.get(14));


                if (window_size > window_size_outlier_threshold) {
                    return; // discard outlier
                } else {
                    if (http_request.equals("") || tcp_length == 0) {
                        context.write(new Text(""), new Text(value.toString().trim() + ",0")); // trim() to avoid line breakers
                        // 0 for not http or no tcp payload (length == 0)
                    } else {
                        context.write(new Text(""), new Text(value.toString().trim() + ",1"));
                        // 1 otherwise, for clean records
                    }
                }

            } else {
                context.write(new Text(""), new Text(value.toString() + ",1"));
                // 1 for all udp records
            }
        } catch (Exception e) {
            System.out.println("Error parsing CSV: " + e.getMessage());
        }
    }
}
