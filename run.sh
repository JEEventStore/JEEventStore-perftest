ab -k \
	-u content.json \
	-H "Content-Type: application/json" \
	-H "Accept: application/json"  \
	-c 16 -n $1 http://127.0.0.1:8080/JEEventStore-perftest-web/test/es/random_stream

