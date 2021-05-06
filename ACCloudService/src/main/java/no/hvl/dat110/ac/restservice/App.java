package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations describined in the project description
		
		//receive json message, status
		//return json AccessEntry
		post("/accessdevice/log", (req, res) ->{
			Gson gson = new Gson();
			AccessMessage msg = gson.fromJson(req.body(), AccessMessage.class);
			
			int id = accesslog.add(msg.getMessage());
			
			return gson.toJson(accesslog.get(id));
		});
		
		//return json log of all entries
		get("/accessdevice/log/", (req, res) ->{
			
			return accesslog.toJson();
		});
		
		// TODO: return single result from log
		get("/accessdevice/log/:id", (req, res) ->{
			Gson gson = new Gson();
			
			int id = Integer.parseInt(req.params("id"));
			
			return gson.toJson(accesslog.get(id));
		});
		
		//Updating the access code
		//returning json of new code
		put("/accessdevice/code", (req, res) ->{
			Gson gson = new Gson();
			AccessCode newCode = gson.fromJson(req.body(), AccessCode.class);
			
			accesscode.setAccesscode(newCode.getAccesscode());
			
			return gson.toJson(accesscode);
		});
		
		//returning curent code in json
		get("/accessdevice/code", (req, res) ->{
			Gson gson = new Gson();
			
			return gson.toJson(accesscode);
		});
		
		//deletes all entries in log
		//return the empty access log
		delete("/accessdevice/log", (req, res) ->{
			accesslog.clear();
			
			return accesslog.toJson();
		});
		
    }
    
}
