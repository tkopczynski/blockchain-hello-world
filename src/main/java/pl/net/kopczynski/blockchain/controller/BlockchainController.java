package pl.net.kopczynski.blockchain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import pl.net.kopczynski.blockchain.contract.HelloWorld;
import pl.net.kopczynski.blockchain.contract.Voting;
import pl.net.kopczynski.blockchain.service.DeploymentService;

import java.io.IOException;
import java.math.BigInteger;

@RestController
public class BlockchainController {

    @Autowired
    private Web3j web3j;

    @Autowired
    private DeploymentService deploymentService;

    @GetMapping("/client")
    public String connect() throws IOException {
        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
        return web3ClientVersion.getWeb3ClientVersion();
    }

    @GetMapping("/hello")
    public String hello() throws Exception {
        HelloWorld contract = deploymentService.helloWorldContract();
        return contract.hello().send();
    }

    @PostMapping("/vote")
    public void vote(@RequestParam("candidateId") Long candidateId) throws Exception {
        Voting voting = deploymentService.votingContract();
        voting.vote(BigInteger.valueOf(candidateId)).send();
    }

    @GetMapping("/votes")
    public BigInteger votes(@RequestParam("candidateId") Long candidateId) throws Exception {
        Voting voting = deploymentService.votingContract();
        return voting.candidates(BigInteger.valueOf(candidateId)).send();
    }
}
