package com.saber.spring_camel_web_mvc_demo.site.routes;

import com.saber.spring_camel_web_mvc_demo.dto.HelloDto;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.annotations.Component;

@Component("sampleRoute")
public class SampleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer:sample?fixedRate=true&period=3000")
                .log("Hello World");

        from("direct:sayHello")
                .log("Hello World")
                .process(exchange -> {
                    System.out.println("hello");
                    HelloDto helloDto = new HelloDto();
                    helloDto.setMessage("Hello World");
                    exchange.getIn().setBody(helloDto);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE,constant(200));
    }
}
