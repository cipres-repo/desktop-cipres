# desktop-cipres
Example javafx/webview application that uses cipres-xml.

This will be a desktop application that uses cipres-xml to let the user choose a cipres tool to run and display a form to let the user configure the tool's parameters.  The app will then submit the job to the cipres rest api.  The user can list his submitted jobs, check their status, download results, cancel jobs.

At the moment the only thing implemented is displaying the cipres-xml form.

Build with mvn

Run: cd target; java -cp * org.cipres.Main path_to_index.html
