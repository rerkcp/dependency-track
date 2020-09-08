/*
 * This file is part of Dependency-Track.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.dependencytrack.policy;

import org.dependencytrack.model.Component;
import org.dependencytrack.model.Policy;
import org.dependencytrack.model.PolicyCondition;
import java.util.Optional;

/**
 * Evaluates a components Common Platform Enumeration (CPE) against a policy.
 *
 * @author Steve Springett
 * @since 4.0.0
 */
public class CpePolicyEvaluator extends AbstractPolicyEvaluator {

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyCondition.Subject supportedSubject() {
        return PolicyCondition.Subject.CPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PolicyConditionViolation> evaluate(final Policy policy, final Component component) {
        if (component.getCpe() == null) {
            return Optional.empty();
        }
        for (final PolicyCondition condition: super.extractSupportedConditions(policy)) {
            if (PolicyCondition.Operator.MATCHES == condition.getOperator()) {
                if (component.getCpe() != null && component.getCpe().contains(condition.getValue())) {
                    return Optional.of(new PolicyConditionViolation(condition, component));
                }
            } else if (PolicyCondition.Operator.NO_MATCH == condition.getOperator()) {
                if (component.getCpe() != null && !component.getCpe().contains(condition.getValue())) {
                    return Optional.of(new PolicyConditionViolation(condition, component));
                }
            }
        }
        return Optional.empty();
    }

}
