package org.qm2017;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class NetTextualReducer
    extends Reducer<Text, LongWritable, Text, LongWritable> {
    public void reduce(Text key, Iterable<LongWritable> values, Context context)
        throws IOException, InterruptedException {
        long total_count = 0;
        for (LongWritable value : values) {
            total_count += value.get();
        }
        context.write(key, new LongWritable(total_count));
    }
}
