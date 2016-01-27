# desktop-cipres
Example javafx/webview application that uses cipres-xml.

This is a desktop application that uses cipres-xml to let the user choose a cipres tool and configure the tool's parameters. Cipres-xml makes an ajax call to retrieve the list of available tools from the CIPRES REST API (CRA) and displays the list. Once the user makes a choice, the selected tool's xml description is retrieved from the CRA and cipres-xml javascript code dynamically creates a form. When the user presses "View", control is transferred to a callback in this java application, that receives the form parameters and displays them on stdout. That's it for the moment, but in the future, we may extend the application to use the [CRA java client library](https://www.phylo.org/restusers/docs/guide.html#SampleCode) to submit the job, list jobs, check their status, download results, cancel jobs.

Note that desktop-cipres installs a handler for both the "View/submit" function and for the File Chooser.  



# Prerequisites 
* cipres-xml: Clone the cipres-xml repo and follow its build instructions.  The important thing here is that the build instructions will cause jquery.js to be downloaded to the location expected by cipres-xml/index.html. (You don't need to npm start the cipres-xml application). Take note of the full path to cipres-xml/index.html, you'll need to pass it, on the command line, to desktop-cipres, as shown below.

* java version 8
* maven 3.2.3 or later


# Build 
* cd desktop-cipres
* mvn clean install

# Run
* cd desktop-cipres/target 
* java -cp \*with\*.jar org.cipres.Main *path_to_cipres-xml*/index.html
