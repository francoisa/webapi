package net.jmf.cnormsvc;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

@Path("hash")
public class MyResource {
	private static final Logger log = Logger.getLogger(MyResource.class.toString());
	
	private Gson gson;
	
	public MyResource() {
		gson = new Gson();
	}
	
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	@Path("{str}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt(@PathParam("str") String str) {
		String result = gson.toJson(new ObjectStats(str));
		log.info("result: " + result);
        return result;
    }
}
