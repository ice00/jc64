#!/bin/bash

# Resolve script directory
DIR="$(cd "$(dirname "$0")" && pwd)"

# Move to bundle root
cd "$DIR"

# Launch native binary
./bin/jc64
