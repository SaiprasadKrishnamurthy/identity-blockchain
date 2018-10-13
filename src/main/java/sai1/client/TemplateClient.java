package sai1.client;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCClientConfiguration;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.messaging.DataFeed;
import net.corda.core.node.services.Vault;
import net.corda.core.utilities.NetworkHostAndPort;
import org.apache.activemq.artemis.api.core.ActiveMQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import sai1.flow.IdentityCaptureFlow;
import sai1.model.IdentityState;

import java.util.concurrent.ExecutionException;

/**
 * Demonstration of how to use the CordaRPCClient to connect to a Corda Node and
 * stream the contents of the node's vault.
 */
public class TemplateClient {
    private static final Logger logger = LoggerFactory.getLogger(TemplateClient.class);

    public static void main(String[] args) throws ActiveMQException, InterruptedException, ExecutionException {

        final NetworkHostAndPort nodeAddress = NetworkHostAndPort.parse("localhost:10010");
        final CordaRPCClient client = new CordaRPCClient(nodeAddress, CordaRPCClientConfiguration.DEFAULT);

        // Can be amended in the Main file.
        final CordaRPCOps proxy = client.start("user1", "test").getProxy();

        // Grab all existing TemplateStates and all future TemplateStates.
        final DataFeed<Vault.Page<IdentityState>, Vault.Update<IdentityState>> dataFeed = proxy.vaultTrack(IdentityState.class);

        final Observable<Vault.Update<IdentityState>> updates = dataFeed.getUpdates();

//        proxy.startFlowDynamic(IdentityCaptureFlow.class, proxy.wellKnownPartyFromX500Name(new CordaX500Name("PartyB", "New York", "US")), "Some Data Again 1");

        updates.toBlocking().subscribe(update -> update.getProduced()
                .forEach(ts -> System.out.println(" --- "+ts.component1().getData().getIdentityJsonData())));
    }
}