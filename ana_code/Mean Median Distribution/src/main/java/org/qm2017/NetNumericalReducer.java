package org.qm2017;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class NetNumericalReducer
    extends Reducer<Text, FloatWritable, Text, FloatWritable> {

    private void distribution_calc(Text key, ArrayList<Float> values, Context context, int total_count)
        throws IOException, InterruptedException {
        float min_value = Float.MAX_VALUE;
        float max_value = Float.MIN_VALUE;
        // run range approximation
        for (Float value : values) {
            // System.out.println("value: " + value.get());
            if (value < min_value) {
                min_value = value;
            }
            if (value > max_value) {
                max_value = value;
            }
        }
        // divide the range into 100 buckets
        float bucket_size = (max_value - min_value) / 100;
        // Initialize buckets counter and set to all 0
        ArrayList<Integer> buckets = new ArrayList<Integer>(100);
        for (int i = 0; i < 100; i++) {
            buckets.add(0);
        }

        // System.out.println("min_value: " + min_value);
        // System.out.println("max_value: " + max_value);

        // run distribution approximation
        for (Float value : values) {
            int bucket_id = (int) ((value - min_value) / bucket_size);
            if (bucket_id == 100) {
                bucket_id = 99;
            }
            buckets.set(bucket_id, buckets.get(bucket_id) + 1);
        }
        // write distribution to context
        for (int i = 0; i < 100; i++) {
            // calculate the range of the bucket
            float bucket_min = min_value + i * bucket_size;
            float bucket_max = min_value + (i + 1) * bucket_size;
            // write the bucket to context
            context.write(new Text(key.toString() +
                            " Distribution from " +
                            bucket_min + " to " + bucket_max),
                new FloatWritable(buckets.get(i)));
        }

        // approximate median
        int median_count = 0;
        for (int i = 0; i < 100; i++) {
            if (median_count + buckets.get(i) > total_count / 2.0) {
                double ratio = (total_count / 2.0 - median_count) / buckets.get(i);
                ratio *= bucket_size;
                double approx_median = min_value + i * bucket_size + ratio;
                context.write(new Text(key + " Median approx. "),
                              new FloatWritable((float) approx_median));
                return;
            }
            median_count += buckets.get(i);
        }
        context.write(new Text(key + " Median approx. NaN "),
                new FloatWritable(0));
    }

    public void reduce(Text key, Iterable<FloatWritable> values, Context context)
        throws IOException, InterruptedException {
        // calculate mean
        int total_count = 0;
        float mean = 0;
        ArrayList<Float> values_list = new ArrayList<Float>();
        for (FloatWritable value : values) {
            total_count += 1; // count the number of values first, in case of overflow
            values_list.add(value.get());
        }
        for (Float value : values_list) {
            mean += value / total_count;
        }
        context.write(new Text(key.toString() + " Mean "), new FloatWritable(mean));
        // System.out.println(key + " " + String.format("%d", total_count));

        // calculate standard deviation
        double std_dev = 0;
        for (Float value : values_list) {
            std_dev += Math.pow(value - mean, 2) / total_count;
        }
        std_dev = Math.sqrt(std_dev);
        context.write(new Text(key.toString() + " Standard Deviation "), new FloatWritable((float) std_dev));

        // calculate distribution and median
        distribution_calc(key, values_list, context, total_count);
    }
}
