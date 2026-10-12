#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

podman compose -f "$ROOT_DIR/infra/compose/03-apps.yml" down
podman compose -f "$ROOT_DIR/infra/compose/02-postgres.yml" down
podman compose -f "$ROOT_DIR/infra/compose/01-kafka-server.yml" down

echo "OrderFlow detenido. Los datos de PostgreSQL se conservaron."
