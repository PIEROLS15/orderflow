#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

podman compose -f "$ROOT_DIR/infra/compose/03-apps.yml" down || true
podman compose -f "$ROOT_DIR/infra/compose/02-postgres.yml" down -v || true
podman compose -f "$ROOT_DIR/infra/compose/01-kafka-server.yml" down || true
podman network exists orderflow-net && podman network rm orderflow-net || true

"$SCRIPT_DIR/start-all.sh"
