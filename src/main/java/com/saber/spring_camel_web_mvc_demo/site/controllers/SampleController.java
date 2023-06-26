package com.saber.spring_camel_web_mvc_demo.site.controllers;

import com.saber.spring_camel_web_mvc_demo.dto.HelloDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
@Slf4j
public class SampleController {

    @Autowired
    private CamelContext camelContext;

    @RequestMapping(value = "/world",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
    public HelloDto helloWorld(){
        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        Exchange responseExchange = producerTemplate.send("direct:sayHello", exchange -> {
        });
        Integer statusCode = responseExchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);
        log.info("statusCode ===> {}",statusCode);
        String body = responseExchange.getIn().getBody(String.class);
        log.info("body ===> {}",body);
        HelloDto helloDto = new HelloDto();
        helloDto.setMessage("Hello World");
        return  helloDto;
    }
}
