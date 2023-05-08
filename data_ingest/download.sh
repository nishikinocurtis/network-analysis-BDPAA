#!/bin/bash
mkdir maccdc_ds
curl https://download.netresec.com/pcap/maccdc-2012/maccdc2012_00000.pcap.gz -o maccdc_ds/maccdc2012_00000.pcap.gz
gzip -d maccdc_ds/maccdc2012_00000.pcap.gz

tshark -r maccdc2012_00000.pcap -T fields \
 -E separator=, -E header=y -E occurrence=f \
 -e frame.number -e frame.time_relative -e ip.src -e ip.dst -e ip.ttl -e ip.proto -e _ws.col.Protocol\
 -e tcp.srcport -e tcp.dstport -e tcp.seq -e tcp.time_delta -e tcp.time_relative -e tcp.window_size \
 -e tcp.hdr_len -e tcp.len -e tcp.segments \
 -e udp.srcport -e udp.dstport -e udp.length \
 -e http.request.method -e http.content_length -e http.response.code -e http.response.phrase \
 -E quote=d > output.csv

tar --use-compress-program=pigz -cvf output.tgz output.csv