cd %CD%

call mvn -Dtest=FirstGroup,SecondGroup,ThirdGroup test surefire-report:report site

pause






