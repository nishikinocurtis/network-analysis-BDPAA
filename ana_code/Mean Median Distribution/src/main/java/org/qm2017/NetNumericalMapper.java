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

public class NetNumericalMapper
        extends Mapper<LongWritable, Text, Text, FloatWritable> {

    private final HashMap<String, Integer> attributes = new HashMap<String, Integer>();
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        try {
            CSVParser parser = CSVParser.parse(value.toString(), CSVFormat.DEFAULT);
            CSVRecord record = parser.getRecords().get(0);
            String protocol = record.get(6);
            if (protocol.equals("TCP")) { // TCP Metrics
                // System.out.println("TCP");
                attributes.put("TCP Time Delta", 10);
                attributes.put("TCP Time Relative", 11);
                attributes.put("TCP Window Size", 12);
                attributes.put("TCP Length", 14);
                attributes.put("TCP Segments", 15);

                for (String attribute : attributes.keySet()) {
                    String raw_value = record.get(attributes.get(attribute));
                    if (raw_value.equals("")) {
                        continue;
                    }
                    float get_csv_value = Float.parseFloat(raw_value);
                    System.out.println(attribute + " " + get_csv_value);
                    context.write(new Text(attribute), new FloatWritable(get_csv_value));
                }
            } else { // UDP Metrics
                float udp_length = Float.parseFloat(record.get(18));

                context.write(new Text("UDP Length"), new FloatWritable(udp_length));
            }

        } catch (Exception e) {
            System.out.println("Error parsing CSV: " + e.getMessage());
        }

    }
}
