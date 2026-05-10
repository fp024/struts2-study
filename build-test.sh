#!/bin/sh
set -e

echo "=============================================================================="
echo " [1/2] Root install (Parent-POM + Struts2 JUnit 5 Plugin to local repository)"
echo "=============================================================================="
./mvnw clean install

echo ""
echo "============================================================"
echo " [2/2] Full test suite (struts2-study-parent)"
echo "============================================================"
./mvnw clean test -f struts2-study-parent/pom.xml

echo ""
echo "[SUCCESS] Build and tests completed successfully."
