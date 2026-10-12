#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

podman network exists orderflow-net || podman network create orderflow-net
podman compose -f "$ROOT_DIR/infra/compose/01-kafka-server.yml" up -d
podman compose -f "$ROOT_DIR/infra/compose/02-postgres.yml" up -d
podman compose -f "$ROOT_DIR/infra/compose/03-apps.yml" up -d --build

echo "OrderFlow levantado. API: http://localhost:8082/q/swagger-ui | AKHQ: http://localhost:8080"
