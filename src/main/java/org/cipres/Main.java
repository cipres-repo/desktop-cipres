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
		webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->
		{
			JSObject window = (JSObject) webEngine.executeScript("window");
			JavaBridge bridge = new JavaBridge();
			window.setMember("java", bridge);
			webEngine.executeScript("console.log = function(message)\n" + "{\n" + "    java.log(message);\n" + "};");
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
		webEngine.load(webRoot);

		stage.show();
	}


	public static void main(String[] args) 
	{
		launch(args);
	}
}

/*
It seems this must be a public class defined in another file.
class JavaBridge
{
    public void log(String text)
    {
        System.out.println(text);
    }
}
*/

