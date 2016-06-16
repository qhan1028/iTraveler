import org.codehaus.jackson.map.*;
import org.codehaus.jackson.type.*;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.*;
import java.io.*;

public class ServerTest
{
	public static void main(String args[]) throws IOException
	{
		ServerAverage sa = new ServerAverage();
		
		String command = "";
		String param1, param2, param3;
		Scanner consoleInput = new Scanner(System.in);
		while (true) 
		{
			param1 = param2 = param3 = "";
			command = consoleInput.next();
			if (command.equals("put"))
			{
				param1 = consoleInput.next();
				param2 = consoleInput.next();
				param3 = consoleInput.next();
				sa.inputOneData(param1, param2, Integer.parseInt(param3));
			}
			else if (command.equals("get"))
			{
				param1 = consoleInput.next();
				param2 = consoleInput.next();
				System.out.println(sa.getAverage(param1, param2));
			}
			else if (command.equals("write")) 
			{
				sa.writeToFiles();
			}
			else if (command.equals("quit")) 
			{
				sa.writeToFiles();
				break;
			}
		}
	}
}
