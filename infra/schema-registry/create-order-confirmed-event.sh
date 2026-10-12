#!/usr/bin/env bash
set -euo pipefail

curl -X POST http://localhost:8081/subjects/order-confirmed-topic-value/versions \
  -H 'Content-Type: application/vnd.schemaregistry.v1+json' \
  --data "{\"schema\":$(jq -Rs . < OrderConfirmedEvent.avsc)}"
