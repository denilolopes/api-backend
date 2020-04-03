package cv.gov.dnre.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class Processo {

    @NotBlank(message="id may not be blank")
    public Long id;

    @Min(message="Minimo é 0", value=1)
    public String code;

    @NotBlank(message="description may not be blank")
    public String description;

    public Processo (String code) {
        this.code = code;
    }

    public Processo( Long id, String code ) {
        this.id = id;
        this.code = code;
    }

    /*public static Multi<Processo> findAll(PgPool client) {
        return client.query("SELECT id, code FROM Processo ORDER BY code ASC")
                .onItem().produceMulti(set -> Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false)))
                .onItem().apply(Processo::from);
    }*/

    public static Uni<List<Processo>> findAll(PgPool client) {
        return client.query("SELECT id, code,description FROM Processo ORDER BY code ASC")
                .map(pgRowSet -> {
                    List<Processo> list = new ArrayList<>(pgRowSet.size());
                    for (Row row : pgRowSet) {
                        list.add(from(row));
                    }
                    return list;
                });
    }

    public static Uni<Processo> findById(PgPool client, Long id) {
        return client.preparedQuery("SELECT id, code FROM Processo WHERE id = $1", Tuple.of(id))
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Long> save(PgPool client) {
        return client.preparedQuery("INSERT INTO Processo (code) VALUES ($1) RETURNING (id)", Tuple.of(code))
                .onItem().apply(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public Uni<Boolean> update(PgPool client) {
        return client.preparedQuery("UPDATE Processo SET code = $1 WHERE id = $2", Tuple.of(code, id))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> delete(PgPool client, Long id) {
        return client.preparedQuery("DELETE FROM Processo WHERE id = $1", Tuple.of(id))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private static Processo from(Row row) {
        return new Processo(row.getLong("id"), row.getString("code"));
    }

}
