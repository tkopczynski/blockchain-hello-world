package pl.net.kopczynski.blockchain.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

@Component
public class Configuration {

    @Value("${account.privateKey}")
    private String accountPrivateKey;

    @Value("${blockchain.url}")
    private String blockchainUrl;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(blockchainUrl));
    }

    @Bean("walletCredentials")
    public Credentials walletCredentials() {
        return Credentials.create(accountPrivateKey);
    }

    @Bean
    public ContractGasProvider contractGasProvider() {
        return new DefaultGasProvider();
    }
}
