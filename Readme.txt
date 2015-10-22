Terri's notes to herself, thoughts about what I'm trying to do here.

TO DO: Create a java application which
	- somehow displays the cipres-xml gui
	- when the user presses submit, control returns to the java app, which
	- initially, just dumps out the data from the cipres-xml gui
	- later, submits a job to the rest api


How does the java app display the cipres-xml gui? 

	- It could launch a browser and user some browser control library:

	- Some (chromium based?) framework for creating desktop apps
	with html5, js, css.  I think these are also launching a browser but it is a
	built in browser:
		- node webkit looks good: http://tutorialzine.com/2015/01/your-first-node-webkit-app/.
		One problem with this approach is the size of the application.
		Also, don't know how to integrate this with a java app.

	- JxBrowser:  not open source.  Expensive.

	- JavaFX (apparently this is the new name for Swing) has an embedded WebView that can
	run javascript: http://docs.oracle.com/javafx/2/webview/jfxpub-webview.htm



	- Tried just opening an html file (cipres-xml/index.html) in the browser (firefox).
	Html file loads the javascript.  The onready method gets called and ajax call is made
	but get a CORS error message because the origin is "null".  Seems there is no browser
	independent way to make ajax requests from a file on disk: 
	http://stackoverflow.com/questions/8456538/origin-null-is-not-allowed-by-access-control-allow-origin


First, lets see if I can make cipres-xml work with just an html page and a browser 
(no node.js web server like we're currently doing).



JavaFx
	WebEngine class - provides basic web page functionality: forms, html, dom, javascript
	WebView - encapsulates a WebEngine object.

		WebView browser = new WebView();
		WebEngine engine = browser.getEngine();
		engine.load("file://myfile.html");

	I have something that basically works at showing the gui, so long as the html & js files are on disk.
	Turn this into a java app that lets the user choose a tool, configure it, *submit a job*, *show a list
	of submitted jobs*, *get results*.   First steps:
		- have app take root of web pages as a parameter, load them from that location
		- display alert box
		- get form parameters in java.
		- what do we use for rest communication from java client?  Whatever Mesquite used.  Apache something?
