package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *  nacos 服务器配置信息，用于配置dashboard连接的nacos地址，端口，namespace，用户名，密码等
 * </pre>
 *
 * @author luliangwei
 **/
@ConfigurationProperties(prefix = "sentinel.dashboard.nacos")
@Configuration
public class NacosPropertiesConfig {

    /**
     * nacos server address, e.g. 192.168.100.1:8848
     */
    private String serverAddr;

    /**
     * nacos namespace, default is public
     */
    private String namespace;

    /**
     * nacos username
     */
    private String username;

    /**
     * nacos password
     */
    private String password;

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
