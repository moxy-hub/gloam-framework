package com.gloamframework.cloud.sentinel.limit;

import cn.hutool.core.map.MapUtil;
import com.alibaba.csp.sentinel.slots.block.flow.ClusterFlowConfig;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.gloamframework.cloud.sentinel.properties.GloamSentinelProperties;
import com.gloamframework.cloud.sentinel.properties.limit.LimitRuleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.ArrayList;
import java.util.List;

/**
 * 限流规则配置
 *
 * @author 晓龙
 */
@Configurable
public class LimitRuleConfigure {

    @Autowired
    public void configure(GloamSentinelProperties sentinelProperties) {
        LimitRuleProperties limit = sentinelProperties.getLimit();
        List<FlowRule> rules = new ArrayList<>();
        FlowRule defaultRule = this.assembleRule(limit.getDefaultSymbol(), limit.getDefaultRule());
        rules.add(defaultRule);
        if (MapUtil.isNotEmpty(limit.getRule())) {
            limit.getRule().forEach((resourceName, rule) -> {
                rules.add(this.assembleRule(resourceName, rule));
            });
        }
        FlowRuleManager.loadRules(rules);
    }

    private FlowRule assembleRule(String resource, LimitRuleProperties.LimitRule limitRule) {
        ClusterFlowConfig clusterFlowConfig = JSON.parseObject(JSON.toJSONString(limitRule.getCluster()), ClusterFlowConfig.class);
        FlowRule flowRule = JSON.parseObject(JSON.toJSONString(limitRule), FlowRule.class);
        flowRule.setResource(resource);
        flowRule.setClusterConfig(clusterFlowConfig);
        return flowRule;
    }

}
