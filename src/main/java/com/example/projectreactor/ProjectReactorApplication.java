package com.example.projectreactor;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ProjectReactorApplication {

    private Server webServer;

    private Server tcpServer;

    public static void main(String[] args) {
        SpringApplication.run(ProjectReactorApplication.class, args);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void start() throws java.sql.SQLException {
        this.webServer = Server.createWebServer("-webPort", "8081", "-tcpAllowOthers").start();
//        this.tcpServer = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        this.webServer.stop();
//        this.tcpServer.stop();
    }

}
