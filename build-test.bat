@echo off
setlocal

echo ==============================================================================
echo  [1/2] Root install (Parent-POM + Struts2 JUnit 5 Plugin to local repository)
echo ==============================================================================
call mvnw.cmd clean install
if %ERRORLEVEL% neq 0 (
    echo.
    echo [FAILED] mvnw clean install failed. Aborting.
    exit /b %ERRORLEVEL%
)

echo.
echo ============================================================
echo  [2/2] Full test suite (struts2-study-parent)
echo ============================================================
call mvnw.cmd clean test -f struts2-study-parent/pom.xml
if %ERRORLEVEL% neq 0 (
    echo.
    echo [FAILED] Tests failed.
    exit /b %ERRORLEVEL%
)

echo.
echo [SUCCESS] Build and tests completed successfully.
endlocal
