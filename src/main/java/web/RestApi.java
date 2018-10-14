package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import sai1.flow.IdentityCaptureFlow;
import sai1.model.IdentityState;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1")
public class RestApi {
    private final RPCConnection rpcConnection;
    private final SimpMessagingTemplate template;

    @Autowired
    public RestApi(final RPCConnection rpcConnection, final SimpMessagingTemplate template) {
        this.rpcConnection = rpcConnection;
        this.template = template;

        rpcConnection.getOps1().vaultTrack(IdentityState.class).getUpdates().subscribe(identityStateUpdate1 ->
                identityStateUpdate1.getProduced().forEach(identityStateStateAndRef1 -> {
                    ImmutableMap<String, String> one = ImmutableMap.of(
                            "source1", identityStateStateAndRef1.getState().getData().getSourceParty().getName().toString(),
                            "target1", identityStateStateAndRef1.getState().getData().getTargetParty().getName().toString(),
                            "data1", identityStateStateAndRef1.getState().getData().getIdentityJsonData(),
                            "transactionId1", identityStateStateAndRef1.getRef().getTxhash().toString(),
                            "party", "1"
                    );
                    template.convertAndSend("/stompresponse1", one);
                }));
        rpcConnection.getOps2().vaultTrack(IdentityState.class).getUpdates().subscribe(identityStateUpdate2 ->
                identityStateUpdate2.getProduced().forEach(identityStateStateAndRef2 -> {
                    ImmutableMap<String, String> one = ImmutableMap.of(
                            "source2", identityStateStateAndRef2.getState().getData().getSourceParty().getName().toString(),
                            "target2", identityStateStateAndRef2.getState().getData().getTargetParty().getName().toString(),
                            "data2", identityStateStateAndRef2.getState().getData().getIdentityJsonData(),
                            "transactionId2", identityStateStateAndRef2.getRef().getTxhash().toString(),
                            "party", "2"
                    );
                    template.convertAndSend("/stompresponse2", one);
                }));
        rpcConnection.getOps3().vaultTrack(IdentityState.class).getUpdates().subscribe(identityStateUpdate3 ->
                identityStateUpdate3.getProduced().forEach(identityStateStateAndRef3 -> {
                    ImmutableMap<String, String> one = ImmutableMap.of(
                            "source3", identityStateStateAndRef3.getState().getData().getSourceParty().getName().toString(),
                            "target3", identityStateStateAndRef3.getState().getData().getTargetParty().getName().toString(),
                            "data3", identityStateStateAndRef3.getState().getData().getIdentityJsonData(),
                            "transactionId3", identityStateStateAndRef3.getRef().getTxhash().toString(),
                            "party", "3"
                    );
                    template.convertAndSend("/stompresponse3", one);
                }));
    }

    @GetMapping(value = "/capture", produces = "text/plain")
    public String capture(@RequestParam("to") final String to, @RequestParam("data") final String data) {
        CordaX500Name targetX500Name = CordaX500Name.parse(to);
        Party party = rpcConnection.getOps1().wellKnownPartyFromX500Name(targetX500Name);
        rpcConnection.getOps1().startFlowDynamic(IdentityCaptureFlow.class, party, data);
        return "OK";
    }

    @PostMapping(value = "/submit", produces = "text/plain", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String submit(@RequestBody final IdentityCaptureRequest identityCaptureRequest) throws Exception {
        CordaX500Name targetX500Name = CordaX500Name.parse(identityCaptureRequest.getTo());
        Party party = rpcConnection.getOps1().wellKnownPartyFromX500Name(targetX500Name);
        rpcConnection.getOps1().startFlowDynamic(IdentityCaptureFlow.class, party, new ObjectMapper().writeValueAsString(identityCaptureRequest));
        return "OK";
    }

    @GetMapping(value = "/states", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map> states(@RequestParam("party") final String name) throws Exception {
        Vault.Page<IdentityState> identityStatePage = null;
        if (name.equalsIgnoreCase("PartyA")) {
            identityStatePage = rpcConnection.getOps1().vaultQuery(IdentityState.class);
        } else if (name.equalsIgnoreCase("PartyB")) {
            identityStatePage = rpcConnection.getOps2().vaultQuery(IdentityState.class);
        } else {
            identityStatePage = rpcConnection.getOps3().vaultQuery(IdentityState.class);
        }
        return identityStatePage.getStates()
                .stream()
                .map(s -> s.getState().getData())
                .map(s -> s.getIdentityJsonData())
                .map(s -> {
                    try {
                        return new ObjectMapper().readValue(s, Map.class);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
