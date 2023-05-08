package org.qm2017;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Mapper;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;

public class NumericalCleanerMapper
    extends Mapper<LongWritable, Text, Text, Text> {

    // private final HashMap<String, Integer> attributes = new HashMap<String, Integer>();
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        try {
            CSVParser parser = CSVParser.parse(value.toString(), CSVFormat.DEFAULT);
            CSVRecord record = parser.getRecords().get(0);
            String protocol = record.get(6);
            if (protocol.equals("TCP")) {
//                attributes.put("A TCP Src Port", 7);
//                attributes.put("B TCP Dst Port", 8);
//                attributes.put("C TCP Seq Number", 9);
//                attributes.put("D TCP Time Delta", 10);
//                attributes.put("E TCP Time Relative", 11);
//                attributes.put("F TCP Window Size", 12);
//                attributes.put("TCP Length", 14);
//                attributes.put("TCP Segments", 15);
                ArrayList<Integer> positions = new ArrayList<Integer>();
                positions.add(7);
                positions.add(8);
                positions.add(9);
                positions.add(10);
                positions.add(11);
                positions.add(12);
                positions.add(14);
                positions.add(15);

                // re-processing all numerical fields as double, for KMeans clustering, generating a cleaned csv line

                ArrayList<Double> numerical_values = new ArrayList<Double>();
                for (Integer position : positions) {
                    String raw_value = record.get(position);
                    if (raw_value.equals("")) {
                        numerical_values.add(0.0);
                        continue;
                    }
                    double get_csv_value = Double.parseDouble(raw_value);
                    numerical_values.add(get_csv_value);
                }

                StringBuilder numerical_values_string = new StringBuilder();
                for (int i = 0; i < numerical_values.size(); i++) {
                    numerical_values_string.append(numerical_values.get(i));
                    if (i != numerical_values.size() - 1) {
                        numerical_values_string.append(",");
                    }
                }

                context.write(new Text(""), new Text(numerical_values_string.toString()));
            }
        }
        catch (Exception e) {
            System.out.println("Error parsing CSV: " + e.getMessage());
        }
    }
}
