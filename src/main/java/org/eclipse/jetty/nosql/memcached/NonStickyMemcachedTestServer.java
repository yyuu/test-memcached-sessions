package org.eclipse.jetty.nosql.memcached;

// ========================================================================
// Copyright (c) 1996-2009 Mort Bay Consulting Pty. Ltd.
// ------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// and Apache License v2.0 which accompanies this distribution.
// The Eclipse Public License is available at 
// http://www.eclipse.org/legal/epl-v10.html
// The Apache License v2.0 is available at
// http://www.opensource.org/licenses/apache2.0.php
// You may elect to redistribute this code under either of these licenses. 
// ========================================================================



import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.nosql.kvs.KeyValueStoreSessionIdManager;
import org.eclipse.jetty.server.SessionIdManager;


/**
 * @version $Revision$ $Date$
 */
public class NonStickyMemcachedTestServer extends MemcachedTestServer
{
    public NonStickyMemcachedTestServer(int port)
    {
        super(port, 30, 10);
    }

    public NonStickyMemcachedTestServer(int port, int maxInactivePeriod, int scavengePeriod)
    {
        super(port, maxInactivePeriod, scavengePeriod, null);
    }
    
    public NonStickyMemcachedTestServer(int port, int maxInactivePeriod, int scavengePeriod, String sessionIdMgrConfig) {
        super(port, maxInactivePeriod, scavengePeriod, sessionIdMgrConfig);
    }
    
    public NonStickyMemcachedTestServer(int port, int maxInactivePeriod, int scavengePeriod, boolean saveAllAttributes)
    {
        super(port, maxInactivePeriod, scavengePeriod);
    }

    @Override
    public SessionIdManager newSessionIdManager(String config)
    {
        try {
            _idManager = (KeyValueStoreSessionIdManager) super.newSessionIdManager(config);
            _idManager.setSticky(false);
            return _idManager;
        }
        catch (Exception e)
        {
            throw new IllegalStateException();
        }
    }
}
