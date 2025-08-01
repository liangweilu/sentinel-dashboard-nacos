package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * <pre>
 *  发布熔断规则到nacos
 * </pre>
 *
 * @author luliangwei
 **/
@Component("degradeRuleNacosPublisher")
public class DegradeRuleNacosPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

    private static final Logger logger = LoggerFactory.getLogger(DegradeRuleNacosPublisher.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private Converter<List<DegradeRuleEntity>, String> converter;

    @Override
    public void publish(String app, List<DegradeRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (Objects.isNull(rules) || rules.isEmpty()) {
            return;
        }
        logger.info("publish degrade rules to nacos, app={}, rules={}", app, rules);
        configService.publishConfig(app + NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX,
            NacosConfigUtil.GROUP_ID, converter.convert(rules), ConfigType.JSON.getType());
    }
}
