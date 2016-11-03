package net.jmf.cnormsvc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

@Path("hash")
public class MyResource {
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
        return gson.toJson(new ObjectStats(str));
    }
}
