/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.confluent.common.metrics;

import io.confluent.common.utils.Time;

public final class KafkaMetric implements Metric {

  private final String name;
  private final String description;
  private final Object lock;
  private final Time time;
  private final Measurable measurable;
  private MetricConfig config;

  KafkaMetric(Object lock, String name, String description, Measurable measurable,
              MetricConfig config, Time time) {
    super();
    this.name = name;
    this.description = description;
    this.lock = lock;
    this.measurable = measurable;
    this.config = config;
    this.time = time;
  }

  MetricConfig config() {
    return this.config;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public String description() {
    return this.description;
  }

  @Override
  public double value() {
    synchronized (this.lock) {
      return value(time.milliseconds());
    }
  }

  double value(long timeMs) {
    return this.measurable.measure(config, timeMs);
  }

  public void config(MetricConfig config) {
    synchronized (lock) {
      this.config = config;
    }
  }
}