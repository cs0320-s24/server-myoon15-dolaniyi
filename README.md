> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details
This is a server application that uses a webAPI for data retrieval and search. It supports both CSV loading, viewing, and searching by specifying a filepath. The user can search a CSV by a specified column, or all, and returns the matching rows. 
This project also accesses ACS database to recover broadband access by state and county. Matches can be specified by county, or all. 

# Design Choices
We created CSVLoadHandler, CSVViewHandler, and CSVSearchHandler to handle the endpoints for the CSV segment of the project. It connects to the CSVParser backend where the file is parsed line-by-line, and any matches (if specified) are stored. This data gets serialized into Json format with a Moshi adapter class SerializeUtility.java and added to the responseMap to be printed on localhost.

For the Broadband segment, we created BroadbandHandler, APIBroadbandData, Response, and CachedBroadband to handle the operations. When requested, BroadbandHandler asks the cache (CachedBroadband) for data, and if it doesn't exist, it requests APIBroadbandData to access the API.

# Errors/Bugs
None we know of.

# Tests
CSV:
Element exists any column; element exists in specific column; element does not exist; detect malformed CSV; detect normal CSV with nested commas; inaccessible data source; not CSV filetype; file does not exist; ill-formed request; column 

Broadband:
Works with normal parameters; returns correct error for an invalid state; returns correct error for an invalid county; returns correct response for ill-formed parameters

# How to
Load CSV: localhost.3231/loadcsv?filepath=(...)
View CSV: localhost.3231/viewcsv
Search CSV: localhost.3231/searchcsv?word=(...)&column=(...)

Broadband: localhost.3231/broadband?state=(...)&county=(...)
Note: state and county must be encoded. Ex: (state 06: California, county 033: Riverside)
