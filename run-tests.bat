@echo off
setlocal EnableDelayedExpansion

rem --- locate the junit console jar under lib ---
set "ROOT=%~dp0"
set "JAR="
for %%f in ("%ROOT%lib\junit-platform-console-standalone-"*.jar) do (
  set "JAR=%%~f"
  goto :jar_found
)
:jar_found
if not defined JAR (
  echo [ERROR] junit-platform-console-standalone-*.jar not found under "%ROOT%lib"
  echo Put the JUnit 6 console jar into the lib folder and run again.
  exit /b 1
)

rem --- create output folders ---
if not exist "%ROOT%out\main" mkdir "%ROOT%out\main"
if not exist "%ROOT%out\test" mkdir "%ROOT%out\test"

rem --- collect source files (convert \ to / and quote each line) ---
> "%ROOT%main-sources.txt" (
  for /r "%ROOT%src\main\java" %%F in (*.java) do (
    set "p=%%~fF"
    set "p=!p:\=/!"
    echo "!p!"
  )
)
> "%ROOT%test-sources.txt" (
  for /r "%ROOT%src\test\java" %%F in (*.java) do (
    set "p=%%~fF"
    set "p=!p:\=/!"
    echo "!p!"
  )
)

echo === compile main ===
if exist "%ROOT%main-sources.txt" (
  javac -encoding UTF-8 -d "%ROOT%out\main" @"%ROOT%main-sources.txt"
)

echo === compile tests ===
if exist "%ROOT%test-sources.txt" (
  javac -encoding UTF-8 -d "%ROOT%out\test" -cp "%ROOT%out\main;%JAR%" @"%ROOT%test-sources.txt"
)

echo === run tests ===
rem JUnit 6 (M2) console syntax:
java -jar "%JAR%" execute --classpath "%ROOT%out\main;%ROOT%out\test" --scan-class-path

del /q "%ROOT%main-sources.txt" "%ROOT%test-sources.txt" 2>nul

endlocal



