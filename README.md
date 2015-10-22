# desktop-cipres
Example javafx/webview application that uses cipres-xml.

This will be a desktop application that uses cipres-xml to let the user choose a cipres tool to run and display a form to let the user configure the tool's parameters.  The app will then submit the job to the cipres rest api.  The user can list his submitted jobs, check their status, download results, cancel jobs.

At the moment the only thing implemented is displaying the cipres-xml form.

To build and run this application, first clone the cipres-xml repo and follow its build instructions.  The important thing here is that the build instructions will cause jquery.js to be downloaded to the location expected by cipres-xml/index.html.  Take note of the full path to cipres-xml/index.html, you'll need to pass it, on the command line, to this application, as shown below.

# Build 
* cd desktop-cipres
* mvn clean install

# Run
* cd desktop-cipres/target 
* java -cp * org.cipres.Main *path_to_cipres-xml*/index.html
