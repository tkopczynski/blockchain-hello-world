package pl.net.kopczynski.blockchain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import pl.net.kopczynski.blockchain.contract.HelloWorld;
import pl.net.kopczynski.blockchain.contract.Voting;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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
        String address = deployContract("HelloWorld", this::deployHelloWorldContract);
        return HelloWorld.load(address, web3j, credentials, gasProvider);
    }

    public synchronized Voting votingContract() throws Exception {
        String address = deployContract("Voting", this::deployVotingContract);
        return Voting.load(address, web3j, credentials, gasProvider);
    }

    private String deployContract(String key, Supplier<String> deployContractFunction) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File deployedContractsDatabase = new File(deployedContractsFilePath);

        if (!deployedContractsDatabase.exists()) {
            mapper.writeValue(deployedContractsDatabase, new HashMap<String, String>());
        }

        Map<String, String> deployedContracts = mapper.readValue(deployedContractsDatabase, Map.class);
        int initialSize = deployedContracts.size();
        String address = deployedContracts.computeIfAbsent(key, s -> {
            try {
                return deployContractFunction.get();
            } catch (Exception e) {
                log.error(String.format("%s contract not deployed", key), e);
                return null;
            }
        });

        if (address == null) {
            throw new IllegalStateException(String.format("%s contract not deployed properly", key));
        }

        if (initialSize != deployedContracts.size()) {
            mapper.writeValue(deployedContractsDatabase, deployedContracts);
        }

        return address;
    }

    @SneakyThrows(Exception.class)
    private String deployHelloWorldContract() {
        HelloWorld contract = HelloWorld.deploy(web3j, credentials, gasProvider).send();
        return contract.getContractAddress();
    }

    @SneakyThrows(Exception.class)
    private String deployVotingContract() {
        Voting contract = Voting.deploy(web3j, credentials, gasProvider).send();
        return contract.getContractAddress();
    }
}
