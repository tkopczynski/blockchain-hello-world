package pl.net.kopczynski.blockchain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

@RestController
public class BlockchainController {

    @GetMapping("/client")
    public String connect() throws IOException {
        Web3j web3 = Web3j.build(new HttpService("http://localhost:7545"));
        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
        return web3ClientVersion.getWeb3ClientVersion();
    }
}
