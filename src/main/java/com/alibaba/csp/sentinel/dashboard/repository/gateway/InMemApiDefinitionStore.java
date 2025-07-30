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
package com.alibaba.csp.sentinel.dashboard.repository.gateway;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.repository.rule.InMemoryRuleRepositoryAdapter;
import com.alibaba.nacos.common.utils.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Store {@link ApiDefinitionEntity} in memory.
 *
 * @author cdfive
 * @since 1.7.0
 */
@Component
public class InMemApiDefinitionStore extends InMemoryRuleRepositoryAdapter<ApiDefinitionEntity> {

    private static AtomicLong ids = new AtomicLong(0);

    @Override
    protected long nextId(ApiDefinitionEntity entity) {
        // 如果规则持久化到nacos后，重启后新增规则，ID会被覆盖，这里解决规则被覆盖的问题
        if (ids.intValue() == 0) {
            if (!CollectionUtils.isEmpty(this.findAllByApp(entity.getApp()))) {
                long maxId = Objects.requireNonNull(this.findAllByApp(entity.getApp())
                                .stream()
                                .max(Comparator.comparingLong(ApiDefinitionEntity::getId))
                                .orElse(null), "exist ApiDefinitionEntity id is null")
                        .getId();
                ids.set(maxId);
            }
        }
        return ids.incrementAndGet();
    }
}
