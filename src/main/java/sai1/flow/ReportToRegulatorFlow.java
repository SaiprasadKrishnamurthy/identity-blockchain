package sai1.flow;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;

@InitiatingFlow
public class ReportToRegulatorFlow extends FlowLogic<Void> {

    private final Party regulator;
    private final SignedTransaction finalTransaction;

    public ReportToRegulatorFlow(final Party regulator, final SignedTransaction finalTransaction) {
        this.regulator = regulator;
        this.finalTransaction = finalTransaction;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        FlowSession session = initiateFlow(regulator);
        subFlow(new SendTransactionFlow(session, finalTransaction));
        return null;
    }
}