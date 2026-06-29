#!/usr/bin/env bash
# stress_test.sh
#
# Fires NUM_CLIENTS TCP connections at the server as fast as possible.
# Each "client" sends the starter argument (1) and immediately disconnects.
# This simulates 10,000 users trying to connect at the same time.
#
# Usage:
#   ./stress_test.sh [NUM_CLIENTS]   (default: 10000)
#
# WARNING – running this against ServerThreaded may freeze your machine.
# Run it against ServerThreadPool first to confirm it stays stable, then
# (carefully) against ServerThreaded to observe the crash.

NUM_CLIENTS=${1:-10000}
HOST="localhost"
PORT=8080

echo "=========================================="
echo "Stress test: $NUM_CLIENTS connections → $HOST:$PORT"
echo "Start time: $(date)"
echo "=========================================="

# Launch all clients in the background at once
for i in $(seq 1 "$NUM_CLIENTS"); do
    # Each sub-shell: connect, send '1', read one line, close.
    (echo "1"; sleep 0.05) | nc -w 1 "$HOST" "$PORT" >/dev/null 2>&1 &
done

echo "All $NUM_CLIENTS connections fired. Waiting for them to finish..."
wait
echo "=========================================="
echo "Done. End time: $(date)"
echo "=========================================="