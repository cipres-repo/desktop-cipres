package org.cipres;

import javafx.application.Application;
import javafx.application.Application.Parameters;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEvent;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.cipres.JavaBridge;
import java.io.File;
import java.nio.file.Paths;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Main extends Application 
{
	String webRoot;

	private void initWebRoot() throws Exception
	{
		List<String> args = getParameters().getUnnamed();
		if (args.isEmpty())
		{
			System.err.println("missing required argument: page to load");
			throw new Exception("");
		}
		File f = new File(args.get(0));
		webRoot = "file://" + f.getAbsolutePath();
		if (!f.isFile())
		{
			System.err.println("required argument, page to load, not a valid file: " + webRoot);
			throw new Exception("");
		}
	}

		

	@Override
	public void start(final Stage stage) 
		throws Exception 
	{
		initWebRoot();

		final WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();

		Scene scene = new Scene(browser, 1000, 600, Color.web("#666970"));
		stage.setScene(scene);


		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() 
		{
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) 
			{
				if (newState == Worker.State.SUCCEEDED) 
				{
					stage.setTitle(webEngine.getLocation());
				}
			}
		});

		// This lets console.log messages go to stdout
		// Enables an upcall from javascript (console.log) to java code.
		/*
			https://blogs.oracle.com/javafx/entry/communicating_between_javascript_and_javafx
			https://docs.oracle.com/javafx/2/api/javafx/scene/web/WebEngine.html

			You can pass arbitrary javascript to the JS engine of a WebEngine object using
			WebEngine.executeScript().  It returns the javascript retval converted to a java type.  For
			primitives, like integer it returns Integer or for  string -> String, but most JavaScript objects
			will be wrapped in a JSObject.  Thus webEngine.executeScript("window") returns the javascript
			current window object, wrapped in a JSOBject, a class that lets us manipulate the js object in 
			java.

			This method gets called each time the worker thread that loads the content changes.  It seems
			to get called 3 times, SCHEDULED, RUNNING and SUCCEEDED.  Not sure we need it called more
			than once.
		*/
		webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->
		{
			System.out.println("Setup console log, state is " + newValue);
			// Get the current js window, wrapped in a JSObject
			JSObject window = (JSObject) webEngine.executeScript("window");


			// Inject a member into the window, it's name is "java", it's value is the new bridge object.
			JavaBridge bridge = new JavaBridge();
			window.setMember("java", bridge);

			// Have the js engine run a script that changes the built in console.log function
			// to point to a new fn, defined here as calling our bridge's log() method.
			webEngine.executeScript("console.log = function(message)\n" + "{\n" + "    java.log(message);\n" + "};");
			webEngine.executeScript("theCallback = function(iparams, vparams)\n" + "{\n" + "    java.callback(iparams, vparams);\n" + "};");

		});

		// This makes javascript alert() popup an alert dialog window.
		webEngine.setOnAlert((WebEvent<String> wEvent) -> 
		{ 
			System.out.println("Alert: " + wEvent.getData()); 
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Alert");
			alert.setContentText(wEvent.getData());
			alert.showAndWait();
		});


		System.out.println("Loading " + webRoot);

		// Loading happens on a background thread.  This returns right away.
		webEngine.load(webRoot);

		stage.show();
	}


	public static void main(String[] args) 
	{
		launch(args);
	}
}

