import org.codehaus.jackson.map.*;
import org.codehaus.jackson.type.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;

public class ServerAverage {

	private final String DIR = "./data/";
	private final int MAXOBJ = 6;
	private final boolean AVERAGE = false;
	private final boolean COUNT = true;
	private final String [] object_str = {"eat",   "live",   "entertain",   "transport",   "buy",   "other", 
										  "eat_c", "live_c", "entertain_c", "transport_c", "buy_c", "other_c"};
	// hashmaps
	private HashMap<String, String> eat,   live,   entertain,   transport,   buy,   other;
	private HashMap<String, String> eat_c, live_c, entertain_c, transport_c, buy_c, other_c;
	private ObjectMapper mapper;
	private TypeReference<HashMap<String, String>> typeref;

	protected HashMap<String, String> findMap(String obj, boolean count)
	{
		if (obj.equals("buy")) return count? buy_c : buy;
		if (obj.equals("eat")) return count? eat_c : eat;
		if (obj.equals("live")) return count? live_c : live;
		if (obj.equals("other")) return count? other_c : other;
		if (obj.equals("entertain")) return count? entertain_c : entertain;
		if (obj.equals("transport")) return count? transport_c : transport;
		return null;
	}

	// constructor
	public ServerAverage() throws IOException 
	{	
		// construct mapper
		System.out.print(">>> constructing mapper...");
		typeref = new TypeReference<HashMap<String, String>>() {};
		mapper = new ObjectMapper();
		System.out.println("OK");
		
		// check if directory exists
		File directory = new File("data");
		if (!directory.exists()) directory.mkdirs();

		System.out.print(">>> reading datas...");
		String [] json = new String[MAXOBJ * 2];
		for (int i = 0 ; i < MAXOBJ * 2 ; i++) 
		{
			// check if average files exist
			File file = new File(DIR + object_str[i]);
			if (!file.exists())	file.createNewFile();
			// read from files
			BufferedReader br = new BufferedReader(new FileReader(DIR + object_str[i]));
			json[i] = br.readLine();
			if (json[i] == null) json[i] = "{}";
		}
		System.out.println("OK");

		// put into hashmap
		System.out.print(">>> building hashmaps...");
		eat = mapper.readValue(json[0], typeref);
		live = mapper.readValue(json[1], typeref);
		entertain = mapper.readValue(json[2], typeref);
		transport = mapper.readValue(json[3], typeref);
		buy = mapper.readValue(json[4], typeref);
		other = mapper.readValue(json[5], typeref);
		eat_c = mapper.readValue(json[6], typeref);
		live_c = mapper.readValue(json[7], typeref);
		entertain_c = mapper.readValue(json[8], typeref);
		transport_c = mapper.readValue(json[9], typeref);
		buy_c = mapper.readValue(json[10], typeref);
		other_c = mapper.readValue(json[11], typeref);
		System.out.println("OK");
	}

	public float getAverage(String obj, String subobj) throws IOException
	{
		HashMap<String, String> map = findMap(obj, AVERAGE);
		if (map == null || map.get(subobj) == null) return 0f;
		return Float.parseFloat(map.get(subobj));
	}

	public void inputArrayList(String obj, ArrayList<String> item, ArrayList<Integer> value) throws IOException
	{
		HashMap<String, String> map = findMap(obj, AVERAGE);
		HashMap<String, String> map_c = findMap(obj, COUNT);

		for (int i = 0 ; i < item.size() ; i++)
		{
			String subobj = item.get(i);
			float new_value = (value.get(i)).floatValue();
			
			if (map.get(subobj) == null) 
			{
				map.put(subobj, Float.toString(new_value) );
				map_c.put(subobj, Integer.toString(1) );
			}
			else 
			{
				int new_count = Integer.parseInt(map_c.get(subobj)) + 1;
				float new_average = (Float.parseFloat(map.get(subobj)) * (new_count - 1) + new_value) / new_count;
				map.put(subobj, Float.toString(new_average) );
				map_c.put(subobj, Integer.toString(new_count) );
			}
		}
	}

	public void inputOneData(String obj, String subobj, int new_value) throws IOException
	{
		HashMap<String, String> map = findMap(obj, AVERAGE);
		HashMap<String, String> map_c = findMap(obj, COUNT);
		
		if (map.get(subobj) == null) 
		{
			map.put(subobj, Float.toString(new_value) );
			map_c.put(subobj, Integer.toString(1) );
		}
		else 
		{
			int new_count = Integer.parseInt( map_c.get(subobj) ) + 1;
			float new_average = (Float.parseFloat( map.get(subobj) ) * (new_count - 1) + new_value) / new_count;
			map.put(subobj, Float.toString(new_average));
			map_c.put(subobj, Integer.toString(new_count));
		}
	}

	public void writeToFiles() throws IOException
	{
		System.out.print(">>> writing to files...");
		for (int i = 0 ; i < MAXOBJ * 2 ; i++)
		{
			HashMap<String, String> map = findMap(object_str[i % MAXOBJ], (i < MAXOBJ)? AVERAGE : COUNT);
			BufferedWriter bw = new BufferedWriter(new FileWriter(DIR + object_str[i]));
			bw.write( mapper.writeValueAsString(map) );
			bw.flush();
		}
		System.out.println("OK");
	}
}
