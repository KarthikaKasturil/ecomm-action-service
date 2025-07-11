package com.secor.ecommactionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class AppConfig
{
    @Autowired
    EurekaDiscoveryClient discoveryClient;

    @Bean
    @Scope("prototype")
    public WebClient productWebClient(WebClient.Builder webClientBuilder)
    {
        List<ServiceInstance> instances = discoveryClient.getInstances("ecomm-product-service");
        //No load balancing algorithm is used here, so we are just taking the first instance
        // you can use load balancing algorithm like round robin or random if you want
        String hostname = instances.get(0).getHost();
        String port = String.valueOf(instances.get(0).getPort());
        return webClientBuilder
                .baseUrl(String.format("http://%s:%s/products", hostname, port))
                .filter(new LoggingWebClientFilter())
                .build();
    }

    @Bean
    @Scope("prototype")
    public WebClient inventoryWebClient(WebClient.Builder webClientBuilder)
    {
        List<ServiceInstance> instances = discoveryClient.getInstances("ecomm-inventory-service");
        //No load balancing algorithm is used here, so we are just taking the first instance
        // you can use load balancing algorithm like round robin or random if you want
        String hostname = instances.get(0).getHost();
        String port = String.valueOf(instances.get(0).getPort());
        return webClientBuilder
                .baseUrl(String.format("http://%s:%s/inventory", hostname, port))
                .filter(new LoggingWebClientFilter())
                .build();
    }

    @Bean
    @Scope("prototype")
    public WebClient customerWebClient(WebClient.Builder webClientBuilder)
    {
        List<ServiceInstance> instances = discoveryClient.getInstances("ecomm-customer-service");
        //No load balancing algorithm is used here, so we are just taking the first instance
        // you can use load balancing algorithm like round robin or random if you want
        String hostname = instances.get(0).getHost();
        String port = String.valueOf(instances.get(0).getPort());
        return webClientBuilder
                .baseUrl(String.format("http://%s:%s/customer", hostname, port))
                .filter(new LoggingWebClientFilter())
                .build();
    }

    @Bean
    @Scope("prototype")
    public WebClient orderWebClient(WebClient.Builder webClientBuilder)
    {
        List<ServiceInstance> instances = discoveryClient.getInstances("ecomm-order-service");
        //No load balancing algorithm is used here, so we are just taking the first instance
        // you can use load balancing algorithm like round robin or random if you want
        String hostname = instances.get(0).getHost();
        String port = String.valueOf(instances.get(0).getPort());
        return webClientBuilder
                .baseUrl(String.format("http://%s:%s/orders", hostname, port))
                .filter(new LoggingWebClientFilter())
                .build();
    }

    @Bean
    @Scope("prototype")
    public WebClient paymentWebClient(WebClient.Builder webClientBuilder)
    {
        List<ServiceInstance> instances = discoveryClient.getInstances("ecomm-payment-service");
        //No load balancing algorithm is used here, so we are just taking the first instance
        // you can use load balancing algorithm like round robin or random if you want
        String hostname = instances.get(0).getHost();
        String port = String.valueOf(instances.get(0).getPort());
        return webClientBuilder
                .baseUrl(String.format("http://%s:%s/payments", hostname, port))
                .filter(new LoggingWebClientFilter())
                .build();
    }

}
