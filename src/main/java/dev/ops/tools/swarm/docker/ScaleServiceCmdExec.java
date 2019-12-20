package dev.ops.tools.swarm.docker;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.AbstrSyncDockerCmdExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.io.StringWriter;

public class ScaleServiceCmdExec extends AbstrSyncDockerCmdExec<ScaleServiceCmd, Void> implements ScaleServiceCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScaleServiceCmdExec.class);

    public ScaleServiceCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(ScaleServiceCmd command) {
        // we first make a GET call to get the latest info, then we scale and POST
        WebTarget webTarget = getBaseResource().path("/services/{id}").resolveTemplate("id", command.getServiceId());
        String response = webTarget.request().accept(MediaType.APPLICATION_JSON).get(String.class);

        JsonObject serviceObject;
        try (JsonReader reader = Json.createReader(new StringReader(response))) {
            serviceObject = reader.readObject();
        }

        JsonNumber version = (JsonNumber) Json.createPointer("/Version/Index").getValue(serviceObject);
        webTarget = getBaseResource().path("/services/{id}/update")
                .resolveTemplate("id", command.getServiceId())
                .queryParam("version", version.intValue());

        JsonObject spec = serviceObject.getJsonObject("Spec");
        LOGGER.debug("Current service Spec {}", response);

        JsonPointer pointer = Json.createPointer("/Mode/Replicated/Replicas");
        spec = pointer.replace(spec, Json.createValue(command.getReplicas()));

        StringWriter writer = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(writer)) {
            jsonWriter.writeObject(spec);
        }

        LOGGER.debug("Scaled service spec {}", writer.toString());

        Response update = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(writer.toString(), MediaType.APPLICATION_JSON));
        LOGGER.trace("Response: {}", update);
        update.close();

        return null;
    }
}
