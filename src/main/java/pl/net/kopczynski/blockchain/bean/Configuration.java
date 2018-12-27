package pl.net.kopczynski.blockchain.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Component
public class Configuration {

    @Value("${account.privateKey}")
    private String accountPrivateKey;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService("http://localhost:7545"));
    }

    @Bean("walletCredentials")
    public Credentials walletCredentials() {
        return Credentials.create(accountPrivateKey);
    }
}
