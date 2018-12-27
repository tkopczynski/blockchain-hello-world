package pl.net.kopczynski.blockchain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"account.privateKey=abc","blockchain.url=http://localhost:7545"})
public class BlockchainHelloWorldApplicationTests {

    @Test
    public void contextLoads() {
    }

}

