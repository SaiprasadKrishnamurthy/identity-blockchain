package sai1.flow;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import sai1.contract.IdentityCaptureContract;
import sai1.model.IdentityState;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/* Our flow, automating the process of updating the ledger.
 * See src/main/java/examples/IAmAFlowPair.java for an example. */
@InitiatingFlow
@StartableByRPC
public class IdentityCaptureFlow extends FlowLogic<Void> {
    private final ProgressTracker progressTracker = new ProgressTracker();
    private final String identityData;
    private final Party targetParty;

    public IdentityCaptureFlow(final Party targetParty, final String identityData) {
        this.identityData = identityData;
        this.targetParty = targetParty;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {

        // We choose our transaction's notary (the notary prevents double-spends).
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

        CordaX500Name watchdogName = new CordaX500Name("Regulator", "Geneva", "CH");
        Party watchDog = getServiceHub().getNetworkMapCache().getPeerByLegalName(watchdogName);

        // We get a reference to our own identity.
        Party sourceParty = getOurIdentity();

        IdentityState identityState = new IdentityState(sourceParty, targetParty, identityData);

        IdentityCaptureContract.SaveIdentity saveIdentityCommand = new IdentityCaptureContract.SaveIdentity(identityData);

        TransactionBuilder transactionBuilder = new TransactionBuilder(notary);
        transactionBuilder.addOutputState(identityState, IdentityCaptureContract.ID);
        transactionBuilder.addCommand(saveIdentityCommand, sourceParty.getOwningKey());
        transactionBuilder.setTimeWindow(getServiceHub().getClock().instant(), Duration.ofSeconds(30));
        transactionBuilder.verify(getServiceHub());

        // We sign the transaction with our private key, making it immutable.
        SignedTransaction partiallySignedTransaction = getServiceHub().signInitialTransaction(transactionBuilder);

        // Counter sign the transaction.
        List<FlowSession> counterSignedSessions = getServiceHub().getNetworkMapCache().getAllNodes()
                .stream()
                .flatMap(nodeInfo -> nodeInfo.getLegalIdentities().stream())
                .map(this::initiateFlow)
                .collect(Collectors.toList());

        SignedTransaction allPartiesSignedTransaction = subFlow(new CollectSignaturesFlow(partiallySignedTransaction, counterSignedSessions));

        // We get the transaction notarised and recorded automatically by the platform.
        SignedTransaction finalTx = subFlow(new FinalityFlow(allPartiesSignedTransaction));
        subFlow(new ReportToRegulatorFlow(watchDog, finalTx));
        return null;
    }
}