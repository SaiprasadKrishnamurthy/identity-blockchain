package java_bootcamp;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

/* Our contract, governing how our state will evolve over time.
 * See src/main/kotlin/examples/ExampleContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "java_bootcamp.TokenContract";

    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getInputs().size() != 0)
            throw new IllegalArgumentException(" Inputs must be zero");
        if (tx.getOutputs().size() > 1)
            throw new IllegalArgumentException(" Output must be one");
        if (tx.getCommands().size() > 1)
            throw new IllegalArgumentException(" Commands must be one");
        if (!(tx.getOutput(0) instanceof TokenState))
            throw new IllegalArgumentException(" Wrong type");
        if (((TokenState) tx.getOutputs().get(0).getData()).getAmount() <= 0)
            throw new IllegalArgumentException(" Invalid Amount ");
        if (!(tx.getCommand(0).getValue() instanceof Issue))
            throw new IllegalArgumentException(" Invalid Command ");

        TokenState token = (TokenState) tx.getOutput(0);
        if (!tx.getCommand(0).getSigners().contains(token.getParty().getOwningKey())) {
            throw new IllegalArgumentException(" Signature Command ");
        }
    }

    public static class Issue implements CommandData {
    }
}