package web;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RPCConnection {

    @Value("${config.rpc.host1}")
    String host1;
    @Value("${config.rpc.username}")
    String username;
    @Value("${config.rpc.password}")
    String password;
    @Value("${config.rpc.port1}")
    int rpcPort1;

    @Value("${config.rpc.host2}")
    String host2;
    @Value("${config.rpc.port2}")
    int rpcPort2;

    @Value("${config.rpc.host3}")
    String host3;
    @Value("${config.rpc.port3}")
    int rpcPort3;

    private CordaRPCOps cordaRPCOps1;
    private CordaRPCOps cordaRPCOps2;
    private CordaRPCOps cordaRPCOps3;

    @PostConstruct
    public void init() {
        cordaRPCOps1 = new CordaRPCClient(new NetworkHostAndPort(host1, rpcPort1)).start(username, password).getProxy();
        cordaRPCOps2 = new CordaRPCClient(new NetworkHostAndPort(host2, rpcPort2)).start(username, password).getProxy();
        cordaRPCOps3 = new CordaRPCClient(new NetworkHostAndPort(host3, rpcPort3)).start(username, password).getProxy();
    }

    public CordaRPCOps getOps1() {
        return cordaRPCOps1;
    }

    public CordaRPCOps getOps2() {
        return cordaRPCOps2;
    }

    public CordaRPCOps getOps3() {
        return cordaRPCOps3;
    }
}
