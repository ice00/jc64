#!/usr/bin/env bash
set -euo pipefail

# Directory base (cartella che contiene questo script)
BASE_DIR="$(cd "$(dirname "$0")" && pwd)"

# Percorsi relativi dentro dist-run
BIN_DIR="${BASE_DIR}/bin"
CONF_DIR="${BASE_DIR}/conf"

# Se vuoi forzare una JDK/JRE esterna, imposta JDK_DIR qui (opzionale)
# JDK_DIR="/opt/bellsoft-liberica-..."   # uncomment and set if needed

# Se JDK_DIR non è impostato, prova a usare JAVA_HOME o fallback a BIN_DIR (per le .so copiate)
if [ -z "${JDK_DIR:-}" ]; then
  if [ -n "${JAVA_HOME:-}" ]; then
    JDK_DIR="$JAVA_HOME"
  else
    JDK_DIR=""
  fi
fi

# Costruisci LD_LIBRARY_PATH includendo le cartelle lib della JDK (se esiste)
LD_PATHS=()

if [ -n "$JDK_DIR" ] && [ -d "$JDK_DIR/lib" ]; then
  LD_PATHS+=("$JDK_DIR/lib")
fi
if [ -n "$JDK_DIR" ] && [ -d "$JDK_DIR/lib/amd64" ]; then
  LD_PATHS+=("$JDK_DIR/lib/amd64")
fi
if [ -n "$JDK_DIR" ] && [ -d "$JDK_DIR/lib/server" ]; then
  LD_PATHS+=("$JDK_DIR/lib/server")
fi

# aggiungi la cartella bin (dove abbiamo copiato le .so)
LD_PATHS+=("$BIN_DIR")

# unisci i path
OLD_LD="${LD_LIBRARY_PATH:-}"
IFS=":" read -r -a _tmp <<< "${OLD_LD:-}"
# Prepend our paths
NEW_LD="$(IFS=:; echo "${LD_PATHS[*]}")"
if [ -n "$OLD_LD" ]; then
  NEW_LD="${NEW_LD}:$OLD_LD"
fi
export LD_LIBRARY_PATH="$NEW_LD"

# Imposta JAVA_HOME per le librerie che lo richiedono (se JDK_DIR è definito)
if [ -n "${JDK_DIR}" ]; then
  export JAVA_HOME="$JDK_DIR"
fi

# Stampa diagnostica utile
echo "BASE_DIR=$BASE_DIR"
echo "BIN_DIR=$BIN_DIR"
echo "CONF_DIR=$CONF_DIR"
echo "JAVA_HOME=${JAVA_HOME:-<not set>}"
echo "LD_LIBRARY_PATH=$LD_LIBRARY_PATH"

# Assicura permessi esecuzione sul binario
chmod +x "$BIN_DIR/jc64" || true

# Esegui il binario dalla working dir corretta
cd "$BASE_DIR"
exec "$BIN_DIR/jc64" "$@"
