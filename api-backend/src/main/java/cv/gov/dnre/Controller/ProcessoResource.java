package cv.gov.dnre.Controller;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/processo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProcessoResource {

    @Inject
    @ConfigProperty(name = "myapp.schema.create", defaultValue = "true")
    boolean schemaCreate;

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    @PostConstruct
    void config() {
        if (schemaCreate) {
            initdb();
        }
    }

    private void initdb() {
       client.query("DROP TABLE IF EXISTS processo")
               .flatMap(r -> client.query("CREATE TABLE processo (id SERIAL PRIMARY KEY, code TEXT NOT NULL,description TEXT NOT NULL)"))
               .flatMap(r -> client.query("INSERT INTO processo (id,code,description) VALUES (1,'Kiwi','Description')"))
               .flatMap(r -> client.query("INSERT INTO processo (id,code,description) VALUES (2,'Durian','Description')"))
               .flatMap(r -> client.query("INSERT INTO processo (id,code,description) VALUES (3,'Pomelo','Description')"))
               .flatMap(r -> client.query("INSERT INTO processo (id,code,description) VALUES (4,'Lychee','Description')"))
               .await().indefinitely();
    }


    /*@GET
    @Path("/processolista")
    public Multi<Processo> get() {
        return Processo.findAll(client);
    }*/

    @GET
    @Path("/processolista")
    public Uni<Response> get() {
        return Processo.findAll(client)
                .map(Response::ok)
                .map(Response.ResponseBuilder::build);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingle(@PathParam Long id) {
        return Processo.findById(client, id)
                /*.onItem().apply(processo -> processo != null ? Response.ok(processo) : Response.status(Response.Status.NOT_FOUND))
                .onItem().apply(Response.ResponseBuilder::build);*/
                .map(Response::ok)
                .map(Response.ResponseBuilder::build);
    }

    @POST
    public Uni<Response> create(Processo processo) {
        return processo.save(client)
                .onItem().apply(id -> URI.create("/processo/" + id))
                .onItem().apply(uri -> Response.created(uri).build());
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@PathParam Long id, Processo processo) {
        return processo.update(client)
                .onItem().apply(updated -> updated ? Response.Status.OK : Response.Status.NOT_FOUND)
                .onItem().apply(status -> Response.status(status).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam Long id) {
        return Processo.delete(client, id)
                .onItem().apply(deleted -> deleted ? Response.Status.NO_CONTENT : Response.Status.NOT_FOUND)
                .onItem().apply(status -> Response.status(status).build());
    }
}