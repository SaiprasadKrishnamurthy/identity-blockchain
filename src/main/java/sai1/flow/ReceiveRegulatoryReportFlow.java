package sai1.flow;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.node.StatesToRecord;

@InitiatedBy(ReportToRegulatorFlow.class)
public class ReceiveRegulatoryReportFlow extends FlowLogic<Void> {

    private final FlowSession otherSideSession;

    public ReceiveRegulatoryReportFlow(final FlowSession otherSideSession) {
        this.otherSideSession = otherSideSession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        subFlow(new ReceiveTransactionFlow(otherSideSession, true, StatesToRecord.ALL_VISIBLE));
        return null;
    }
}