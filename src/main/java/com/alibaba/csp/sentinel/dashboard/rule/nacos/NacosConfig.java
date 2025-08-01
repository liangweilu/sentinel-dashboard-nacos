/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * Nacos配置服务类，主要配置Json和数据对象转换器，以及配置ConfigService的实例
 *
 * @author Eric Zhao
 * @since 1.4.0
 */
@Configuration
public class NacosConfig {

    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder() {
        return list -> {
            return prettyJson(JSON.toJSONString( list));
        };
    }

    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    @Bean
    public Converter<String, List<DegradeRuleEntity>> degradeRuleEntityDecoder() {
        return s -> JSON.parseArray(s, DegradeRuleEntity.class);
    }

    @Bean
    public Converter<List<DegradeRuleEntity>, String> degradeRuleEntityEncoder() {
        return list -> {
            return prettyJson(JSON.toJSONString( list));
        };
    }

    /**
     * 美化Json
     *
     * @param json  json字符串
     * @return  美化后的格式
     */
    private String prettyJson(String json) {
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonArray);
    }

    /**
     * 实例化ConfigService，指定数据源为Nacos，从配置文件读取Nacos配置
     * Nacos配置信息见：{@link NacosPropertiesConfig}
     *
     * @param propertiesConfig  nacos配置信息
     * @return  配置服务实例
     * @throws Exception    .
     */
    @Bean
    public ConfigService nacosConfigService(NacosPropertiesConfig propertiesConfig) throws Exception {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, propertiesConfig.getServerAddr());
        properties.put(PropertyKeyConst.NAMESPACE, propertiesConfig.getNamespace());
        if (StringUtil.isNotEmpty(propertiesConfig.getUsername())) {
            properties.put(PropertyKeyConst.USERNAME, propertiesConfig.getUsername());
        }
        if (StringUtil.isNotEmpty(propertiesConfig.getPassword())) {
            properties.put(PropertyKeyConst.PASSWORD, propertiesConfig.getPassword());
        }
        return ConfigFactory.createConfigService(properties);
    }
}
