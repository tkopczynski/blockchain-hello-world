package pl.net.kopczynski.blockchain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import pl.net.kopczynski.blockchain.contract.HelloWorld;

import java.io.IOException;

@RestController
public class BlockchainController {

    @Autowired
    private Credentials credentials;

    @Autowired
    private Web3j web3j;

    private String contractAddress;

    @GetMapping("/client")
    public String connect() throws IOException {
        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
        return web3ClientVersion.getWeb3ClientVersion();
    }

    @PostMapping("/deploy")
    public void deploy() throws Exception {
        HelloWorld contract = HelloWorld.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).send();
        contractAddress = contract.getContractAddress();
    }

    @GetMapping("/hello")
    public String hello() throws Exception {
        HelloWorld contract = HelloWorld.load(contractAddress, web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
        return contract.hello().send();
    }
}
