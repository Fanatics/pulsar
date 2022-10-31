/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.pulsar.sql.presto.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.api.raw.MessageDecryptor;
import org.apache.pulsar.common.api.raw.NoopMessageDecryptor;

public class DecryptionUtil {
  public static MessageDecryptor getMessageDecryptor(String decryptPluginClassName) throws PulsarClientException {
    try {
      if(StringUtils.isNotBlank(decryptPluginClassName)) {
        Class<?> decryptClass = DecryptionUtil.class.getClassLoader().loadClass(decryptPluginClassName);
        return (MessageDecryptor) decryptClass.getDeclaredConstructor().newInstance();
      } else {
        return new NoopMessageDecryptor();
      }
    } catch (Throwable t) {
      throw new PulsarClientException(t);
    }
  }
}
