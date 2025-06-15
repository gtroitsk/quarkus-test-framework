#!/bin/bash
set -e
export GITHUB_BASE_REF="main"

cp release-checker-tests/mocks/project-next-version-1.7.0-Beta1.yml project.yml
export RELEASES_JSON=$(cat release-checker-tests/data/releases.json)
export LATEST_JSON=$(cat release-checker-tests/data/releases-latest.json | jq -r .tag_name)

echo "🔍 [TEST] Starting new minor cycle after unreleased prerelease (expect success)"

if RELEASES_JSON="$RELEASES_JSON" LATEST_JSON="$LATEST_JSON" bash check-release-version.sh 2>&1 | grep "Starting new minor development cycle after unreleased prerelease"; then
  echo "✅ Test passed"
else
  echo "❌ Test failed"
  exit 1
fi
