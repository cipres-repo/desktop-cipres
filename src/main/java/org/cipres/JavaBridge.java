package org.cipres;
import netscape.javascript.JSObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import javafx.stage.Stage;
import javafx.stage.FileChooser; 
import java.io.File;

public class JavaBridge
{
	Stage stage;

	public JavaBridge(Stage stage)
	{
		this.stage = stage;
	}

    public void log(String text)
    {
        System.out.println(text);
    }

	// These should be json strings representing name/value maps
	public boolean callback(String toolID, String iparams, String vparams)
	{
		System.out.println("In JavaBridge.callback");
		System.out.println("toolID is: " + toolID);
		System.out.println("iparams is: " + iparams);
		System.out.println("vparams is: " + vparams);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> imap =  null;
		Map<String, Object> vmap =  null;
		try
		{
			imap = mapper.readValue(iparams, new TypeReference<Map<String, Object>>(){});
			vmap = mapper.readValue(vparams, new TypeReference<Map<String, Object>>(){});

			System.out.println("Converted JSON Input Data Parameters to java:");
			showMap(imap);
			System.out.println("Converted JSON Visible Parameters to java:");
			showMap(vmap);
			System.out.println("java callback returning false");
			return false; 
		}
		catch (Exception e)
		{
			System.out.println("java callback caught exception, returning false: " + e.toString());
			e.printStackTrace();
			return false;
		}
	}

    public String fileChooser()
	{
		System.out.println("In java fileChooser()");
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(stage);
		if (file != null)
		{
			System.out.println("Selected " + file.getAbsolutePath());
			return file.getAbsolutePath();
		}
		System.out.println("Nothing selected");
		return null;
	}

	private void showMap(Map<String, Object> m)
	{
		if (m == null) return;

		for (String key : m.keySet())
		{
			//System.out.println(key + "=" + m.get(key) + ". Value is of type " +  m.get(key).getClass().getName());
			System.out.println(key + "=" + m.get(key) );
		}
	}
}
