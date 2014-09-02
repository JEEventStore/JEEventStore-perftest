package org.jeeventstore.tests.performance;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.jeeventstore.ConcurrencyException;
import org.jeeventstore.DuplicateCommitException;
import org.jeeventstore.EventStore;
import org.jeeventstore.ReadableEventStream;
import org.jeeventstore.StreamNotFoundException;
import org.jeeventstore.WritableEventStream;
import org.jeeventstore.util.IteratorUtils;

@Stateless
@Path("/es")
public class TestService {

    @EJB
    private EventStore eventStore;

    private static Random random = new Random();

    @PUT
    @Path("random_stream")
    public String putStream(String data)  {
        String bucketId = new Long(random.nextLong()).toString();
        String realstreamId = new Long(random.nextLong()).toString();
        WritableEventStream es = eventStore.createStream(bucketId, realstreamId);
        es.append(data);
        try {
            es.commit(realstreamId);
        } catch (DuplicateCommitException | ConcurrencyException e) {
//            System.out.println("Error: caught " + e.getClass());
            throw new RuntimeException(e);
        }
        return "ok";
    }

    @GET
    @Path("stream/{bucketId}/{streamId}")
    @Produces({"application/json"})
    public List<Serializable> getStream(@PathParam("bucketId") String bucketId,
            @PathParam("streamId") String streamId, @Context final HttpServletResponse response) {
        try {
            ReadableEventStream stream = eventStore.openStreamForReading(bucketId, streamId);
            List<Serializable> events = IteratorUtils.toList(stream.events());
            return events;
        } catch (StreamNotFoundException e) {
            response.setStatus(Response.Status.NOT_FOUND.getStatusCode());
            return null;
        }
    }
}
