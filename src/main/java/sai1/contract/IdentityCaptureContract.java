package sai1.contract;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import sai1.model.IdentityState;

import java.security.PublicKey;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireThat;

/**
 * Smart contract.
 */
public class IdentityCaptureContract implements Contract {

    public static String ID = "sai1.contract.IdentityCaptureContract";

    @Override
    public void verify(final LedgerTransaction incomingTransaction) throws IllegalArgumentException {

        requireThat(requirements -> {
            requirements.using("There must be 0 input state", incomingTransaction.getInputs().size() == 0);
            requirements.using("There must be 1 output state", incomingTransaction.getOutputs().size() == 1);
            requirements.using("There must be 1 command1", incomingTransaction.getCommands().size() == 1);
            requirements.using("IdentityCaptureRequest Validation Failed miserably!", incomingTransaction.commandsOfType(SaveIdentity.class).get(0).getValue().getIdentity().length() != 4);

            IdentityState identityOutputState = incomingTransaction.outputsOfType(IdentityState.class).get(0);

            // The identity has to be signed by the actual source system.
            List<PublicKey> signedParties = ImmutableList.of(identityOutputState.getSourceParty().getOwningKey());

            requirements.using("Not everyone has signed the transaction", incomingTransaction.getCommand(0).getSigners().equals(signedParties));

            return null;

        });
    }

    public static class SaveIdentity implements CommandData {
        private final String identity;

        public SaveIdentity(String identity) {
            this.identity = identity;
        }

        public String getIdentity() {
            return identity;
        }
    }
}
