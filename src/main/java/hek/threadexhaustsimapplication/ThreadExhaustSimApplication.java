package hek.threadexhaustsimapplication;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ThreadExhaustSimApplication {
    private AbstractProtocol<?> protocol;

    public static void main(String[] args) {
        SpringApplication.run(ThreadExhaustSimApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol<?> p) {
                protocol = p;
                System.out.println("Tomcat maxThreads = " + protocol.getMaxThreads());
                System.out.println("Tomcat acceptCount = " + protocol.getAcceptCount());
                System.out.println("Tomcat minSpareThreads = " + protocol.getMinSpareThreads());
            }
        });
    }

    public AbstractProtocol<?> getProtocol() {
        return protocol;
    }

}
