package org.cipres;
import netscape.javascript.JSObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;

public class JavaBridge
{
    public void log(String text)
    {
        System.out.println(text);
    }

	// These should be json strings representing name/value maps
	public void callback(String iparams, String vparams)
	{
		System.out.println("In JavaBridge.callback");
		System.out.println("iparams is: " + iparams);
		System.out.println("vparams is: " + vparams);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> imap =  null;
		Map<String, Object> vmap =  null;
		try
		{
			imap = mapper.readValue(iparams, new TypeReference<Map<String, Object>>(){});
			vmap = mapper.readValue(vparams, new TypeReference<Map<String, Object>>(){});

			System.out.println("showMap iparams");
			showMap(imap);
			System.out.println("showMap vparams");
			showMap(vmap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void showMap(Map<String, Object> m)
	{
		if (m == null) return;

		for (String key : m.keySet())
		{
			System.out.println(key + "=" + m.get(key));
			Object value = m.get(key);
			System.out.println("value is of type: " + m.get(key).getClass().getName());
		}
	}
}
