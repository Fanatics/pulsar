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
package org.apache.pulsar.sql.presto.decryptor;

import io.airlift.log.Logger;
import io.trino.spi.security.ConnectorIdentity;
import java.lang.reflect.Constructor;
import org.apache.commons.lang3.StringUtils;

public class DecryptionPluginUtil {
  private static final Logger log = Logger.get(DecryptionPluginUtil.class);
  public static MessageDecryptor getMessageDecryptor(String decryptPluginClassName,
      ConnectorIdentity connectorIdentity) throws MessageDecryptException {
    try {
      if (StringUtils.isNotBlank(decryptPluginClassName)) {
        Class<?> decryptClass = DecryptionPluginUtil.class.getClassLoader().loadClass(decryptPluginClassName);
        Constructor c = decryptClass.getConstructor(ConnectorIdentity.class);
        return (MessageDecryptor) c.newInstance(connectorIdentity);
      } else {
        return new NoopMessageDecryptor(connectorIdentity);
      }
    } catch (Throwable t) {
      t.printStackTrace();
      throw new MessageDecryptException(t.getMessage());
    }
  }
}
