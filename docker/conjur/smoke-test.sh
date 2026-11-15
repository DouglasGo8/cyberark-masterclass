#!/usr/bin/env bash
# Smoke test: authenticate as the host identity (API key) and read a variable
# through the TLS proxy. This is the same flow the Camel cyberark-vault component performs.
# Run from anywhere: bash docker/conjur/smoke-test.sh
set -euo pipefail
cd "$(dirname "$0")"

URL="${CONJUR_URL:-https://localhost:8443}"
ACCOUNT="${CONJUR_ACCOUNT:-myConjurAccount}"
HOST_ID="${CONJUR_HOST_ID:-poc/app}"
VAR_ID="${CONJUR_VAR_ID:-poc/app/demo-secret}"
CACERT="${CONJUR_CACERT:-tls/conjur.crt}"

# The host API key was written by "conjur policy load" into poc_app_data (git-ignored)
API_KEY="$(grep -o '"api_key": *"[^"]*"' poc_app_data | head -n1 | cut -d'"' -f4)"
[ -n "$API_KEY" ] || { echo "api_key not found in poc_app_data" >&2; exit 1; }

# URL-encode the slashes of ids
enc() { printf '%s' "${1//\//%2F}"; }

# 1) Authenticate: API key -> short-lived access token (base64 form)
TOKEN="$(curl -sS --fail --cacert "$CACERT" \
  -H 'Accept-Encoding: base64' \
  --data "$API_KEY" \
  "$URL/authn/$ACCOUNT/host%2F$(enc "$HOST_ID")/authenticate")"
echo "authenticated as host/$HOST_ID"

# 2) Read the variable with the token
printf 'value of %s: ' "$VAR_ID"
curl -sS --fail --cacert "$CACERT" \
  -H "Authorization: Token token=\"$TOKEN\"" \
  "$URL/secrets/$ACCOUNT/variable/$(enc "$VAR_ID")"
echo