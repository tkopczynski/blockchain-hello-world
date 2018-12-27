package pl.net.kopczynski.blockchain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import pl.net.kopczynski.blockchain.contract.HelloWorld;

import java.io.File;
import java.util.Map;

@Service
public class DeploymentService {

    private static final Logger log = LoggerFactory.getLogger(DeploymentService.class);

    private static final String deployedContractsFilePath = "/tmp/deployedContracts";

    @Autowired
    private Web3j web3j;

    @Autowired
    private Credentials credentials;

    @Autowired
    private ContractGasProvider gasProvider;

    public synchronized HelloWorld helloWorldContract() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> deployedContracts = mapper.readValue(new File(deployedContractsFilePath), Map.class);
        String address = deployedContracts.computeIfAbsent("HelloWorld", s -> {
            try {
                return deployHelloWorldContract();
            } catch (Exception e) {
                log.error("HelloWorld contract not deployed", e);
                return null;
            }
        });

        if (address == null) {
            throw new IllegalStateException("HelloWorld contract not deployed properly");
        }

        mapper.writeValue(new File(deployedContractsFilePath), deployedContracts);

        return HelloWorld.load(address, web3j, credentials, gasProvider);
    }

    private String deployHelloWorldContract() throws Exception {
        HelloWorld contract = HelloWorld.deploy(web3j, credentials, gasProvider).send();
        return contract.getContractAddress();
    }
}
