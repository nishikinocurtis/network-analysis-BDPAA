package org.example;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;


public class CleanMapper extends
    Mapper<LongWritable, Text, Text, Text>{
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        if (key.equals(new LongWritable(0))) {
            // context.write(new Text(""), new Text(value.toString()));
            return;
        } else {
            try {
                CSVParser parser =
                    CSVParser.parse(value.toString() + "\r\n", CSVFormat.DEFAULT);
                CSVRecord record = parser.getRecords().get(0);
                String protocol = record.get(6);
                System.out.println(protocol);
                if (!protocol.equals("TCP") && !protocol.equals("UDP")) {
                    return;
                } else {
                    context.write(new Text(""), new Text(value.toString()));
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}
